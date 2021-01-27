package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.service.impl.InsuredProductServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto;
import wscontratacion.contratacion.fuentes.parametros.DatosProductoAlta;

@RunWith(MockitoJUnitRunner.class)
public class InsuredProductServiceImplTest {

	@Mock
	ProductService productoService;

	@InjectMocks
	InsuredProductServiceImpl insuredProductServiceImpl;

	@Test
	public void givenDataValidCallObtenerProductosAseguradoReturnListProducto() {
		
		//given
		final var datosProductoMock = new DatosProductoAlta();
	
		final List<DatosProductoAlta> productosCoberturaMock = Arrays.asList(datosProductoMock);
		final var productoMock = mock(Producto.class);
		final var datosPlanMock = mock(DatosContratacionPlan.class);

		//when

		when(productoService.obtenerProducto(datosProductoMock, datosPlanMock)).thenReturn(productoMock);
		
		//then
		var response = insuredProductServiceImpl.obtenerProductosAsegurado(productosCoberturaMock, datosPlanMock);
	
		assertNotNull(response);
		assertEquals(response.length,1);
		

	}

}
