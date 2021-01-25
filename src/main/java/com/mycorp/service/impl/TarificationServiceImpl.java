package com.mycorp.service.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.TarificationService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@Component
public class TarificationServiceImpl implements TarificationService {

	private static final Predicate<TarificacionPoliza> isNotNullTarification = object -> object != null
			&& object.getTarificacion() != null;
	
	public Set<FrecuenciaEnum> getFrequenciesRate(final DatosAlta oDatosAlta,
			final List<BeneficiarioPolizas> lBeneficiarios, final Map<String, Object> hmValores) {
		Set< FrecuenciaEnum > frecuenciasTarificar = EnumSet.noneOf( FrecuenciaEnum.class );
        if( hmValores.containsKey( StaticVarsContratacion.FREC_MENSUAL ) ) {
            frecuenciasTarificar.clear();
            frecuenciasTarificar.add( FrecuenciaEnum.MENSUAL );
        }
        if( lBeneficiarios != null ) {
            frecuenciasTarificar.clear();
            frecuenciasTarificar
                    .add( FrecuenciaEnum.obtenerFrecuencia( oDatosAlta.getGenFrecuenciaPago() ) );
        }
        if( frecuenciasTarificar.isEmpty() ) {
            frecuenciasTarificar = EnumSet.allOf( FrecuenciaEnum.class );
        }
		return frecuenciasTarificar;
	}

	
	public Tarificacion getPrincingPolicy(final List<String> errores,
			final List<TarificacionPoliza> resultadoSimulacionesList) throws ExcepcionContratacion {
		final TarificacionPoliza retornoPoliza = IterableUtils.find(resultadoSimulacionesList,
				isNotNullTarification);

		if (retornoPoliza == null) {
			throw new ExcepcionContratacion(
					"No se ha podido obtener un precio para el presupuesto. Por favor, inténtelo de nuevo más tarde.");
		}
		final Tarificacion retorno = retornoPoliza.getTarificacion();
		final String codigoError = retornoPoliza.getCodigoError();
		if (codigoError != null && !StringUtils.isEmpty(codigoError)) {
			errores.add(codigoError);
		}
		return retorno;
	}	
}
