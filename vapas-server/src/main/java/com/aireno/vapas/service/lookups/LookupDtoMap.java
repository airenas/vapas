package com.aireno.vapas.service.lookups;

import com.aireno.base.LookupDto;
import com.aireno.dto.LookupItemDto;
import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.service.persistance.LookupEntityDto;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.persistance.PrekeList;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class LookupDtoMap {

    public LookupDto toDto(LookupEntityDto item) {
        LookupDto dto = new LookupItemDto();
        dto.setId(item.getId());
        dto.setPavadinimas(item.getPavadinimas());
        return dto;
    }
}
