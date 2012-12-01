package com.aireno.vapas.service.tiekejai;

import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.dto.TiekejasDto;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.persistance.PrekeList;
import com.aireno.vapas.service.persistance.Tiekejas;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class TiekejasDtoMap {

    public TiekejasDto toDto(Tiekejas item) {
        TiekejasDto dto = new  TiekejasDto();
        dto.setId(item.getId());
        dto.setPavadinimas(item.getPavadinimas());
        dto.setAdresas(item.getAdresas());
        dto.setTelefonas(item.getTelefonas());
        dto.setPastaba(item.getPastaba());
        return dto;
    }

    public void fromDto(Tiekejas item, TiekejasDto dto) {
        item.setAdresas(dto.getAdresas());
        item.setPavadinimas(dto.getPavadinimas());
        item.setTelefonas(dto.getTelefonas());
        item.setPastaba(dto.getPastaba());
    }

}
