package com.mycorp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycorp.service.CoverageService;
import com.mycorp.service.ProductService;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import wscontratacion.beneficiario.vo.ProductoCobertura;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	CoverageService coberturas;

	@Override
	public es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto obtenerProducto(
			final DatosProductoAlta productoAlta, final DatosContratacionPlan oDatosPlan) {
		final es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto producto = new es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto();
		final int idProducto = productoAlta.getIdProducto();
		producto.setIdProducto(idProducto);
		producto.setListaCoberturas(coberturas.obtenerCoberturas(idProducto, oDatosPlan));
		return producto;
	}

	@Override
	public es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto obtenerProducto(
			final ProductoCobertura productoCobertura, final DatosContratacionPlan oDatosPlan) {
		final es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto producto = new es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto();
		final int idProducto = productoCobertura.getIdProducto();
		producto.setIdProducto(idProducto);
		producto.setListaCoberturas(coberturas.obtenerCoberturas(idProducto, oDatosPlan));
		return producto;
	}

	@Override
	public es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto[] obtenerProductos(
			final List<ProductoCobertura> productosCobertura, final DatosContratacionPlan oDatosPlan) {
		final List<es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto> productos = new ArrayList<>();
		if (productosCobertura != null && !productosCobertura.isEmpty()) {
			for (final ProductoCobertura producto : productosCobertura) {
				productos.add(obtenerProducto(producto, oDatosPlan));
			}
		}

		return productos.toArray(new es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto[0]);
	}
}
