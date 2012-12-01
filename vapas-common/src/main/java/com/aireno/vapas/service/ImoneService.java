package com.aireno.vapas.service;

import com.aireno.dto.ImoneDto;

import java.util.List;

public interface ImoneService {
    List<ImoneDto> sarasas(String[] keywords) throws Exception;

    ImoneDto gauti(Long id) throws Exception;

    ImoneDto saugoti(ImoneDto updatedContact) throws Exception;
}
