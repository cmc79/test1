package com.mycorp.service;

import java.util.List;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

public interface InsuredProductService {

    Producto[] obtenerProductosAsegurado(final List<DatosProductoAlta> productosCobertura,
                                         final DatosContratacionPlan oDatosPlan);
}
