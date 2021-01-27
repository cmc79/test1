package com.mycorp.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.PrimasPorProducto;
import com.mycorp.domain.dto.PromocionAplicada;
import com.mycorp.domain.dto.Recibo;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.domain.dto.type.TipoPromocionEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.impl.SimulationServiceImpl;
import com.mycorp.service.impl.SolverServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.bravo.ws.stubs.contratacionws.documentacion.Primas;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promocion;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promociones;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaBeneficiario;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TarifaProducto;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarifas;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@RunWith(MockitoJUnitRunner.class)
public class SimulationServiceImplTest {



	@Mock
	TarificationService tarificationService;
	@Mock
	SimulationService simulationsService;
	@Mock
	PromotionService promotionsService;
	@Mock
	PaymentsReceiptsService paymentsReceiptsService;
	@Mock
	SolverServiceImpl solverServiceImpl;
	@InjectMocks
	SimulationServiceImpl simulationServiceImpl;

	@Test
	public void givenDataValidNoPromotionsCallAddDataSimulationThenReturnException() {

		// given
		final Map<String, Object> hmSimulacion = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		final List<Primas> primas = mock(List.class);
		final Double[] descuentosTotales = new Double[0];
		final Double[] pagoTotal = new Double[0];
		final Double[] precioConPromocion = new Double[0];

		@SuppressWarnings("unchecked")
		final List<List<PrimasPorProducto>> primasDesglosadas = mock(List.class);

		final List<List<PromocionAplicada>> promociones = new ArrayList<>(new ArrayList<>());


		@SuppressWarnings("unchecked")
		final List<List<Recibo>> recibos = mock(List.class);
		@SuppressWarnings("unchecked")
		final List<String> errores = mock(List.class);

		var response = simulationServiceImpl.addDataSimulation(true, hmSimulacion, primas, descuentosTotales, pagoTotal,
				precioConPromocion, primasDesglosadas, promociones, recibos, errores);

		assertNotNull(response);
		assertEquals(response.size(), 10);

	}

	@Test
	public void givenDataValidPromotionsCallAddDataSimulationThenReturnException() {

		// given
		final Map<String, Object> hmSimulacion = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		final List<Primas> primas = mock(List.class);
		final Double[] descuentosTotales = new Double[0];
		final Double[] pagoTotal = new Double[0];
		final Double[] precioConPromocion = new Double[0];

		@SuppressWarnings("unchecked")
		final List<List<PrimasPorProducto>> primasDesglosadas = mock(List.class);

		final var promocionAplicada = new PromocionAplicada();
		promocionAplicada.setTipoPromocion(TipoPromocionEnum.DESCUENTO_PORCENTAJE);
		final List<PromocionAplicada> promocionesMock = Arrays.asList(promocionAplicada);
		final List<List<PromocionAplicada>> promociones = new ArrayList<>(new ArrayList<>());
		promociones.add(promocionesMock);

		@SuppressWarnings("unchecked")
		final List<List<Recibo>> recibos = mock(List.class);
		@SuppressWarnings("unchecked")
		final List<String> errores = mock(List.class);

		// when

		var response = simulationServiceImpl.addDataSimulation(true, hmSimulacion, primas, descuentosTotales, pagoTotal,
				precioConPromocion, primasDesglosadas, promociones, recibos, errores);

		assertNotNull(response);
		assertEquals(response.size(), 11);

	}

