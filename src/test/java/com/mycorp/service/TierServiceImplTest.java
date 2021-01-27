package com.mycorp.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.service.impl.TierServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TierServiceImplTest   {
	@InjectMocks TierServiceImpl tierServiceImp;
	
	@Test
	public void givenDatoAltaCorrectThenReturnObject() {
	   
		//Given
		final var coeficicenteTier="1.2#3.4#5.1#2.6";
		DatosAltaAsegurados mockDatosAltaAsegurados = new DatosAltaAsegurados();
		mockDatosAltaAsegurados.setCoeficientesTier(coeficicenteTier);
	
		//then
		var response = tierServiceImp.obtenerTier(mockDatosAltaAsegurados);
		
        assertNotNull(response);
        assertThat(Arrays.asList(response.getTierProductos()), hasSize(3));
	}
	
	@Test
	public void givenDatoAltaCorrectWithCoeficientTierEmptyThenReturnNull() {
	   
		//Given
		final var coeficicenteTier=StringUtils.EMPTY;
		DatosAltaAsegurados mockDatosAltaAsegurados = new DatosAltaAsegurados();
		mockDatosAltaAsegurados.setCoeficientesTier(coeficicenteTier);
	
		//then
		var response = tierServiceImp.obtenerTier(mockDatosAltaAsegurados);
		
        assertNull(response);
    
	}
	

	
}
