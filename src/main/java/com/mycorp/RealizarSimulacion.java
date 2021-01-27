package com.mycorp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.SimulationService;
import com.mycorp.service.SolverService;
import com.mycorp.service.TarificationService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.documentacion.Primas;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@Component
public class RealizarSimulacion implements IRealizarSimulacion{

	@Autowired
	TarificationService tarificationService;
	@Autowired
	SimulationService simulationsService;
	@Autowired
	SolverService solverService;



	/**
	 * Método que realiza las llamadas a las diferentes clases de simulación, para
	 * tarificar
	 *
	 * @param oDatosAlta Objeto del tipo DatosAlta
	 * @param lProductos Listado de productos que sólo se tendrán en cuenta en caso
	 *                   de inclusión de productos, en el resto de casos no aplica
	 * @return Map con diferentes valores obtenidos de la simulación, incluida la
	 *         lista de precios por asegurado
	 * @throws Exception             Excepción lanzada en caso de que haya errores
	 * @throws ExcepcionContratacion Excepción controlada
	 */
	public Map<String, Object> realizarSimulacion(final DatosAlta oDatosAlta, final List<ProductoPolizas> lProductos,
			final List<BeneficiarioPolizas> lBeneficiarios, final boolean desglosar,
			final Map<String, Object> hmValores) throws Exception, ExcepcionContratacion {

		final Map<String, Object> hmSimulacion = new HashMap<>();
		@SuppressWarnings("unchecked")
		final List<String> lExcepciones = (List<String>) hmValores.get("EXCEPCIONES");
		final DatosContratacionPlan oDatosPlan = (DatosContratacionPlan) hmValores
				.get(StaticVarsContratacion.DATOS_PLAN);

		final List<Primas> primas = new ArrayList<>();
		final Double descuentosTotales[] = { 0.0, 0.0, 0.0, 0.0 };
		final Double pagoTotal[] = { 0.0, 0.0, 0.0, 0.0 };
		final Double precioConPromocion[] = { 0.0, 0.0, 0.0, 0.0 };
		final List<List<PrimasPorProducto>> primasDesglosadas = new ArrayList<>();
		final List<List<PromocionAplicada>> promociones = new ArrayList<>();
		final List<List<com.mycorp.domain.dto.Recibo>> recibos = new ArrayList<>();
		final List<String> errores = new ArrayList<>();

		Set<FrecuenciaEnum> frecuenciasTarificar = tarificationService.getFrequenciesRate(oDatosAlta, lBeneficiarios,
				hmValores);

		final Collection<Callable<TarificacionPoliza>> solvers = new ArrayList<>(0);
		for (final FrecuenciaEnum frecuencia : frecuenciasTarificar) {
			solvers.add(solverService.simularPolizaFrecuencia(hmValores, oDatosAlta, lProductos, lBeneficiarios,
					frecuencia));
		}

		final List<TarificacionPoliza> resultadoSimulacionesList = simulationsService
				.getResultadoSimulaciones(solvers);

		simulationsService.getDataSimulation(oDatosAlta, lExcepciones, oDatosPlan, primas, descuentosTotales, pagoTotal,
				precioConPromocion, primasDesglosadas, promociones, recibos, errores, frecuenciasTarificar,
				resultadoSimulacionesList);

		return simulationsService.addDataSimulation(desglosar, hmSimulacion, primas, descuentosTotales, pagoTotal, precioConPromocion,
				primasDesglosadas, promociones, recibos, errores);
	}











}
