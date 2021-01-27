package com.mycorp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.SimulationService;
import com.mycorp.service.SolverService;
import com.mycorp.service.TarificationService;

import wscontratacion.contratacion.fuentes.parametros.DatosAlta;


/**
 * Unit test for simple App.
 */
@RunWith(MockitoJUnitRunner.class)
public class RealizarSimulacionTest  {

	@Mock
	TarificationService tarificationService;
	@Mock
	SimulationService simulationsService;
	@Mock
	SolverService solverService;
	@InjectMocks
	RealizarSimulacion realizarSimulacion;

	@Test
	public void givenDatoValidNoFrecuenciesTarificationAltaCorrectThenReturnObject() throws ExcepcionContratacion, Exception {
	   
		// given
		@SuppressWarnings("unchecked")
		final Map<String,Object> hmValores = mock(Map.class);
		final var oDatosAlta = mock(DatosAlta.class);
		@SuppressWarnings("unchecked")
		final List<ProductoPolizas> lProductos = mock(List.class);

		@SuppressWarnings("unchecked")
		final List<BeneficiarioPolizas> lBeneficiarios = mock(List.class);

	
		//then
		var response = realizarSimulacion.realizarSimulacion(oDatosAlta, lProductos, lBeneficiarios, true, hmValores);
		
        assertNotNull(response);
        assertEquals(response.size(),0);
	}
	@Test
	public void givenDatoValidFrecuenciesTarificationAltaCorrectThenReturnObject() throws ExcepcionContratacion, Exception {
	   
		// given
		@SuppressWarnings("unchecked")
		final Map<String,Object> hmValores = mock(Map.class);
		final var oDatosAlta = mock(DatosAlta.class);
		@SuppressWarnings("unchecked")
		final List<ProductoPolizas> lProductos = mock(List.class);
		@SuppressWarnings("unchecked")
		final List<BeneficiarioPolizas> lBeneficiarios = mock(List.class);
		final Set<FrecuenciaEnum> frecuenciasTarificar = new TreeSet<FrecuenciaEnum>();
		frecuenciasTarificar.add(FrecuenciaEnum.ANUAL);
		
		//when
		when(tarificationService.getFrequenciesRate(oDatosAlta, lBeneficiarios,hmValores)).thenReturn(frecuenciasTarificar);
		//then
		var response = realizarSimulacion.realizarSimulacion(oDatosAlta, lProductos, lBeneficiarios, true, hmValores);
		
        assertNotNull(response);

	}

}
