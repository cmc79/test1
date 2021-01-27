package com.mycorp.service;


import java.util.List;

import com.mycorp.domain.dto.Recibo;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.ReciboProducto;

public interface PaymentsReceiptsService {

    List<Recibo> toReciboList(final ReciboProducto[] recibos);
}
