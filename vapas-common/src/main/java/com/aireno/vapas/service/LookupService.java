package com.aireno.vapas.service;

import com.aireno.base.LookupDto;

import java.util.List;

public interface LookupService {
    List<LookupDto> sarasas(LookupRequest request) throws Exception;
    LookupDto gauti(String kodas, Long id) throws Exception;

    public class LookupRequest
    {

        private String kodas;
        private String[] keywords;

        public LookupRequest(String kodas) {
            this.kodas = kodas;
        }

        public String getKodas() {
            return kodas;
        }

        public void setKodas(String kodas) {
            this.kodas = kodas;
        }


        public String[] getKeywords() {
            return keywords;
        }

        public void setKeywords(String[] keywords) {
            this.keywords = keywords;
        }
    }
}
