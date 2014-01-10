package com.aireno.vapas.service.likuciai;

import com.aireno.dto.LikutisListDto;
import com.aireno.vapas.service.LikutisService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.LikutisList;
import com.aireno.vapas.service.persistance.Preke;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

import java.math.BigDecimal;
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
    public List<LikutisListDto> sarasas(SarasasRequest request) throws Exception {
        return new ProcessorBase<SarasasRequest, List<LikutisListDto>>() {
            @Override
            protected List<LikutisListDto> processInt(final SarasasRequest request) throws Exception {
                List<LikutisList> result = getSession().createQuery("from LikutisList").list();
                LikutisDtoMap mapper = getMapper();
                List<LikutisListDto> res = new ArrayList<LikutisListDto>();

                Predicate<LikutisList> filter = new Predicate<LikutisList>() {
                    @Override
                    public boolean apply(LikutisList p) {
                        return !request.arRodytiTikTeigiamus
                                || p.getKiekis().compareTo(BigDecimal.ZERO) > 0;
                    }
                };

                ImmutableList<LikutisList> selectedResult = ImmutableList.copyOf(
                        Iterables.filter(result, filter));
                for (LikutisList item : selectedResult) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(request);
    }
}
