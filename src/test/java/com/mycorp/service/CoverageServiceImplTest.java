package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.service.impl.CoverageServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosCobertura;
import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosPlanProducto;

@RunWith(MockitoJUnitRunner.class)
public class CoverageServiceImplTest {
	@InjectMocks CoverageServiceImpl coverageServiceImpl;
	

	
	@Test
	public void givenDataValidCallObtenerCoberturasWithCoberturasreturnListCobertura() {
		
		//Given
		
		Integer id = 1;
		final var idProducto =  Long.valueOf(id);
        final var idCobertura = Long.valueOf(id);
		DatosContratacionPlan datosContratacionMock = mock(DatosContratacionPlan.class);
		DatosPlanProducto datosPlanProductoMock = mock(DatosPlanProducto.class);
	    DatosCobertura datosCoberturaMock = mock(DatosCobertura.class);		
		
		final var productos = Arrays.asList(datosPlanProductoMock);
		final var coberturas = Arrays.asList(datosCoberturaMock);

		//when

		when(datosContratacionMock.getProductos()).thenReturn(productos);
		when(datosPlanProductoMock.getIdProducto()).thenReturn(idProducto);		
		when(datosPlanProductoMock.getCoberturas()).thenReturn(coberturas);
		when(datosCoberturaMock.getCapitalMinimo()).thenReturn(1);
		when(datosCoberturaMock.getIdCobertura()).thenReturn(idCobertura);
		when(datosCoberturaMock.isSwObligatorio()).thenReturn(true);		
		
		//then
		var response = coverageServiceImpl.obtenerCoberturas(id, datosContratacionMock);
		
		assertNotNull(response);
		assertEquals(response.length,1);
		assertEquals(response[0].getIdCobertura(),id);
		
	}	
	@Test
	public void givenDataValidCallObtenerCoberturasNoWithCoveragesreturnZeroCoverage (){
		
		//Given
		DatosContratacionPlan datosContratacionMock = mock(DatosContratacionPlan.class);
		
		//when
		when(datosContratacionMock.getProductos()).thenReturn(Collections.emptyList());	
		
		//then
		var response = coverageServiceImpl.obtenerCoberturas(1, datosContratacionMock);
		
		assertNotNull(response);
		assertEquals(response.length,0);
		
	}	
	
	@Test
	public void givenDataValidCallObtenerCoberturasNoMatchProductreturnCoberturasReturnListZero() {
		
		//Given
		DatosContratacionPlan datosContratacionMock = mock(DatosContratacionPlan.class);
		DatosPlanProducto datosPlanProductoMock = mock(DatosPlanProducto.class);	
		final var productos = Arrays.asList(datosPlanProductoMock);

		//when

		when(datosContratacionMock.getProductos()).thenReturn(productos);
		when(datosPlanProductoMock.getIdProducto()).thenReturn(Long.valueOf(2));				
		//then
		var response = coverageServiceImpl.obtenerCoberturas(1, datosContratacionMock);
		
		assertNotNull(response);
		assertEquals(response.length,0);
		
	}
	
	@Test
	public void givenDataValidCallObtenerCoberturasWithCoberturasWithSwObligatorioFalseReturnListZero() {
		
		//Given
		
		final var idProducto =  Long.valueOf(1);
		DatosContratacionPlan datosContratacionMock = mock(DatosContratacionPlan.class);
		DatosPlanProducto datosPlanProductoMock = mock(DatosPlanProducto.class);
	    DatosCobertura datosCoberturaMock = mock(DatosCobertura.class);		
		
		final var productos = Arrays.asList(datosPlanProductoMock);
		final var coberturas = Arrays.asList(datosCoberturaMock);

		//when

		when(datosContratacionMock.getProductos()).thenReturn(productos);
		when(datosPlanProductoMock.getIdProducto()).thenReturn(idProducto);		
		when(datosPlanProductoMock.getCoberturas()).thenReturn(coberturas);
		when(datosCoberturaMock.isSwObligatorio()).thenReturn(false);		
		
		//then
		var response = coverageServiceImpl.obtenerCoberturas(1, datosContratacionMock);
		
		assertNotNull(response);
		assertEquals(response.length,0);
		
	}	
	
}
