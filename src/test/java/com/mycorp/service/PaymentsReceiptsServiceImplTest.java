package com.mycorp.service;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycorp.service.impl.PaymentsReceiptsServiceImpl;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.ReciboProducto;

@RunWith(MockitoJUnitRunner.class)
public class PaymentsReceiptsServiceImplTest {

	@InjectMocks
	PaymentsReceiptsServiceImpl paymentsReceiptsServiceImpl;

	@Test
	public void givenDataValidCalltoReciboListReturnListPaymentsReceipts() {
		
		//given
		final ReciboProducto reciboProductoMock = new ReciboProducto();
		reciboProductoMock.setIdProducto(1);
			
		final List<ReciboProducto> listReciboProductoMock =  Arrays.asList(reciboProductoMock);
		final ReciboProducto[] reciboProductoArrayMock = listReciboProductoMock.toArray(new ReciboProducto[0]);

		
		//then
		var response = paymentsReceiptsServiceImpl.toReciboList(reciboProductoArrayMock);
	
		assertNotNull(response);
		assertThat(response,hasSize(1));

	}
	@Test
	public void givenParameterNullCalltoReciboListReturnListPaymentsReceiptsEmpty() {
		//then
		var response = paymentsReceiptsServiceImpl.toReciboList(null);
	
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

}
