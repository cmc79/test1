package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.RESTResponse;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.impl.SolverServiceImpl;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Beneficiario;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Error;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoContratacion;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoPromociones;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoTier;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarifas;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@RunWith(MockitoJUnitRunner.class)
public class SolverServiceImplTest {



	@Mock
	private SimulacionWS servicioSimulacion;

	@Mock
	PromotionInformationService promotionInformationService;
	@Mock
	TierService tierService;
	@Mock
	BeneficiariesService beneficiariesService;
	@Mock
	ContractingInformation contractingInformationService;
	
	@InjectMocks
	SolverServiceImpl solverServiceImpl;

	@Test(expected = NullPointerException.class)
	public void givenParameterNullCallSimularPolizaFrecuenciaThenReturnException() throws Exception {

		var response = solverServiceImpl.simularPolizaFrecuencia(null, null, null, null, null);
		response.call();
		
	}	
	
	@Test
	public void givenDataValidCallSimularPolizaFrecuenciaWithoutErrorWhenCallSimularThenReturnObject() throws Exception {

		// given
		@SuppressWarnings("unchecked")
		final Map<String,Object> hmValores = mock(Map.class);
		final var datosAltaMock = mock(DatosAlta.class);
		@SuppressWarnings("unchecked")
		final List<ProductoPolizas> lProuductosMock = mock(List.class);
		final var frecuenciaMock = FrecuenciaEnum.ANUAL;
		@SuppressWarnings("unchecked")
		final List<BeneficiarioPolizas> lBeneficiariosMock = mock(List.class);

		final var tarificacionMock = new Tarificacion();
		final var tarifa = mock(Tarifas.class);
		tarificacionMock.setTarifas(tarifa);
		final var errorMock = mock(Error.class);
		final RESTResponse<Tarificacion, Error > responseREst = new RESTResponse<Tarificacion, Error >();
		responseREst.out = tarificacionMock;
		responseREst.error = errorMock;
		final var infoContratacionMock = mock(InfoContratacion.class);
		
		final var listBeneficiarioMock = Arrays.asList(mock(Beneficiario.class)).toArray(new Beneficiario[0]);
		
	    //when
        when(promotionInformationService.obtenerInfoPromociones( datosAltaMock )).thenReturn(mock(InfoPromociones.class));
        when(tierService.obtenerTier(datosAltaMock)).thenReturn(mock(InfoTier.class));
        when(beneficiariesService.obtenerBeneficiarios(any(), any(), any(), any())).thenReturn(listBeneficiarioMock);
        when(servicioSimulacion.simular(any())).thenReturn(responseREst);   
        when(contractingInformationService.obtenerInfoContratacion(any(), any(), any(), any(), any())).thenReturn(infoContratacionMock);
        
		var callable = solverServiceImpl.simularPolizaFrecuencia(hmValores, datosAltaMock, lProuductosMock, lBeneficiariosMock, frecuenciaMock);
		var response = callable.call();
		
		assertNotNull(response);
		assertEquals(response.getTarificacion(),tarificacionMock);
		assertNull(response.getCodigoError());
		
	}
	
	@Test(expected = ExcepcionContratacion.class)
	public void givenDataValidCallSimularPolizaFrecuenciaWithErrorWhenCallSimularThenReturnException() throws Exception {

		// given
		@SuppressWarnings("unchecked")
		final Map<String,Object> hmValores = mock(Map.class);
		final var datosAltaMock = mock(DatosAlta.class);
		@SuppressWarnings("unchecked")
		final List<ProductoPolizas> lProuductosMock = mock(List.class);
		final var frecuenciaMock = FrecuenciaEnum.ANUAL;
		@SuppressWarnings("unchecked")
		final List<BeneficiarioPolizas> lBeneficiariosMock = mock(List.class);


		final var errorMock = mock(Error.class);
		final RESTResponse<Tarificacion, Error > responseREst = new RESTResponse<Tarificacion, Error >();
		responseREst.error = errorMock;
		final var infoContratacionMock = mock(InfoContratacion.class);
		
		final var listBeneficiarioMock = Arrays.asList(mock(Beneficiario.class)).toArray(new Beneficiario[0]);
		
	    //when
		when(errorMock.getCodigo()).thenReturn(StaticVarsContratacion.SIMULACION_PROMOCIONES_AUTOMATICAS);
        when(promotionInformationService.obtenerInfoPromociones( datosAltaMock )).thenReturn(mock(InfoPromociones.class));
        when(tierService.obtenerTier(datosAltaMock)).thenReturn(mock(InfoTier.class));
        when(beneficiariesService.obtenerBeneficiarios(any(), any(), any(), any())).thenReturn(listBeneficiarioMock);
        when(servicioSimulacion.simular(any())).thenReturn(responseREst);   
        when(contractingInformationService.obtenerInfoContratacion(any(), any(), any(), any(), any())).thenReturn(infoContratacionMock);
        
		var callable = solverServiceImpl.simularPolizaFrecuencia(hmValores, datosAltaMock, lProuductosMock, lBeneficiariosMock, frecuenciaMock);
		callable.call();
		

		
	}	
	
}
