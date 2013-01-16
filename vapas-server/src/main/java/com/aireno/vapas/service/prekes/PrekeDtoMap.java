package com.aireno.vapas.service.prekes;

import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.service.persistance.MatavimoVienetas;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.persistance.PrekeList;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class PrekeDtoMap {

    public PrekeDto toDto(Preke item) {
        PrekeDto dto = new  PrekeDto();
        dto.setId(item.getId());
        dto.setMatVienetasId(item.getMatVienetasId());
        dto.setPavadinimas(item.getPavadinimas());
        dto.setIslaukaMesai(item.getIslaukaMesai());
        dto.setIslaukaPienui(item.getIslaukaPienui());
        dto.setPastaba(item.getPastaba());
        return dto;
    }

    public void fromDto(Preke item, PrekeDto dto) {
        item.setMatVienetasId(dto.getMatVienetasId());
        item.setPavadinimas(dto.getPavadinimas());
        item.setIslaukaMesai(dto.getIslaukaMesai());
        item.setIslaukaPienui(dto.getIslaukaPienui());
        item.setPastaba(dto.getPastaba());
    }

    public PrekeListDto toListDto(PrekeList item) {
        PrekeListDto dto = new PrekeListDto();
        dto.setId(item.getId());
        dto.setMatVienetas(item.getMatVienetas());
        dto.setPavadinimas(item.getPavadinimas());
        dto.setIslaukaMesai(item.getIslaukaMesai());
        dto.setIslaukaPienui(item.getIslaukaPienui());
        return dto;
    }
}
