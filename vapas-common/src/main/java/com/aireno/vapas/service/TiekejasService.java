package com.aireno.vapas.service;

import com.aireno.dto.TiekejasDto;

import java.util.List;

public interface TiekejasService
{
    List<TiekejasDto> sarasas(String[] keywords) throws Exception;

    TiekejasDto gauti(Long id) throws Exception;

    TiekejasDto saugoti(TiekejasDto updatedContact) throws Exception;
}
