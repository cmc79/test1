package com.mycorp.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.service.impl.ProductServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Cobertura;
import wscontratacion.beneficiario.vo.ProductoCobertura;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

	@Mock
	CoverageService coberturas;

	@InjectMocks
	ProductServiceImpl productServiceImpl;

	@Test
	public void givenDatosProductoAltaCallObtenerProductosReturnListProducto() {

		// given

		final var datosPlanMock = mock(DatosContratacionPlan.class);
        final var coberturaMock = new Cobertura();
        final var datosProductoAltaMock = new DatosProductoAlta();
        datosProductoAltaMock.setIdProducto(1);
		// when

		when(coberturas.obtenerCoberturas( 1, datosPlanMock )).thenReturn(Arrays.asList(coberturaMock).toArray(new Cobertura[0]));

		// then
		var response = productServiceImpl.obtenerProducto(datosProductoAltaMock, datosPlanMock);

		assertNotNull(response);
		assertEquals(response.getIdProducto().intValue(), 1);
		assertThat(Arrays.asList(response.getCoberturas()),hasSize(1));

	}

	@Test
	public void givenDatosproductoCoberturaCallObtenerProductosReturnListProducto() {

		// given

		final var datosPlanMock = mock(DatosContratacionPlan.class);
        final var coberturaMock = new Cobertura();
        final var datosProductCoberturaMock = new ProductoCobertura();
        datosProductCoberturaMock.setIdProducto(1);
		// when

		when(coberturas.obtenerCoberturas( 1, datosPlanMock )).thenReturn(Arrays.asList(coberturaMock).toArray(new Cobertura[0]));

		// then
		var response = productServiceImpl.obtenerProducto(datosProductCoberturaMock, datosPlanMock);

		assertNotNull(response);
		assertEquals(response.getIdProducto().intValue(), 1);
		assertThat(Arrays.asList(response.getCoberturas()),hasSize(1));

	}
	
	@Test
	public void givenDatosListProductoCoberturaCallObtenerProductosReturnListProducto() {

		// given

		final var datosPlanMock = mock(DatosContratacionPlan.class);
        final var coberturaMock = new Cobertura();
        final var datosProductCoberturaMock = new ProductoCobertura();
        datosProductCoberturaMock.setIdProducto(1);
        final var listProductosCoberturaMock = Arrays.asList(datosProductCoberturaMock);
		// when

		when(coberturas.obtenerCoberturas( 1, datosPlanMock )).thenReturn(Arrays.asList(coberturaMock).toArray(new Cobertura[0]));

		// then
		var response = productServiceImpl.obtenerProductos(listProductosCoberturaMock, datosPlanMock);

		assertNotNull(response);
		assertEquals(response[0].getIdProducto().intValue(),1);
		assertThat(Arrays.asList(response[0].getCoberturas()),hasSize(1));

	}
	
}
