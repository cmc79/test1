package com.mycorp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.Recibo;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.domain.dto.type.TipoPromocionEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.PaymentsReceiptsService;
import com.mycorp.service.PromotionService;
import com.mycorp.service.SimulationService;
import com.mycorp.service.TarificationService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.documentacion.Primas;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaBeneficiario;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaProducto;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@Component
public class SimulationServiceImpl implements SimulationService {

	private static final Logger LOG = LoggerFactory.getLogger(SimulationServiceImpl.class);
	private static final int TIMEOUT = 30;
	private static final int NUMERO_HILOS = 4;
	private final ExecutorService pool = Executors.newFixedThreadPool(NUMERO_HILOS);


	@Autowired
	TarificationService tarificationService;
	@Autowired
	SimulationService simulationsService;
	@Autowired
	PromotionService promotionsService;
	@Autowired
	PaymentsReceiptsService paymentsReceiptsService;

	@Override
	public List<TarificacionPoliza> getResultadoSimulaciones(Collection<Callable<TarificacionPoliza>> solvers)
			throws ExcepcionContratacion {
		final CompletionService<TarificacionPoliza> ecs = new ExecutorCompletionService<>(pool);
		int n = 0;
		for (final Callable<TarificacionPoliza> s : solvers) {
			try {
				ecs.submit(s);
				n++;
			} catch (final RuntimeException ree) {
				LOG.error("RejectedExecutionException con el metodo " + s.toString(), ree);
			}
		}
		final List<TarificacionPoliza> resultadoSimulaciones = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			try {
				final Future<TarificacionPoliza> future = ecs.poll(TIMEOUT, TimeUnit.SECONDS);
				if (future != null) {
					resultadoSimulaciones.add(future.get());
				} else {
					LOG.error("La llamada asincrona al servicio de simulacion ha fallado por timeout");
				}
			} catch (final InterruptedException e) {
				LOG.error("InterruptedException", e);
			} catch (final ExecutionException e) {
				LOG.error("ExecutionException", e);
				throw new ExcepcionContratacion(e.getCause().getMessage());
			}
		}
		return resultadoSimulaciones;
	}

	@Override
	public Map<String, Object> addDataSimulation(boolean desglosar, Map<String, Object> hmSimulacion,
			List<Primas> primas, Double[] descuentosTotales, Double[] pagoTotal, Double[] precioConPromocion,
			List<List<PrimasPorProducto>> primasDesglosadas, List<List<PromocionAplicada>> promociones,
			List<List<Recibo>> recibos, List<String> errores) {

		hmSimulacion.put(StaticVarsContratacion.PRIMAS_SIMULACION, primas);
		hmSimulacion.put(StaticVarsContratacion.PRIMAS_SIMULACION_DESGLOSE, primasDesglosadas);
		hmSimulacion.put(StaticVarsContratacion.SIMULACION_PROVINCIA, "Madrid");
		hmSimulacion.put(StaticVarsContratacion.HAY_DESGLOSE, desglosar);
		hmSimulacion.put(StaticVarsContratacion.DESCUENTOS_TOTALES, descuentosTotales);
		hmSimulacion.put(StaticVarsContratacion.TOTAL_ASEGURADOS, primas);
		hmSimulacion.put(StaticVarsContratacion.PROMOCIONES_SIMULACION, promociones);
		hmSimulacion.put(StaticVarsContratacion.RECIBOS_SIMULACION, recibos);
		hmSimulacion.put(StaticVarsContratacion.PAGO_TOTAL, pagoTotal);
		hmSimulacion.put(StaticVarsContratacion.ERROR, errores);

		// Si en la simulación hay apliacada alguna promoción
		// de descuento sobre la prima
		if (hayPromocionDescuento(promociones)) {
			hmSimulacion.put(StaticVarsContratacion.PAGO_TOTAL, precioConPromocion);
			hmSimulacion.put(StaticVarsContratacion.PRECIOS_SIN_PROMOCION_SIMULACION, pagoTotal);
		}
		return hmSimulacion;
	}

	private boolean hayPromocionDescuento(final List<List<PromocionAplicada>> promocionesAplicadas) {
		boolean codigoAplicado = Boolean.FALSE;
		if (promocionesAplicadas != null) {
			for (final List<PromocionAplicada> promociones : promocionesAplicadas) {
				for (final PromocionAplicada promocion : promociones) {
					if (promocion != null
							&& TipoPromocionEnum.DESCUENTO_PORCENTAJE.equals(promocion.getTipoPromocion())) {
						codigoAplicado = Boolean.TRUE;
					}
				}
			}
		}
		return codigoAplicado;
	}

	@Override
	public void getDataSimulation(final DatosAlta oDatosAlta, final List<String> lExcepciones,
			final DatosContratacionPlan oDatosPlan, final List<Primas> primas, final Double[] descuentosTotales,
			final Double[] pagoTotal, final Double[] precioConPromocion,
			final List<List<PrimasPorProducto>> primasDesglosadas, final List<List<PromocionAplicada>> promociones,
			final List<List<com.mycorp.domain.dto.Recibo>> recibos, final List<String> errores,
			Set<FrecuenciaEnum> frecuenciasTarificar, final List<TarificacionPoliza> resultadoSimulacionesList)
			throws ExcepcionContratacion {
		
		for (final FrecuenciaEnum frecuencia : frecuenciasTarificar) {
			final Tarificacion retorno = tarificationService.getPrincingPolicy(errores, resultadoSimulacionesList);

			int contadorBeneficiario = 0;
			double css = 0;
			for (final TarifaBeneficiario tarifaBeneficiario : retorno.getTarifas().getTarifaBeneficiarios()) {
				List<PrimasPorProducto> listaProductoPorAseg = new ArrayList<>();
				if (primasDesglosadas.size() > contadorBeneficiario) {
					listaProductoPorAseg = primasDesglosadas.get(contadorBeneficiario);
				} else {
					primasDesglosadas.add(listaProductoPorAseg);
				}

				Primas primaAsegurado = new Primas();
				if (primas.size() < contadorBeneficiario) {
					primas.add(primaAsegurado);
				}

				int contadorProducto = 0;
				for (final TarifaProducto tarifaProducto : tarifaBeneficiario.getTarifasProductos()) {

					css = promotionsService.calculatePricePromotion(oDatosAlta, lExcepciones, oDatosPlan,
							descuentosTotales, pagoTotal, frecuencia, css, listaProductoPorAseg, contadorProducto,
							tarifaProducto);
					contadorProducto++;
				}
				contadorBeneficiario++;
			}

			// Promociones aplicadas a la simulación
			promociones.add(promotionsService.recuperarPromocionesAgrupadas(
					retorno.getPromociones().getListaPromocionesPoliza(), contadorBeneficiario));

			// Lista de recibos del primer año
			if (retorno.getRecibos() != null) {
				recibos.add(paymentsReceiptsService.toReciboList(retorno.getRecibos().getListaRecibosProductos()));

				// Se calcula el precio total con promoción
				// Es el importe del primer recibo sin el impuesto del consorcio
				precioConPromocion[frecuencia.getValor() - 1] = retorno.getRecibos().getReciboPoliza().getRecibos()[0]
						.getImporte() - css;
			}
		}
	}

}
