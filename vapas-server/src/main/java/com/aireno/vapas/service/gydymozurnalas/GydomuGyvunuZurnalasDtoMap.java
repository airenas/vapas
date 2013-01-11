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

    public ZurnaloVaistasDto toPrekeDto(ZurnaloVaistas itemP) {
        ZurnaloVaistasDto dto = new ZurnaloVaistasDto();
        dto.setId(itemP.getId());
        dto.setReceptas(itemP.getReceptas());
        dto.setPrekeId(itemP.getPrekeId());
        dto.setKiekis(itemP.getKiekis());
        return dto;
    }

    public void fromPrekeDto(ZurnaloVaistas itemP, ZurnaloVaistasDto itemPDto, GydomuGyvunuZurnalas item) {
        itemP.setId(itemP.getId());
        itemP.setReceptas(itemPDto.getReceptas());
        itemP.setPrekeId(itemPDto.getPrekeId());
        itemP.setKiekis(itemPDto.getKiekis());
        itemP.setZurnaloId(item.getId());
    }
}