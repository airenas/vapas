package com.aireno.vapas.service.matavimoVienetai;

import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.vapas.service.persistance.MatavimoVienetas;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 20.05
 * To change this template use File | Settings | File Templates.
 */
public class MatavimoVienetasDtoMap {

    public MatavimoVienetasDto toDto(MatavimoVienetas item) {
        MatavimoVienetasDto dto = new  MatavimoVienetasDto();
        dto.setId(item.getId());
        dto.setKodas(item.getKodas());
        dto.setPavadinimas(item.getPavadinimas());
        return dto;
    }

    public void fromDto(MatavimoVienetas item, MatavimoVienetasDto dto) {
        item.setKodas(dto.getKodas());
        item.setPavadinimas(dto.getPavadinimas());
    }
}
