package com.aireno.vapas.service;

import com.aireno.base.LookupDto;
import com.aireno.dto.*;

import java.util.List;

public interface GydomuGyvunuZurnalasService {
    List<LookupDto> gyvunoRusys(String[] keywords) throws Exception;

    GydomuGyvunuZurnalasDto gauti(Long id) throws Exception;

    GydomuGyvunuZurnalasDto saugoti(GydomuGyvunuZurnalasDto updatedContact) throws Exception;

    List<StringLookupItemDto> sarasasReceptai(ReceptaiRequest req) throws Exception;

    LookupItemDto saugotiGyvunoRusi(LookupItemDto dto) throws Exception;

    LookupItemDto gautiGyvunoRusi(long id) throws Exception;

    List<GydomuGyvunuZurnalasListDto> sarasas(String[] keywords) throws Exception;

    List<StringLookupItemDto> sarasasLaikytoju(String[] keywords) throws Exception;

    List<StringLookupItemDto> sarasasDiagnozes(String[] keywords) throws Exception;

    public class ReceptaiRequest {
        public long prekeId;

        public ReceptaiRequest(long prekeId) {
            this.prekeId = prekeId;

        }
    }
}
