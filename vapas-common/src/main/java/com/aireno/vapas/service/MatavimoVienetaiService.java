package com.aireno.vapas.service;

import com.aireno.dto.MatavimoVienetasDto;

import java.util.List;

public interface MatavimoVienetaiService
{
    List<MatavimoVienetasDto> sarasas(String[] keywords);

    MatavimoVienetasDto gauti(Long id);

    MatavimoVienetasDto saugoti(MatavimoVienetasDto updatedContact);
}
