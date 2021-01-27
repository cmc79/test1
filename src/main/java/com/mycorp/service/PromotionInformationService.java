package com.mycorp.service;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoPromociones;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

public interface PromotionInformationService {

    InfoPromociones obtenerInfoPromociones(final DatosAlta oDatosAlta);
    

}
