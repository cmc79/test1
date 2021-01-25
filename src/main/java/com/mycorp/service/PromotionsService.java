package com.mycorp.service;



import java.util.List;

import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.type.FrecuenciaEnum;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaProducto;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface PromotionsService {

    public List<PromocionAplicada> recuperarPromocionesAgrupadas(
            final es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promocion[] promociones,
            final int numeroAsegurados);
    
	public double calculatePricePromotion(final DatosAlta oDatosAlta, final List<String> lExcepciones,
			final DatosContratacionPlan oDatosPlan, final Double[] descuentosTotales, final Double[] pagoTotal,
			final FrecuenciaEnum frecuencia, double css, List<PrimasPorProducto> listaProductoPorAseg,
			int contadorProducto, final TarifaProducto tarifaProducto);
}
