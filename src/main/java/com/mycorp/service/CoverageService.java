package com.mycorp.service;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Cobertura;

public interface CoverageService {

    Cobertura[] obtenerCoberturas(final int idProducto, final DatosContratacionPlan oDatosPlan);
}
