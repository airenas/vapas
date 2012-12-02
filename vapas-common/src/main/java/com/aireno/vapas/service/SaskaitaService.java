package com.aireno.vapas.service;

import com.aireno.dto.ImoneDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;

import java.util.List;

public interface SaskaitaService {
    List<SaskaitaListDto> sarasas(String[] keywords) throws Exception;

    SaskaitaDto gauti(Long id) throws Exception;

    SaskaitaDto saugoti(SaskaitaDto updatedContact) throws Exception;

    void tvirtinti(Long id) throws Exception;
}
