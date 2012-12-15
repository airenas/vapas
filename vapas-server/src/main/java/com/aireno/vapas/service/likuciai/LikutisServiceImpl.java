package com.aireno.vapas.service.likuciai;

import com.aireno.dto.LikutisListDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.vapas.service.LikutisService;
import com.aireno.vapas.service.SaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.*;
import com.aireno.vapas.service.saskaitos.SaskaitaDtoMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class LikutisServiceImpl extends ServiceBase implements LikutisService {
    LikutisDtoMap getMapper() {
        return new LikutisDtoMap();
    }

    @Override
    public List<LikutisListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<LikutisListDto>>() {
            @Override
            protected List<LikutisListDto> processInt(String[] request) throws Exception {
                List<LikutisList> result = getSession().createQuery("from LikutisList").list();
                LikutisDtoMap mapper = getMapper();
                List<LikutisListDto> res = new ArrayList<LikutisListDto>();
                for (LikutisList item : result) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(keywords);
    }




}
