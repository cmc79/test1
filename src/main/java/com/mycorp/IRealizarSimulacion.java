package com.mycorp;

import java.util.List;
import java.util.Map;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.exception.ExcepcionContratacion;

import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface IRealizarSimulacion {

    public Map< String, Object > realizarSimulacion( final DatosAlta oDatosAlta,
            final List< ProductoPolizas > lProductos, final List< BeneficiarioPolizas > lBeneficiarios,
            final boolean desglosar, final Map< String, Object > hmValores )
            throws Exception, ExcepcionContratacion;
}
