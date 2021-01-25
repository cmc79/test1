package com.mycorp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.Recibo;
import com.mycorp.service.PaymentsReceiptsService;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.ReciboProducto;

@Component
public class PaymentsReceiptsServiceImpl implements PaymentsReceiptsService {




    private final SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

    @Override
    public List< Recibo> toReciboList(final ReciboProducto[] recibos ) {
        final List< Recibo > recibosList = new LinkedList< >();

        if( recibos != null ) {
            for( final ReciboProducto recibo : recibos ) {
                final Recibo reciboParam = toRecibo( recibo );
                if( reciboParam != null ) {
                    recibosList.add( reciboParam );
                }
            }
        }
        return recibosList;
    }

    /**
     * Popula un objeto ReciboProviderOutParam con la simulación de un recibo.
     *
     * @param recibo datos del recibo
     * @return objeto ReciboProviderOutParam con la simulación de un recibo.
     */
    private Recibo toRecibo( final ReciboProducto recibo ) {
        Recibo reciboParam = null;
        if( recibo != null ) {
            reciboParam = new Recibo();
            final Calendar fechaEmision = Calendar.getInstance();
            try {
                fechaEmision.setTime( sdf.parse( "25/12/2016" ) );
            } catch( final ParseException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            reciboParam.setFechaEmision( fechaEmision );
            reciboParam.setImporte( recibo.getIdProducto() * 1000. );
        }
        return reciboParam;
    }
}
