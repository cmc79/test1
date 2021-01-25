package com.mycorp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycorp.service.InsuredProductService;
import com.mycorp.service.ProductService;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

@Component
public class InsuredProductServiceImpl implements InsuredProductService {

    @Autowired
    ProductService producto;



    @Override
    public Producto[] obtenerProductosAsegurado(final List< DatosProductoAlta > productosCobertura,
                                                 final DatosContratacionPlan oDatosPlan ) {
        final List< Producto > productos = new ArrayList< >();
        if( productosCobertura != null && !productosCobertura.isEmpty() ) {
            for( final DatosProductoAlta productoCobertura : productosCobertura ) {
                productos.add( producto.obtenerProducto( productoCobertura, oDatosPlan ) );
            }
        }

        return productos.toArray( new Producto[ 0 ] );
    }
}
