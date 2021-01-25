package com.mycorp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mycorp.domain.dto.DatosAltaAsegurados;
import com.mycorp.service.TierService;

import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.InfoTier;
import es.sanitas.seg.simulacionpoliza.services.api.simulacion.vo.TierProducto;
import wscontratacion.contratacion.fuentes.parametros.DatosAlta;

@Component
public class TierServiceImpl implements TierService {

	private static final String SEPARADOR_TIER = "#";

	@Override
	public InfoTier obtenerTier(final DatosAlta oDatosAlta) {
		InfoTier infoTier = null;
		if (oDatosAlta instanceof DatosAltaAsegurados) {
			final DatosAltaAsegurados oDatosAltaAsegurados = (DatosAltaAsegurados) oDatosAlta;
			final String coeficientesTier = oDatosAltaAsegurados.getCoeficientesTier();
			if (!StringUtils.isEmpty(coeficientesTier)) {
				final List<String> productos = Arrays.asList("producto-1", "producto-5", "producto-3");
				final String[] st = coeficientesTier.split(SEPARADOR_TIER);

				infoTier = new InfoTier();
				final List<TierProducto> tierProductos = new ArrayList<>();
				int i = 1;
				for (final String idProducto : productos) {
					final TierProducto tier = new TierProducto();
					tier.setIdProducto(Integer.valueOf(idProducto));
					tier.setValor(Double.valueOf(st[i++]));
					tierProductos.add(tier);
				}

				infoTier.setListaTierProductos(tierProductos.toArray(new TierProducto[0]));
				infoTier.setTierGlobal(Double.valueOf(st[st.length - 1]).intValue());
			}
		}
		return infoTier;
	}
}
