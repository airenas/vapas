package com.aireno.vapas.service.tiekejai;

import com.aireno.dto.PrekeDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.dto.TiekejasDto;
import com.aireno.vapas.service.PrekeService;
import com.aireno.vapas.service.TiekejasService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.persistance.PrekeList;
import com.aireno.vapas.service.persistance.Tiekejas;
import com.aireno.vapas.service.prekes.PrekeDtoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class TiekejasServiceImpl extends ServiceBase implements TiekejasService {

    TiekejasDtoMap getMapper()
    {
        return new TiekejasDtoMap();
    }

    @Override
    public List<TiekejasDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<TiekejasDto>>() {
           @Override
            protected List<TiekejasDto> processInt(String[] request) throws Exception {
               List<Tiekejas> result = getSession().createQuery("from Tiekejas").list();
               TiekejasDtoMap mapper = getMapper();
               List<TiekejasDto> res = new ArrayList<TiekejasDto>();
               for(Tiekejas item: result)
               {
                   res.add(mapper.toDto(item));
               }
               return res;
            }
        }.process(keywords);
    }

    @Override
    public TiekejasDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, TiekejasDto>() {
            @Override
            protected TiekejasDto processInt(Long id) throws Exception {
                String queryString = "from Tiekejas c where c.id = ?1";
                List<Tiekejas> list =  getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                Tiekejas item =  list.get(0);
                return getMapper().toDto(item);
            }
        }.process(id);
    }

    @Override
    public TiekejasDto saugoti(TiekejasDto dto) throws Exception {
        return new ProcessorBase<TiekejasDto, TiekejasDto>() {
            @Override
            protected TiekejasDto processInt(TiekejasDto dto) throws Exception {
                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getPavadinimas(), "Nėra pavadinimo");
                Tiekejas item = new Tiekejas();
                if (dto.getId() > 0){
                    String queryString = "from Tiekejas c where c.id = ?1";
                    List<Tiekejas> list =  getSession().createQuery(queryString)
                            .setParameter("1", dto.getId()).list();
                    getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                    item =  list.get(0);
                }
                getMapper().fromDto(item, dto);
                getSession().save(item);
                return getMapper().toDto(item);
            }
        }.process(dto);

    }
}
