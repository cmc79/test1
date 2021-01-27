package com.mycorp.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.impl.TarificationServiceImpl;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@RunWith(MockitoJUnitRunner.class)
public class TarificationServiceImplTest {


	@InjectMocks
	TarificationServiceImpl tarificacionServiceImpl;

	@Test(expected = NullPointerException.class)
	public void givenParametersNullCallGetFrequenciesRateThenReturnException() {

		tarificacionServiceImpl.getFrequenciesRate(null, null, null);
	
	}
	
	@Test
	public void givenDataValidEmptyCallGetFrequenciesRateThenReturnAllListFrecuencies() {

		// given
		final Map<String,Object> hmValores = new HashMap<String,Object>();
		var response = tarificacionServiceImpl.getFrequenciesRate(null, null, hmValores);
		
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(response.size(),FrecuenciaEnum.getListaFrecuencias().size());

	}
	
	@Test
	public void givenDataBeneficiariesValidCallGetFrequenciesRateThenReturnListFrecuenciaEnum() {

		// given
		final Map<String,Object> hmValores = new HashMap<String,Object>();
		final var datosAlta = mock(DatosAlta.class);
		final List<BeneficiarioPolizas> lBeneficiarios = Collections.emptyList();
		//when
		when(datosAlta.getGenFrecuenciaPago()).thenReturn(StaticVarsContratacion.FRECUENCIA_PAGO_ANUAL);
		//then
		var response = tarificacionServiceImpl.getFrequenciesRate(datosAlta, lBeneficiarios, hmValores);
		
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertThat(response,hasSize(1));

	}	

	@Test(expected = NullPointerException.class)
	public void givenDataAltaWithNullFrecuenciaPagoCallGetFrequenciesRateThenReturnException() {

		// given
		final Map<String,Object> hmValores = new HashMap<String,Object>();
		hmValores.put(StaticVarsContratacion.FREC_MENSUAL , "FREC_MENSUAL");
		final var datosAlta = mock(DatosAlta.class);
		final List<BeneficiarioPolizas> lBeneficiarios = Collections.emptyList();

		//then
		tarificacionServiceImpl.getFrequenciesRate(datosAlta, lBeneficiarios, hmValores);
		
	}	
	
	@Test
	public void givenHmValoresWithoutBeneficiariosValidCallGetFrequenciesRateThenReturnListFrecuenciaEnum() {

		// given
		final Map<String,Object> hmValores = new HashMap<String,Object>();
		hmValores.put(StaticVarsContratacion.FREC_MENSUAL , "FREC_MENSUAL");
		final var datosAlta = mock(DatosAlta.class);
		final List<BeneficiarioPolizas> lBeneficiarios = null;

		//then
		var response = tarificacionServiceImpl.getFrequenciesRate(datosAlta, lBeneficiarios, hmValores);
		
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertThat(response,hasSize(1));

	}	
	
	@Test(expected = ExcepcionContratacion.class)
	public void givenDataListSimulacionesEmptyFrecuenciaPagoCallGetPrincingPolicyThenReturnException() throws ExcepcionContratacion {

		// given

		final List<TarificacionPoliza> resultadoSimulacionesList = Collections.emptyList();
		final List<String> errores = Collections.emptyList();
		//then
		tarificacionServiceImpl.getPrincingPolicy(errores, resultadoSimulacionesList);
		
	}	
	
	@Test(expected =  ExcepcionContratacion.class)
	public void givenDataListSimulacionesNullCallGetPrincingPolicyThenReturnException() throws ExcepcionContratacion {

		tarificacionServiceImpl.getPrincingPolicy(null, null);
		
	}	
	
	@Test
	public void givenSimulacionWithTarficacionGetPrincingPolicyThenReturnObject() throws ExcepcionContratacion {

		// given

		final var tarificacionPolizaMock = mock(TarificacionPoliza.class); 
		final var tarifiacionMock = mock(Tarificacion.class);
		final var codigoError = "codeError";
		final List<TarificacionPoliza> resultadoSimulacionesList = Arrays.asList(tarificacionPolizaMock);
		final List<String> errores =new ArrayList<>();
		//when
		when(tarificacionPolizaMock.getTarificacion()).thenReturn(tarifiacionMock);
		when(tarificacionPolizaMock.getCodigoError()).thenReturn(codigoError);
		//then
		var response = tarificacionServiceImpl.getPrincingPolicy(errores, resultadoSimulacionesList);
		
		assertNotNull(response);
		assertEquals(response,tarifiacionMock);
		assertThat(errores, hasSize(1));
		
	}		
}
