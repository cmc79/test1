package com.mycorp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.DatosAseguradoInclusion;
import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.domain.dto.type.TipoPromocionEnum;
import com.mycorp.service.PromotionService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosPlanProducto;
import es.sanitas.bravo.ws.stubs.contratacionws.documentacion.Primas;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promocion;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaDesglosada;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaProducto;
import lombok.extern.slf4j.Slf4j;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;
import wscontratacion.contratacion.fuentes.parametros.DatosAsegurado;

@Slf4j
@Component
public class PromotionServiceImpl implements PromotionService {

	@Override
	public List<PromocionAplicada> recuperarPromocionesAgrupadas(final Promocion[] promociones,
			final int numeroAsegurados) {

		List<PromocionAplicada> promocionesAgrupadas = new ArrayList<>();
		if (promociones != null && promociones.length > 0) {
			log.debug(promociones.toString());
			final int numPromociones = promociones.length / numeroAsegurados;
			promocionesAgrupadas = toPromocionAplicadaList(Arrays.copyOfRange(promociones, 0, numPromociones));
		}
		return promocionesAgrupadas;
	}

	private List<PromocionAplicada> toPromocionAplicadaList(final Promocion[] promociones) {
		final List<PromocionAplicada> promocionesParam = new ArrayList<>();

		for (final Promocion promocion : promociones) {
			final PromocionAplicada promocionParam = toPromocionAplicada(promocion);
			if (promocionParam != null) {
				promocionesParam.add(promocionParam);
			}
		}
		return promocionesParam;
	}

	private PromocionAplicada toPromocionAplicada(final Promocion promocion) {
		PromocionAplicada promocionParam = null;
		if (promocion != null) {
			promocionParam = new PromocionAplicada();
			promocionParam.setIdPromocion(
					promocion.getIdPromocion() != null ? Long.valueOf(promocion.getIdPromocion()) : null);
			promocionParam.setDescripcion(promocion.getDescripcion());
			promocionParam.setTipoPromocion(TipoPromocionEnum.obtenerTipoPromocion(promocion.getTipo()));
		}
		return promocionParam;
	}

	@Override
	public double calculatePricePromotion(final DatosAlta oDatosAlta, final List<String> lExcepciones,
			final DatosContratacionPlan oDatosPlan, final Double[] descuentosTotales, final Double[] pagoTotal,
			final FrecuenciaEnum frecuencia, double css, List<PrimasPorProducto> listaProductoPorAseg,
			int contadorProducto, final TarifaProducto tarifaProducto) {
		if ((tarifaProducto.getIdProducto() != 389
				|| !comprobarExcepcion(lExcepciones, StaticVarsContratacion.PROMO_ECI_COLECTIVOS)
				|| hayTarjetas(oDatosAlta)) && tarifaProducto.getIdProducto() != 670
				|| !comprobarExcepcion(lExcepciones, StaticVarsContratacion.PROMO_FARMACIA)
				|| hayTarjetas(oDatosAlta)) {

			PrimasPorProducto oPrimasProducto = addProductsBonus(oDatosPlan, listaProductoPorAseg, contadorProducto,
					tarifaProducto);

			final TarifaDesglosada tarifaDesglosada = tarifaProducto.getTarifaDesglosada();
			final Primas primaProducto = oPrimasProducto.getPrimaProducto();

			// Se calcula el CSS total para poder calcular el precio con promoción
			css += tarifaDesglosada.getCss();

			/**
			 * No sumamos tarifaDesglosada.getCss() + tarifaDesglosada.getCssre() porque la
			 * Compensación del Consorcio de Seguros sólo se aplica en la primera
			 * mensualidad. Y queremos mostrar al usuario el precio de todos los meses.
			 */
			final double pago = tarifaDesglosada.getPrima() + tarifaDesglosada.getISPrima();
			final double descuento = tarifaDesglosada.getDescuento();
			applyDeduction(frecuencia, primaProducto, descuento);
			descuentosTotales[frecuencia.getValor() - 1] += tarifaDesglosada.getDescuento();
			pagoTotal[frecuencia.getValor() - 1] += pago + tarifaDesglosada.getDescuento();

		}
		return css;
	}

