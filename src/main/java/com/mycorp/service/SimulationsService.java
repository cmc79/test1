package com.mycorp.service;




import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.Recibo;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.documentacion.Primas;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface SimulationsService {

    public List<TarificacionPoliza> getResultadoSimulaciones(
            Collection<Callable<TarificacionPoliza>> solvers)
                    throws ExcepcionContratacion;
    /**
     * Add data result simulations
     * @param desglosar
     * @param hmSimulacion
     * @param primas
     * @param descuentosTotales
     * @param pagoTotal
     * @param precioConPromocion
     * @param primasDesglosadas
     * @param promociones
     * @param recibos
     * @param errores
     * @return
     */
	public Map<String, Object> addDataSimulation(final boolean desglosar, final Map<String, Object> hmSimulacion,
			final List<Primas> primas, final Double[] descuentosTotales, final Double[] pagoTotal,
			final Double[] precioConPromocion, final List<List<PrimasPorProducto>> primasDesglosadas,
			final List<List<PromocionAplicada>> promociones, final List<List<com.mycorp.domain.dto.Recibo>> recibos,
			final List<String> errores);
	/**
	 * Get data to execute simulations
	 * @param oDatosAlta
	 * @param lExcepciones
	 * @param oDatosPlan
	 * @param primas
	 * @param descuentosTotales
	 * @param pagoTotal
	 * @param precioConPromocion
	 * @param primasDesglosadas
	 * @param promociones
	 * @param recibos
	 * @param errores
	 * @param frecuenciasTarificar
	 * @param resultadoSimulacionesList
	 */
	public void getDataSimulation(DatosAlta oDatosAlta, List<String> lExcepciones, DatosContratacionPlan oDatosPlan,
			List<Primas> primas, Double[] descuentosTotales, Double[] pagoTotal, Double[] precioConPromocion,
			List<List<PrimasPorProducto>> primasDesglosadas, List<List<PromocionAplicada>> promociones,
			List<List<Recibo>> recibos, List<String> errores, Set<FrecuenciaEnum> frecuenciasTarificar,
			List<TarificacionPoliza> resultadoSimulacionesList) throws ExcepcionContratacion ;
}
