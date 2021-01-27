package com.mycorp.service;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoTier;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface TierService {

    InfoTier obtenerTier(final DatosAlta oDatosAlta);
}
