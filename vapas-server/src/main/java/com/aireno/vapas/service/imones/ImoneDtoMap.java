package com.aireno.vapas.service.imones;

import com.aireno.dto.ImoneDto;
import com.aireno.vapas.service.persistance.Imone;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class ImoneDtoMap {

    public ImoneDto toDto(Imone item) {
        ImoneDto dto = new ImoneDto();
        dto.setId(item.getId());
        dto.setPavadinimas(item.getPavadinimas());
        dto.setAdresas(item.getAdresas());
        dto.setTelefonas(item.getTelefonas());
        dto.setPastaba(item.getPastaba());
        return dto;
    }

    public void fromDto(Imone item, ImoneDto dto) {
        item.setAdresas(dto.getAdresas());
        item.setPavadinimas(dto.getPavadinimas());
        item.setTelefonas(dto.getTelefonas());
        item.setPastaba(dto.getPastaba());
    }

}
