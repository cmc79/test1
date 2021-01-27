package com.mycorp.service;


import java.util.List;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.type.FrecuenciaEnum;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoContratacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface ContractingInformation {

    InfoContratacion obtenerInfoContratacion(final DatosAlta oDatosAlta, final List<BeneficiarioPolizas>
            lBeneficiarios,
                                             final List<ProductoPolizas> lProductos, final FrecuenciaEnum frecuencia,
                                             final Integer tipoOperacion);
}
