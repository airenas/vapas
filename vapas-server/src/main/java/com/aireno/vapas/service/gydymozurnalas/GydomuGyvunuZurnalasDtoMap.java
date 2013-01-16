package com.aireno.vapas.service.gydymozurnalas;

import com.aireno.dto.*;
import com.aireno.utils.ADateUtils;
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
        dto.setRegistracijosData(item.getRegistracijosData());
        dto.setIslaukaMesai(item.getIslaukaMesai());
        dto.setIslaukaPienui(item.getIslaukaPienui());
        return dto;
    }

    public void fromDto(GydomuGyvunuZurnalas item, GydomuGyvunuZurnalasDto dto) {
        item.setLaikytojas(dto.getLaikytojas());
        item.setDiagnoze(dto.getDiagnoze());
        item.setRegistracijosData(dto.getRegistracijosData());
        item.setImoneId(dto.getImoneId());
        item.setIslaukaMesai(dto.getIslaukaMesai());
        item.setIslaukaPienui(dto.getIslaukaPienui());
    }

    public GydomuGyvunuZurnalasListDto toListDto(GydomuGyvunuZurnalasList item) {
        GydomuGyvunuZurnalasListDto dto = new GydomuGyvunuZurnalasListDto();
        dto.setId(item.getId());
        dto.setEilesNumeris(item.getId());
        dto.setVaistai(item.getGydymas());
        dto.setGyvunuSarasas(item.getGyvunuSarasas());
        dto.setImone(item.getImone());
        dto.setLaikytojas(item.getLaikytojas());
        dto.setRegistracijosData(ADateUtils.dateToString(item.getRegistracijosData()));
        dto.setIslaukaMesai(ADateUtils.dateToString(item.getIslaukaMesai()));
        dto.setIslaukaPienui(ADateUtils.dateToString(item.getIslaukaPienui()));
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

    public ZurnaloGyvunasDto toGyvunasDto(ZurnaloGyvunas itemP) {
        ZurnaloGyvunasDto dto = new ZurnaloGyvunasDto();
        dto.setId(itemP.getId());
        dto.setNumeris(itemP.getNumeris());
        dto.setGyvunoRusisId(itemP.getGyvunoRusisId());
        dto.setAmzius(itemP.getAmzius());
        return dto;
    }

    public void fromGyvunasDto(ZurnaloGyvunas itemP, ZurnaloGyvunasDto itemPDto, GydomuGyvunuZurnalas item) {
        itemP.setId(itemP.getId());
        itemP.setNumeris(itemPDto.getNumeris());
        itemP.setGyvunoRusisId(itemPDto.getGyvunoRusisId());
        itemP.setAmzius(itemPDto.getAmzius());
        itemP.setZurnaloId(item.getId());
    }
}
