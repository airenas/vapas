package com.aireno.vapas.service;

import com.aireno.base.LookupDto;
import com.aireno.dto.LookupItemDto;
import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymasListDto;
import com.aireno.dto.StringLookupItemDto;

import java.util.List;

public interface GydymoZurnalasService {
    List<LookupDto> gyvunoRusys(String[] keywords) throws Exception;

    NurasymasDto gauti(Long id) throws Exception;

    NurasymasDto saugoti(NurasymasDto updatedContact) throws Exception;

    Boolean tvirtinti(Long id) throws Exception;

    Boolean generuotiAtaskaita(long id) throws Exception;

    List<StringLookupItemDto> sarasasLaisvuPrekiuSeriju(LaisvuSerijuRequest req) throws Exception;

    LookupItemDto saugotiGyvunoRusi(LookupItemDto dto) throws Exception;

    LookupItemDto gautiGyvunoRusi(long id) throws Exception;

    public class LaisvuSerijuRequest {
        public long prekeId;
        public long imoneId;

        public LaisvuSerijuRequest(long prekeId, long imoneId) {
            this.prekeId = prekeId;
            this.imoneId = imoneId;
        }
    }
}
