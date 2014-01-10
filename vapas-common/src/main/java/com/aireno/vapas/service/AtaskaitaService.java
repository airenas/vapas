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

    Boolean generuotiZurnala(GeneruotiRequest generuotiRequest) throws Exception;

    Boolean generuotiLikucius(GeneruotiRequest generuotiRequest) throws Exception;

    Boolean generuotiDabartiniusLikucius(GeneruotiDabLikuciaiRequest generuotiRequest)throws Exception;

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

    class GeneruotiDabLikuciaiRequest extends GeneruotiRequest {
        public boolean arTikTeigiami;

        public GeneruotiDabLikuciaiRequest(Date date, long imoneId, String numeris, boolean arTikTeigiami) {
            super(date, imoneId, numeris);
            this.arTikTeigiami = arTikTeigiami;
        }
    }
}
