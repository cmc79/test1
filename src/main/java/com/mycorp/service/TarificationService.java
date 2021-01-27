package com.mycorp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface TarificationService {

	public Set<FrecuenciaEnum> getFrequenciesRate(final DatosAlta oDatosAlta,
			final List<BeneficiarioPolizas> lBeneficiarios, final Map<String, Object> hmValores);	
	
	public Tarificacion getPrincingPolicy(final List<String> errores,
			final List<TarificacionPoliza> resultadoSimulacionesList) throws ExcepcionContratacion; 
}
