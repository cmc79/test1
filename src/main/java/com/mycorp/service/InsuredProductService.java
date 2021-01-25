package com.mycorp.service;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

import java.util.List;

public interface InsuredProductService {

    Producto[] obtenerProductosAsegurado(final List<DatosProductoAlta> productosCobertura,
                                         final DatosContratacionPlan oDatosPlan);
}
