package com.aireno.vapas.service.likuciai;

import com.aireno.dto.LikutisListDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.service.persistance.LikutisList;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.18
 * Time: 02.08
 * To change this template use File | Settings | File Templates.
 */
public class LikutisDtoMap {

    public LikutisListDto toListDto(LikutisList item) {
        LikutisListDto dto = new LikutisListDto();
        dto.setId(item.getId());
        dto.setPajamuota(ANumberUtils.toGuiDecimal(item.getPajamuota()));
        dto.setPreke(item.getPreke());
        dto.setImone(item.getImone());
        dto.setData(ADateUtils.dateToString(item.getData()));
        dto.setKiekis(ANumberUtils.toGuiDecimal(item.getKiekis()));
        dto.setMatavimoVienetas(item.getMatavimoVienetas());
        dto.setNurasyta(item.getNurasyta());
        dto.setSerija(item.getSerija());
        return dto;
    }
}
