package com.mycorp.service;



import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;

import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface SolverService {

    public Callable<TarificacionPoliza> simularPolizaFrecuencia(
            final Map<String, Object> hmValores, final DatosAlta oDatosAlta,
            final List<ProductoPolizas> lProductos,
            final List<BeneficiarioPolizas> lBeneficiarios, final FrecuenciaEnum frecuencia);
}
