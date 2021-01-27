package com.mycorp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.DatosAseguradoInclusion;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.service.impl.BeneficiariesServiceImpl;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Producto;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;
import wscontratacion.contratacion.fuentes.parametros.DatosAsegurado;
import wscontratacion.contratacion.fuentes.parametros.DatosPersona;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiariesServiceImplTest {

	@Mock
	InsuredProductService productoAsegurado;
	@Mock
	ProductService productService;
	@InjectMocks
	BeneficiariesServiceImpl beneficiariesServiceImpl;

	@Test
	public void givenDataValidCallobtenerBeneficiariosWithListBeneficiarieReturnListBeneficiaries() {

		// given
		final var datosPersonalesMock = new DatosPersona();
		datosPersonalesMock.setFNacimiento("//");
		;
		datosPersonalesMock.setGenSexo(1);
		datosPersonalesMock.setIdProfesion(1);
		datosPersonalesMock.setNombre("nombre");

		final var beneficiariPolizasMock = new BeneficiarioPolizas();
		beneficiariPolizasMock.setDatosPersonales(datosPersonalesMock);

		final List<BeneficiarioPolizas> lBeneficiariosMock = Arrays.asList(beneficiariPolizasMock);

		final var datosContratacionPlanMock = mock(DatosContratacionPlan.class);

		final var datosAseguradoMock = new DatosAsegurado();
		datosAseguradoMock.setProductosContratados(Collections.emptyList());

		final var datosAltaMock = new DatosAlta();
		datosAltaMock.setFAlta("");
		datosAltaMock.setTitular(datosAseguradoMock);

		final List<ProductoPolizas> listProductosMock = Collections.emptyList();

		// when

		when(productoAsegurado.obtenerProductosAsegurado(any(), any())).thenReturn(new Producto[0]);

		// then
		var response = beneficiariesServiceImpl.obtenerBeneficiarios(datosAltaMock, listProductosMock,
				lBeneficiariosMock, datosContratacionPlanMock);

		assertNotNull(response);
		assertEquals(response.length, 1);
		assertEquals(response[0].getNombre(), "nombre");
	}

	@Test
	public void givenDataValidCallobtenerBeneficiariosNoListBeneficiarieReturnListBeneficiaries() {

		// given
		final var datosPersonalesMock = new DatosPersona();
		datosPersonalesMock.setFNacimiento("//");
		;
		datosPersonalesMock.setGenSexo(1);
		datosPersonalesMock.setIdProfesion(1);
		datosPersonalesMock.setNombre("nombre");

		final var beneficiariPolizasMock = new BeneficiarioPolizas();
		beneficiariPolizasMock.setDatosPersonales(datosPersonalesMock);

		final List<BeneficiarioPolizas> lBeneficiariosMock = Collections.emptyList();

		final var datosContratacionPlanMock = mock(DatosContratacionPlan.class);

		final var datosAseguradoMock = new DatosAseguradoInclusion();
		datosAseguradoMock.setSIdCliente(Long.valueOf("1"));
		datosAseguradoMock.setProductosContratados(Collections.emptyList());
		datosAseguradoMock.setDatosPersonales(datosPersonalesMock);

		final var datosAltaMock = new DatosAlta();
		datosAltaMock.setFAlta("");
		datosAltaMock.setTitular(datosAseguradoMock);
		datosAltaMock.setAsegurados(Arrays.asList(datosAseguradoMock));

		final List<ProductoPolizas> listProductosMock = Collections.emptyList();

		// when
		when(productoAsegurado.obtenerProductosAsegurado(any(), any())).thenReturn(new Producto[0]);

		// then
		var response = beneficiariesServiceImpl.obtenerBeneficiarios(datosAltaMock, listProductosMock,
				lBeneficiariosMock, datosContratacionPlanMock);

		assertNotNull(response);
		assertEquals(response.length, 2);
	}
}
