package com.aireno.vapas.service;

import com.aireno.dto.AtaskaitaListDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;

import java.util.Date;
import java.util.List;

public interface AtaskaitaService {
    List<AtaskaitaListDto> sarasas(String[] keywords) throws Exception;

    Boolean atidaryti(String filename) throws Exception;

    Boolean generuotiNurasymus(GeneruotiRequest request) throws Exception;

    class GeneruotiRequest {
        public Date date;
        public long imoneId;
        public String numeris;

        public GeneruotiRequest(Date date, long imoneId, String numeris) {
            this.date = date;
            this.imoneId = imoneId;
            this.numeris = numeris;
        }
    }
}
