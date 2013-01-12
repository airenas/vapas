package com.aireno.vapas.service.lookups;

import com.aireno.Constants;
import com.aireno.base.LookupDto;
import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.PrekeService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class LookupServiceImpl extends ServiceBase implements LookupService {
    @Override
    public List<LookupDto> sarasas(LookupRequest request) throws Exception {
        return new ProcessorBase<LookupRequest, List<LookupDto>>() {
            @Override
            protected List<LookupDto> processInt(LookupRequest request) throws Exception {
                List<LookupDto> result;
                if (StringUtils.equals(request.getKodas(), Constants.LOOKUP_IMONE))
                {
                    result = getLookups(getSession(), Imone.class);
                }
                else if (StringUtils.equals(request.getKodas(), Constants.LOOKUP_TIEKEJAS))
                {
                    result = getLookups(getSession(), Tiekejas.class);
                }
                else if (StringUtils.equals(request.getKodas(), Constants.LOOKUP_PREKE))
                {
                    result = getLookups(getSession(), Preke.class);
                }
                else if (StringUtils.equals(request.getKodas(), Constants.LOOKUP_GYVUNO_RUSIS))
                {
                    result = getLookups(getSession(), GyvunoRusis.class);
                }
                else
                 throw getAssertor().newException("Nežinomas lookup tipas '%s'", request.getKodas());

                return result;
            }
        }.process(request);
    }

    private <T extends LookupEntityDto> List<LookupDto> getLookups(Session session, Class<T> tClass) {
        List<LookupDto> result = new ArrayList<LookupDto>();
        List<T> list =session.createQuery("from " + tClass.getSimpleName()).list();
        LookupDtoMap mapper = new LookupDtoMap();
        for (T item: list)
        {
            result.add(mapper.toDto(item));
        }
        return result;
    }

    @Override
    public LookupDto gauti(String kodas, Long id) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
