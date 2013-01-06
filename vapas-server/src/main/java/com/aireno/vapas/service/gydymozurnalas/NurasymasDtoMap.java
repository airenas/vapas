package com.aireno.vapas.service.gydymozurnalas;

import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymasListDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.vapas.service.persistance.Nurasymas;
import com.aireno.vapas.service.persistance.NurasymasList;
import com.aireno.vapas.service.persistance.NurasymoPreke;
import com.aireno.vapas.service.persistance.SaskaitaStatusas;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class NurasymasDtoMap {

    public NurasymasDto toDto(Nurasymas item) {
        NurasymasDto dto = new NurasymasDto();
        dto.setId(item.getId());
        dto.setImoneId(item.getImoneId());
        dto.setNumeris(item.getNumeris());
        dto.setData(item.getData());
        if (item.getStatusasNotNull() == SaskaitaStatusas.Pildoma) {
            dto.setStatusas("Pildoma");
        } else {
            dto.setStatusas("Patvirtinta");
        }
        return dto;
    }

    public void fromDto(Nurasymas item, NurasymasDto dto) {
        item.setData(dto.getData());
        item.setImoneId(dto.getImoneId());
        item.setNumeris(dto.getNumeris());
        item.setData(dto.getData());
    }

    public NurasymasListDto toListDto(NurasymasList item) {
        NurasymasListDto dto = new NurasymasListDto();
        dto.setId(item.getId());

        dto.setNumeris(item.getNumeris());
        dto.setImone(item.getImone());
        if (item.getStatusasNotNull() == SaskaitaStatusas.Pildoma) {
            dto.setStatusas("Pildoma");
        } else {
            dto.setStatusas("Patvirtinta");
        }
        dto.setData(item.getData());
        return dto;
    }

    public NurasymoPrekeDto toPrekeDto(NurasymoPreke itemP) {
        NurasymoPrekeDto dto = new NurasymoPrekeDto();
        dto.setId(itemP.getId());
        dto.setSerija(itemP.getSerija());
        dto.setPrekeId(itemP.getPrekeId());
        dto.setKiekis(itemP.getKiekis());
        return dto;
    }

    public void fromPrekeDto(NurasymoPreke itemP, NurasymoPrekeDto itemPDto, Nurasymas item) {
        itemP.setId(itemP.getId());
        itemP.setSerija(itemPDto.getSerija());
        itemP.setPrekeId(itemPDto.getPrekeId());
        itemP.setKiekis(itemPDto.getKiekis());
        itemP.setNurasymasId(item.getId());
    }
}
