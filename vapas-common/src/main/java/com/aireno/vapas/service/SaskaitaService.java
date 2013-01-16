package com.aireno.vapas.service;

import com.aireno.dto.ImoneDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;

import java.util.List;

public interface SaskaitaService {
    List<SaskaitaListDto> sarasas(String[] keywords) throws Exception;

    SaskaitaDto gauti(Long id) throws Exception;

    SaskaitaDto saugoti(SaskaitaDto updatedContact) throws Exception;

    Boolean tvirtinti(Long id) throws Exception;

    List<String> sarasasSerijos(SerijosRequest req) throws Exception;

    public class SerijosRequest {
        public long prekeId;

        public SerijosRequest(long prekeId) {
            this.prekeId = prekeId;

        }
    }
}
