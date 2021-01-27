package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.service.impl.PromotionInformationServiceImpl;

import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@RunWith(MockitoJUnitRunner.class)
public class PromotionInformationTest {


	@InjectMocks
	PromotionInformationServiceImpl promotionInformationServiceImpl;

	@Test
	public void givenDataValidCallObtenerInfoPromocionesReturnListBeneficiaries() {
		
		//given
		final var datosAltaAseguradoMock = new DatosAltaAsegurados();
		datosAltaAseguradoMock.setCodigoPromocional("1");;

		
		//then
		var response = promotionInformationServiceImpl.obtenerInfoPromociones(datosAltaAseguradoMock);
	
		assertNotNull(response);
		assertEquals(response.getPromociones()[0].getIdPromocion(),"1");

	}

	@Test
	public void givenDataNoValidCallObtenerInfoPromocionesReturnnull() {
		
		//given

		final var datosAltaMock = new DatosAlta();
		//then
		var response = promotionInformationServiceImpl.obtenerInfoPromociones(datosAltaMock);
	
		assertNull(response);


	}
}
