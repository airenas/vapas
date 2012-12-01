package com.aireno.vapas.service.imones;

import com.aireno.dto.ImoneDto;
import com.aireno.vapas.service.ImoneService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.Imone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class ImoneServiceImpl extends ServiceBase implements ImoneService {

    ImoneDtoMap getMapper() {
        return new ImoneDtoMap();
    }

    @Override
    public List<ImoneDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<ImoneDto>>() {
            @Override
            protected List<ImoneDto> processInt(String[] request) throws Exception {
                List<Imone> result = getSession().createQuery("from Imone").list();
                ImoneDtoMap mapper = getMapper();
                List<ImoneDto> res = new ArrayList<ImoneDto>();
                for (Imone item : result) {
                    res.add(mapper.toDto(item));
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public ImoneDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, ImoneDto>() {
            @Override
            protected ImoneDto processInt(Long id) throws Exception {
                String queryString = "from Imone c where c.id = ?1";
                List<Imone> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                Imone item = list.get(0);
                return getMapper().toDto(item);
            }
        }.process(id);
    }

    @Override
    public ImoneDto saugoti(ImoneDto dto) throws Exception {
        return new ProcessorBase<ImoneDto, ImoneDto>() {
            @Override
            protected ImoneDto processInt(ImoneDto dto) throws Exception {
                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getPavadinimas(), "Nėra pavadinimo");
                Imone item = new Imone();
                if (dto.getId() > 0) {
                    String queryString = "from Imone c where c.id = ?1";
                    List<Imone> list = getSession().createQuery(queryString)
                            .setParameter("1", dto.getId()).list();
                    getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                    item = list.get(0);
                }
                getMapper().fromDto(item, dto);
                getSession().save(item);
                return getMapper().toDto(item);
            }
        }.process(dto);

    }
}
