package com.aireno.vapas.service.saskaitos;

import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.vapas.service.persistance.Saskaita;
import com.aireno.vapas.service.persistance.SaskaitaList;
import com.aireno.vapas.service.persistance.SaskaitaStatusas;
import com.aireno.vapas.service.persistance.SaskaitosPreke;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class SaskaitaDtoMap {

    public SaskaitaDto toDto(Saskaita item) {
        SaskaitaDto dto = new SaskaitaDto();
        dto.setId(item.getId());
        dto.setImoneId(item.getImoneId());
        dto.setTiekejasId(item.getTiekejasId());
        dto.setArSaskaita(item.isArSaskaita());
        dto.setNumeris(item.getNumeris());
        dto.setData(item.getData());
        return dto;
    }

    public void fromDto(Saskaita item, SaskaitaDto dto) {
        item.setData(dto.getData());
        item.setImoneId(dto.getImoneId());
        item.setTiekejasId(dto.getTiekejasId());
        item.setArSaskaita(dto.isArSaskaita());
        item.setNumeris(dto.getNumeris());
        item.setData(dto.getData());
        if (item.getId() == 0)
            item.setStatusas(SaskaitaStatusas.Pildoma);
    }

    public SaskaitaListDto toListDto(SaskaitaList item) {
        SaskaitaListDto dto = new SaskaitaListDto();
        dto.setId(item.getId());

        dto.setNumeris(item.getNumeris());
        dto.setTiekejas(item.getTiekejas());
        dto.setImone(item.getImone());
        if (item.getStatusas() == SaskaitaStatusas.Pildoma) {
            dto.setStatusas("Pildoma");
        } else {
            dto.setStatusas("Patvirtinta");
        }
        dto.setData(item.getData());
        dto.setSuma(item.getSumaSuPvm());
        return dto;
    }

    public SaskaitosPrekeDto toPrekeDto(SaskaitosPreke itemP) {
        SaskaitosPrekeDto dto = new SaskaitosPrekeDto();
        dto.setId(itemP.getId());
        dto.setSerija(itemP.getSerija());
        dto.setPrekeId(itemP.getPrekeId());
        dto.setKiekis(itemP.getKiekis());
        return dto;
    }
}