	/**
	 * @param oDatosAlta
	 * @return true si el titular o alguno de los asegurados tiene tarjeta de
	 *         sanitas.
	 */
	private boolean hayTarjetas(final DatosAlta oDatosAlta) {
		boolean tieneTarjeta = false;
		if (oDatosAlta != null && oDatosAlta.getTitular() != null) {
			if ("S".equals(oDatosAlta.getTitular().getSwPolizaAnterior())) {
				tieneTarjeta = true;
			}
		}
		if (oDatosAlta != null && oDatosAlta.getAsegurados() != null && oDatosAlta.getAsegurados().size() > 0) {
			@SuppressWarnings("unchecked")
			final Iterator<DatosAseguradoInclusion> iterAseg = oDatosAlta.getAsegurados().iterator();
			while (iterAseg.hasNext()) {
				final DatosAsegurado aseg = iterAseg.next();
				if ("S".equals(aseg.getSwPolizaAnterior())) {
					tieneTarjeta = true;
				}
			}
		}
		return tieneTarjeta;
	}

	/**
	 * Popula una lista de Recibos con la información de los recibos de la
	 * simulación.
	 *
	 * @param recibos recibos del primer año de la simulación
	 * @return lista de Recibos con la información de los recibos de la simulación.
	 */

	/**
	 * Comprueba si pertenece la excepcion a la lista.
	 *
	 * @param lExcepciones Lista de excepciones.
	 * @param comprobar    Dato a comprobar.
	 * @return True si pertenece false en caso contrario.
	 */
	private static boolean comprobarExcepcion(final List<String> lExcepciones, final String comprobar) {
		log.debug("Se va a comprobar si " + comprobar + " estÃ¡ en la lista " + lExcepciones);
		boolean bExcepcion = false;
		if (comprobar != null && lExcepciones != null && lExcepciones.contains(comprobar)) {
			bExcepcion = true;
		}
		return bExcepcion;
	}

	private PrimasPorProducto addProductsBonus(final DatosContratacionPlan oDatosPlan,
			List<PrimasPorProducto> listaProductoPorAseg, int contadorProducto, final TarifaProducto tarifaProducto) {
		PrimasPorProducto oPrimasProducto = new PrimasPorProducto();
		if (listaProductoPorAseg.size() > contadorProducto) {
			oPrimasProducto = listaProductoPorAseg.get(contadorProducto);
		} else {
			oPrimasProducto.setCodigoProducto(tarifaProducto.getIdProducto().intValue());
			oPrimasProducto.setNombreProducto(tarifaProducto.getDescripcion());
			final DatosPlanProducto producto = getDatosProducto(oDatosPlan, tarifaProducto.getIdProducto());
			if (producto != null) {
				oPrimasProducto.setObligatorio(producto.isSwObligatorio() ? "S" : "N");
				oPrimasProducto.setNombreProducto(producto.getDescComercial());
			}
			listaProductoPorAseg.add(oPrimasProducto);
		}
		return oPrimasProducto;
	}

	private DatosPlanProducto getDatosProducto(final DatosContratacionPlan oDatosPlan, final long idProducto) {
		for (final DatosPlanProducto producto : oDatosPlan.getProductos()) {
			if (producto.getIdProducto() == idProducto) {
				return producto;
			}
		}
		return null;
	}

	private Primas applyDeduction(final FrecuenciaEnum frecuencia, final Primas primaProducto, final double descuento) {
		switch (frecuencia.getValor()) {
		case 1:
			// Mensual
			primaProducto.setPrima("" + descuento);
			break;
		case 2:
			// Trimestral
			primaProducto.setPrima("" + descuento);
			break;
		case 3:
			// Semestral
			primaProducto.setPrima("" + descuento * 2);
			break;
		case 4:
			// Anual
			primaProducto.setPrima("" + descuento * 2);
			break;
		}

		return primaProducto;
	}

}
