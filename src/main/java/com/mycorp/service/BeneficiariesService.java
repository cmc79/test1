package com.mycorp.service;


import java.util.List;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface BeneficiariesService {

    public es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Beneficiario[] obtenerBeneficiarios(
            final DatosAlta oDatosAlta, final List<ProductoPolizas> lProductos,
            final List<BeneficiarioPolizas> lBeneficiarios, final DatosContratacionPlan oDatosPlan);
}
