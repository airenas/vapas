package com.aireno.vapas.service;

import com.aireno.base.LookupDto;
import com.aireno.dto.*;

import java.util.List;

public interface GydomuGyvunuZurnalasService {
    List<LookupDto> gyvunoRusys(String[] keywords) throws Exception;

    GydomuGyvunuZurnalasDto gauti(Long id) throws Exception;

    GydomuGyvunuZurnalasDto saugoti(GydomuGyvunuZurnalasDto updatedContact) throws Exception;

    List<String> sarasasReceptai(ReceptaiRequest req) throws Exception;

    List<String> sarasasNumeriai(NumeriaiRequest req) throws Exception;

    List<String> sarasasAmzius(AmziusRequest req) throws Exception;

    LookupItemDto saugotiGyvunoRusi(LookupItemDto dto) throws Exception;

    LookupItemDto gautiGyvunoRusi(long id) throws Exception;

    List<GydomuGyvunuZurnalasListDto> sarasas(String[] keywords) throws Exception;

    List<String> sarasasLaikytoju(String[] keywords) throws Exception;

    List<String> sarasasDiagnozes(String[] keywords) throws Exception;

    IslaukaResponse gautiIslauka(List<ZurnaloVaistasDto> lDto) throws Exception;

    public class ReceptaiRequest {
        public long prekeId;

        public ReceptaiRequest(long prekeId) {
            this.prekeId = prekeId;

        }
    }

    public class IslaukaResponse {
        public Long mesai;
        public Long pienui;

        public IslaukaResponse(Long mesai, Long pienui) {
            this.mesai = mesai;
            this.pienui = pienui;
        }
    }

    public class AmziusRequest {
        public long gyvunoRusisId;

        public AmziusRequest(long gyvunoRusisId) {
            this.gyvunoRusisId = gyvunoRusisId;

        }
    }

    public class NumeriaiRequest {
        public String laikytojas;
        public long gyvunoRusisId;

        public NumeriaiRequest(String laikytojas, Long gyvunoRusisId) {
            this.laikytojas = laikytojas;
            this.gyvunoRusisId = gyvunoRusisId;
        }


    }
}
