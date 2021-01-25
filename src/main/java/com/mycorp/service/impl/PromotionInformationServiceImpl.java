package com.mycorp.service.impl;



import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.service.PromotionInformationService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoPromociones;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Promocion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;


@Component
public class PromotionInformationServiceImpl implements PromotionInformationService {



    @Override
    public InfoPromociones obtenerInfoPromociones(final DatosAlta oDatosAlta ) {
        InfoPromociones infoPromociones = null;
        if( oDatosAlta instanceof DatosAltaAsegurados) {
            final DatosAltaAsegurados oDatosAltaAsegurados = (DatosAltaAsegurados)oDatosAlta;
            infoPromociones = new InfoPromociones();
            infoPromociones
                    .setAutomaticas( StaticVarsContratacion.SIMULACION_PROMOCIONES_AUTOMATICAS );
            // Si no se ha introducido un código promocional se debe enviar
            // de cero elementos
            Promocion[] promociones = new Promocion[ 0 ];
            final String codigoPromocion = oDatosAltaAsegurados.getCodigoPromocional();
            if( codigoPromocion != null ) {
                promociones = new Promocion[ 1 ];
                final Promocion promocion = new Promocion();
                promocion.setIdPromocion( codigoPromocion );
                promociones[ 0 ] = promocion;
            }
            infoPromociones.setListaPromociones( promociones );
        }
        return infoPromociones;
    }
}
