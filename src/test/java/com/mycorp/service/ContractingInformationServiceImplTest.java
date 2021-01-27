package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.service.impl.ContractingInformationServiceImpl;

import wscontratacion.contratacion.fuentes.parametros.DatosDomicilio;

@RunWith(MockitoJUnitRunner.class)
public class ContractingInformationServiceImplTest {

	
	@InjectMocks ContractingInformationServiceImpl contractingInformationServiceImpl;
	@Test
	public void givenDataValidCallObtenerInfoContratacionreturnObject() {
		//given
		DatosAltaAsegurados datosAltaMock = new DatosAltaAsegurados();
		DatosDomicilio datosDomicilioMock = new DatosDomicilio();
		datosAltaMock.setDomicilios(Arrays.asList(datosDomicilioMock));
		datosAltaMock.setFAlta("");
		datosAltaMock.setIdPoliza(Long.valueOf("1"));
		datosAltaMock.setIdColectivo(1);
		datosAltaMock.setIdDepartamento(1);
		datosAltaMock.setIdEmpresa(Long.valueOf("1"));
		datosAltaMock.setIdMediador(Long.valueOf("1"));
		datosAltaMock.setIdPlan(1);
		//then
		var response = contractingInformationServiceImpl.obtenerInfoContratacion(datosAltaMock, Collections.emptyList(), Collections.emptyList(), FrecuenciaEnum.ANUAL, 2);
		assertNotNull(response);
		assertEquals(response.getIdDepartamento().intValue(),datosAltaMock.getIdDepartamento());
		assertEquals(response.getIdPoliza().intValue(),datosAltaMock.getIdPoliza().intValue());
		assertEquals(response.getIdPlan().intValue(),datosAltaMock.getIdPlan());
	
	}

	
	
}
