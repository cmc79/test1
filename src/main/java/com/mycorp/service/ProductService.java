package com.mycorp.service;

import java.util.List;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import wscontratacion.beneficiario.vo.ProductoCobertura;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

public interface ProductService {

    es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto obtenerProducto(final ProductoCobertura productoCobertura, final DatosContratacionPlan oDatosPlan);

    es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto obtenerProducto(final DatosProductoAlta productoAlta, final DatosContratacionPlan oDatosPlan);

    es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto[] obtenerProductos(final List<ProductoCobertura> productosCobertura,
                                                                                           final
                                                                                           DatosContratacionPlan oDatosPlan);
}
