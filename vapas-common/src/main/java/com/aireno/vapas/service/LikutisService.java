package com.aireno.vapas.service;

import com.aireno.dto.LikutisListDto;
import com.aireno.dto.TiekejasDto;

import java.util.List;

public interface LikutisService
{
    List<LikutisListDto> sarasas(SarasasRequest request) throws Exception;
    public class SarasasRequest {
        public boolean arRodytiTikTeigiamus;
    }
}
