package com.mycorp.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.DatosAseguradoInclusion;
import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.service.impl.PromotionServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosPlanProducto;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promocion;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaDesglosada;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaProducto;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@RunWith(MockitoJUnitRunner.class)
public class PromotionServiceImplTest {

	@InjectMocks
	PromotionServiceImpl promotionServiceImpl;

	@Test
	public void givenNoPromocionesCallObtenerInfoPromocionesReturnListBeneficiaries() {

		// given
		final var datosPromociones = Collections.emptyList().toArray(new Promocion[0]);

		// then
		var response = promotionServiceImpl.recuperarPromocionesAgrupadas(datosPromociones, 1);

		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

	@Test
	public void givenPromocionesCallObtenerInfoPromocionesReturnListBeneficiaries() {

		// given
		final var promocionMock = new Promocion();
		promocionMock.setTipo(1);
		promocionMock.setDescripcion("");
		final var listPromocionesMock = Arrays.asList(promocionMock).toArray(new Promocion[0]);

		// then
		var response = promotionServiceImpl.recuperarPromocionesAgrupadas(listPromocionesMock, 1);

		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertThat(response, hasSize(1));

	}

	@Test
	public void givenInvalidCalculatePricePromotionReturnDouble() {

		// given

		final TarifaProducto tarifaProductoMock = new TarifaProducto();
		tarifaProductoMock.setIdProducto(Long.valueOf("389"));
		final String PROMO_ECI_COLECTIVOS = "46";
		final String PROMO_FARMACIA = "49";
		final List<String> lExcepcionesMock = Arrays.asList(PROMO_ECI_COLECTIVOS, PROMO_FARMACIA);
		final Double cssMock = Double.valueOf("0");
		final DatosAlta datosAltaMock = mock(DatosAlta.class);
		final DatosContratacionPlan datosContratacionPlanMock = mock(DatosContratacionPlan.class);
		var descuentosMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		var pagoTotalMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		final List<PrimasPorProducto> listaProductosPorAseg = Collections.emptyList();

		// then
		var response = promotionServiceImpl.calculatePricePromotion(datosAltaMock, lExcepcionesMock,
				datosContratacionPlanMock, descuentosMock, pagoTotalMock, FrecuenciaEnum.MENSUAL, cssMock,
				listaProductosPorAseg, 1, tarifaProductoMock);

		assertNotNull(response);
	    assertEquals(Double.valueOf(response), cssMock);

	}

	@Test
	public void givenValidCalculatePricePromotionWithProductBonusReturnDouble() {

		// given

		var tarifaDesglosadaMock = new TarifaDesglosada();
		tarifaDesglosadaMock.setCss(Double.valueOf("0"));
		tarifaDesglosadaMock.setISPrima(Double.valueOf("0"));
		tarifaDesglosadaMock.setPrima(Double.valueOf("0"));
		tarifaDesglosadaMock.setDescuento(Double.valueOf("0"));
		
		final TarifaProducto tarifaProductoMock = new TarifaProducto();
		tarifaProductoMock.setIdProducto(Long.valueOf("400"));
		tarifaProductoMock.setTarifaDesglosada(tarifaDesglosadaMock);
		
		final String PROMO_ECI_COLECTIVOS = "46";
		final String PROMO_FARMACIA = "49";
		final List<String> lExcepcionesMock = Arrays.asList(PROMO_ECI_COLECTIVOS, PROMO_FARMACIA);
		
		final Double cssMock = Double.valueOf("0");
		
		final DatosAlta datosAltaMock = mock(DatosAlta.class);
		
		final DatosContratacionPlan datosContratacionPlanMock = mock(DatosContratacionPlan.class);
		
		var descuentosMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		var pagoTotalMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		var primasPorProductoMock = new PrimasPorProducto();
		final List<PrimasPorProducto> listaProductosPorAseg = Arrays.asList(primasPorProductoMock);
		// then
		var response = promotionServiceImpl.calculatePricePromotion(datosAltaMock, lExcepcionesMock,
				datosContratacionPlanMock, descuentosMock, pagoTotalMock, FrecuenciaEnum.MENSUAL, cssMock,
				listaProductosPorAseg, 0, tarifaProductoMock);

		assertNotNull(response);
	    assertEquals(Double.valueOf(response), cssMock);

	}
	@Test
	public void givenValidCalculatePricePromotionNoProductBonusReturnDouble() {

		// given

		var tarifaDesglosadaMock = new TarifaDesglosada();
		tarifaDesglosadaMock.setCss(Double.valueOf("0"));
		tarifaDesglosadaMock.setISPrima(Double.valueOf("0"));
		tarifaDesglosadaMock.setPrima(Double.valueOf("0"));
		tarifaDesglosadaMock.setDescuento(Double.valueOf("0"));
		
		final TarifaProducto tarifaProductoMock = new TarifaProducto();
		tarifaProductoMock.setIdProducto(Long.valueOf("389"));
		tarifaProductoMock.setTarifaDesglosada(tarifaDesglosadaMock);
		
		final String PROMO_ECI_COLECTIVOS = "46";
		final String PROMO_FARMACIA = "49";
		final List<String> lExcepcionesMock = Arrays.asList(PROMO_ECI_COLECTIVOS, PROMO_FARMACIA);
		
		final Double cssMock = Double.valueOf("0");
		
		final DatosAlta datosAltaMock = mock(DatosAlta.class);
		
		final var datosAseguradoInclusionMock = new DatosAseguradoInclusion();
		datosAseguradoInclusionMock.setSwPolizaAnterior("S");
		final var listAseguradoInclusion = Arrays.asList(datosAseguradoInclusionMock);
		
		final DatosContratacionPlan datosContratacionPlanMock = mock(DatosContratacionPlan.class);
        final var datosPlanProducto = new DatosPlanProducto();
        datosPlanProducto.setIdProducto(Long.valueOf(Long.valueOf("389")));
        datosPlanProducto.setSwObligatorio(true);
        datosPlanProducto.setDescComercial("descuento");
		final var listDatosPlanProducto = Arrays.asList(datosPlanProducto);
		
		var descuentosMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		var pagoTotalMock = Arrays.asList(Double.valueOf("0")).toArray(new Double[0]);
		//var primasPorProductoMock = new PrimasPorProducto();
		final List<PrimasPorProducto> listaProductosPorAseg = new ArrayList<>();
		//when
		when(datosAltaMock.getAsegurados()).thenReturn(listAseguradoInclusion);
		when( datosContratacionPlanMock.getProductos()).thenReturn(listDatosPlanProducto);
		// then
		var response = promotionServiceImpl.calculatePricePromotion(datosAltaMock, lExcepcionesMock,
				datosContratacionPlanMock, descuentosMock, pagoTotalMock, FrecuenciaEnum.MENSUAL, cssMock,
				listaProductosPorAseg, 2, tarifaProductoMock);

		assertNotNull(response);
	    assertEquals(Double.valueOf(response), cssMock);

	}

}
