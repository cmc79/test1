package com.mycorp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycorp.domain.dto.BeneficiarioPolizas;
import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.domain.dto.ProductoPolizas;
import com.mycorp.domain.dto.RESTResponse;
import com.mycorp.domain.dto.TarificacionPoliza;
import com.mycorp.domain.dto.type.FrecuenciaEnum;
import com.mycorp.exception.ExcepcionContratacion;
import com.mycorp.service.BeneficiariesService;
import com.mycorp.service.ContractingInformation;
import com.mycorp.service.PromotionInformationService;
import com.mycorp.service.SimulacionWS;
import com.mycorp.service.SolverService;
import com.mycorp.service.TierService;
import com.mycorp.util.StaticVarsContratacion;

import es.sanitas.bravo.ws.stubs.contratacionws.consultasoperaciones.DatosContratacionPlan;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Error;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Simulacion;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.Tarificacion;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@Component
public class SolverServiceImpl implements SolverService {


    private static final String LINE_BREAK = "<br/>";

    private static final Logger LOG = LoggerFactory.getLogger( SolverServiceImpl.class );

    private SimulacionWS servicioSimulacion;

    @Autowired
    PromotionInformationService promotionInformationService;
    @Autowired
    TierService tierService;
    @Autowired
    BeneficiariesService beneficiarioService;
    @Autowired
    ContractingInformation informacionContratacionService;



    @Override
    public Callable<TarificacionPoliza> simularPolizaFrecuencia(
            final Map< String, Object > hmValores, final DatosAlta oDatosAlta,
            final List<ProductoPolizas> lProductos,
            final List<BeneficiarioPolizas> lBeneficiarios, final FrecuenciaEnum frecuencia ) {
        return new Callable<TarificacionPoliza>() {

            @Override
            public TarificacionPoliza call() throws ExcepcionContratacion {
                return simular( hmValores, oDatosAlta, lProductos, lBeneficiarios, frecuencia );
            }
        };
    }

    private TarificacionPoliza simular(final Map< String, Object > hmValores, final DatosAlta oDatosAlta,
                                       final List<ProductoPolizas> lProductos, final List<BeneficiarioPolizas> lBeneficiarios,
                                       final FrecuenciaEnum frecuencia ) throws ExcepcionContratacion {

        TarificacionPoliza resultado = null;
        final Simulacion in = new Simulacion();
        final DatosContratacionPlan oDatosPlan = ( DatosContratacionPlan )hmValores
                .get( StaticVarsContratacion.DATOS_PLAN );

        if( lBeneficiarios != null ) {
            in.setOperacion( StaticVarsContratacion.INCLUSION_BENEFICIARIO );
        } else {
            in.setOperacion( StaticVarsContratacion.ALTA_POLIZA );
        }
        in.setInfoPromociones( promotionInformationService.obtenerInfoPromociones( oDatosAlta ) );
        in.setInfoTier( tierService.obtenerTier( oDatosAlta ) );
        in.setListaBeneficiarios(
                beneficiarioService.obtenerBeneficiarios( oDatosAlta, lProductos, lBeneficiarios, oDatosPlan ) );
        in.setInfoContratacion(
                informacionContratacionService.obtenerInfoContratacion( oDatosAlta, lBeneficiarios, lProductos, frecuencia,
                        in.getOperacion()) );

        final RESTResponse<Tarificacion, Error > response = servicioSimulacion
                .simular( in );
        if( !response.hasError() && response.out.getTarifas() != null ) {
            resultado = new TarificacionPoliza();
            resultado.setTarificacion( response.out );

            // Si se ha introducido un código promocional no válido se repite la simulación sin el
            // código promocional
        } else if( response.hasError() && StaticVarsContratacion.SIMULACION_ERROR_COD_PROMOCIONAL
                .equalsIgnoreCase( response.error.getCodigo() ) ) {
            if( oDatosAlta instanceof DatosAltaAsegurados) {
                final DatosAltaAsegurados oDatosAltaAsegurados = (DatosAltaAsegurados)oDatosAlta;
                oDatosAltaAsegurados.setCodigoPromocional( null );
            }
            LOG.info( toMensaje( in, response.rawResponse ) );

            resultado = simular( hmValores, oDatosAlta, lProductos, lBeneficiarios, frecuencia );
            resultado.setCodigoError( StaticVarsContratacion.SIMULACION_ERROR_COD_PROMOCIONAL );
            return resultado;
        } else {
            System.err.println( toMensaje (in, response.rawResponse ) );
            throw new ExcepcionContratacion( response.error.getDescripcion() );
        }

        return resultado;
    }

    private String toMensaje( final Simulacion in, final String error ) {
        final StringBuffer sb = new StringBuffer();
        final ObjectMapper om = new ObjectMapper();
        try {
            sb.append( error );
            sb.append( LINE_BREAK );
            sb.append( LINE_BREAK );
            sb.append( om.writeValueAsString( in ) );
        } catch( final JsonProcessingException e ) {
            LOG.error( e.getMessage(), e );
        }
        return sb.toString();
    }


    /**
     * @return the servicioSimulacion
     */
    public SimulacionWS getServicioSimulacion() {
        return servicioSimulacion;
    }

    /**
     * @param servicioSimulacion the servicioSimulacion to set
     */
    public void setServicioSimulacion( final SimulacionWS servicioSimulacion ) {
        this.servicioSimulacion = servicioSimulacion;
    }
}
