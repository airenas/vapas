package com.aireno.vapas.service.gydymozurnalas;

import com.aireno.dto.*;
import com.aireno.vapas.service.persistance.*;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class GydomuGyvunuZurnalasDtoMap {

    public GydomuGyvunuZurnalasDto toDto(GydomuGyvunuZurnalas item) {
        GydomuGyvunuZurnalasDto dto = new GydomuGyvunuZurnalasDto();
        dto.setId(item.getId());
        dto.setImoneId(item.getImoneId());
        dto.setLaikytojas(item.getLaikytojas());
        dto.setDiagnoze(item.getDiagnoze());
        return dto;
    }

    public void fromDto(GydomuGyvunuZurnalas item, GydomuGyvunuZurnalasDto dto) {
        item.setLaikytojas(dto.getLaikytojas());
        item.setDiagnoze(dto.getDiagnoze());
        item.setRegistracijosData(dto.getRegistracijosData());
        item.setImoneId(dto.getImoneId());
    }

    public GydomuGyvunuZurnalasListDto toListDto(GydomuGyvunuZurnalasList item) {
        GydomuGyvunuZurnalasListDto dto = new GydomuGyvunuZurnalasListDto();
        dto.setId(item.getId());
        dto.setEilesNumeris(item.getId());
        dto.setVaistai(item.getGydymas());
        dto.setGyvunuSarasas(item.getGyvunuSarasas());
        dto.setImone(item.getImone());
        dto.setLaikytojas(item.getLaikytojas());
        dto.setRegistracijosData(item.getRegistracijosData());
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