	@Test
	public void givenDataValidPromotionsCallGetDataSimulationThenReturnList() throws ExcepcionContratacion {

		// given
		final var oDatosAlta = mock(DatosAlta.class);
		final var oDatosPlan = mock(DatosContratacionPlan.class);
		@SuppressWarnings("unchecked")
		final List<String> lExceptiones = mock(List.class);

		final Set<FrecuenciaEnum> frecuenciasTarificar = new TreeSet<FrecuenciaEnum>();
		frecuenciasTarificar.add(FrecuenciaEnum.MENSUAL);

		var tarifaProducto = new TarifaProducto();
		var lstTarifaProductos = Arrays.asList(tarifaProducto).toArray(new TarifaProducto[0]);

		var tarifaBeneficiario = new TarifaBeneficiario();
		tarifaBeneficiario.setListaTarifasProductos(lstTarifaProductos);

		var lstTarifaBeneficiarios = Arrays.asList(tarifaBeneficiario).toArray(new TarifaBeneficiario[0]);
		final var tarifas = new Tarifas();
		tarifas.setListaTarifaBeneficiarios(lstTarifaBeneficiarios);
		
		var promocion= new Promocion();
		var listaPromocionesPoliza = Arrays.asList(promocion).toArray(new Promocion[0]);
		var promociones = new Promociones();
		promociones.setListaPromocionesPoliza(listaPromocionesPoliza);
		
		final var tarificacion = new Tarificacion();
		tarificacion.setTarifas(tarifas);	
        tarificacion.setPromociones(promociones);
		@SuppressWarnings("unchecked")
		final List<TarificacionPoliza> resultadoSimulacionesList = mock(List.class);

		@SuppressWarnings("unchecked")
		final List<Primas> primas = mock(List.class);
		final Double[] descuentosTotales = new Double[0];
		final Double[] pagoTotal = new Double[0];
		final Double[] precioConPromocion = new Double[0];

		@SuppressWarnings("unchecked")
		final List<List<PrimasPorProducto>> primasDesglosadas = mock(List.class);

		final var promocionAplicada = new PromocionAplicada();
		promocionAplicada.setTipoPromocion(TipoPromocionEnum.DESCUENTO_PORCENTAJE);
		final List<PromocionAplicada> promocionesAplicada = Arrays.asList(promocionAplicada);
		final List<List<PromocionAplicada>> promocionesAplicadaList = new ArrayList<>(new ArrayList<>());
		promocionesAplicadaList.add(promocionesAplicada);

		@SuppressWarnings("unchecked")
		final List<List<Recibo>> recibos = mock(List.class);
		@SuppressWarnings("unchecked")
		final List<String> errores = mock(List.class);


       //When
		when(tarificationService.getPrincingPolicy(any(), any())).thenReturn(tarificacion);

		// then
		simulationServiceImpl.getDataSimulation(oDatosAlta, lExceptiones, oDatosPlan, primas, descuentosTotales,
				pagoTotal, precioConPromocion, primasDesglosadas, promocionesAplicadaList, recibos, errores, frecuenciasTarificar,
				resultadoSimulacionesList);
		
		assertNotNull(resultadoSimulacionesList);
		assertFalse(resultadoSimulacionesList.isEmpty());
		assertNotNull(recibos);
		assertFalse(recibos.isEmpty());
		assertNotNull(primasDesglosadas);
		assertFalse(primasDesglosadas.isEmpty());
		assertNotNull(primas);
		assertFalse(primas.isEmpty());
		assertNotNull(promociones);


	}
	
	@Test
	public void givenDataCallableEmptyCallGetResultadoSimulacionesThenReturnEmptyList() throws ExcepcionContratacion {
		
		final Collection<Callable<TarificacionPoliza>> solvers = Collections.emptyList();
		var response = simulationServiceImpl.getResultadoSimulaciones(solvers);
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}
	
	@Test
	public void givenDataValidPromotionsCallGetResultadoSimulacionesThenReturnEmptyList() throws ExcepcionContratacion {
		
//		final Map<String,Object> hmValores = mock(Map.class);
//		final var oDatosAlta = mock(DatosAlta.class);
//		final List<ProductoPolizas> lProductos = mock(List.class);
//		final var frecuencia = FrecuenciaEnum.ANUAL;
//		final List<BeneficiarioPolizas> lBeneficiarios = mock(List.class);
//		final var datosContratacionPlanMock = mock(DatosContratacionPlan.class);
//		final var tarificacionMock = new Tarificacion();
//		final var tarifa = mock(Tarifas.class);
//		tarificacionMock.setTarifas(tarifa);
//		final var errorMock = mock(Error.class);
//		final RESTResponse<Tarificacion, Error > responseREst = new RESTResponse();
//		responseREst.out = tarificacionMock;
//		responseREst.error = errorMock;
//		final var infoContratacionMock = mock(InfoContratacion.class);
		
//		final var listBeneficiarioMock = Arrays.asList(mock(Beneficiario.class)).toArray(new Beneficiario[0]);
		var solver = new Callable<TarificacionPoliza>() {

            @Override
            public TarificacionPoliza call() throws ExcepcionContratacion {
              return mock(TarificacionPoliza.class);
            }
        };
		final Collection<Callable<TarificacionPoliza>> solvers = Arrays.asList(solver);
		var response = simulationServiceImpl.getResultadoSimulaciones(solvers);
		assertNotNull(response);
		assertThat(response,hasSize(1));
	}

}
