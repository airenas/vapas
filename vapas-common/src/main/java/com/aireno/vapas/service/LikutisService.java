package com.aireno.vapas.service;

import com.aireno.dto.LikutisListDto;
import com.aireno.dto.TiekejasDto;

import java.util.List;

public interface LikutisService
{
    List<LikutisListDto> sarasas(String[] keywords) throws Exception;

}
