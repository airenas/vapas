package com.aireno.vapas.service;

import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;

import java.util.List;

public interface PrekeService
{
    List<PrekeListDto> sarasas(String[] keywords) throws Exception;

    PrekeDto gauti(Long id) throws Exception;

    PrekeDto saugoti(PrekeDto updatedContact) throws Exception;
}
