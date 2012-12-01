package com.aireno.vapas.service.prekes;

import com.aireno.dto.*;
import com.aireno.vapas.service.PrekeService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.matavimoVienetai.MatavimoVienetasDtoMap;
import com.aireno.vapas.service.persistance.MatavimoVienetas;
import com.aireno.vapas.service.persistance.Preke;
import com.aireno.vapas.service.persistance.PrekeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class PrekeServiceImpl extends ServiceBase implements PrekeService {

    @Override
    public List<PrekeListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<PrekeListDto>>() {
           @Override
            protected List<PrekeListDto> processInt(String[] request) throws Exception {
               List<PrekeList> result = getSession().createQuery("from PrekeList").list();
               PrekeDtoMap mapper = new PrekeDtoMap();
               List<PrekeListDto> res = new ArrayList<PrekeListDto>();
               for(PrekeList item: result)
               {
                   res.add(mapper.toListDto(item));
               }
               return res;
            }
        }.process(keywords);
    }

    @Override
    public PrekeDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, PrekeDto>() {
            @Override
            protected PrekeDto processInt(Long id) throws Exception {
                PrekeDtoMap mapper = new PrekeDtoMap();
                String queryString = "from Preke c where c.id = ?1";
                List<Preke> list =  getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                Preke item =  list.get(0);
                return mapper.toDto(item);
            }
        }.process(id);
    }

    @Override
    public PrekeDto saugoti(PrekeDto dto) throws Exception {
        return new ProcessorBase<PrekeDto, PrekeDto>() {
            @Override
            protected PrekeDto processInt(PrekeDto dto) throws Exception {
                PrekeDtoMap mapper = new PrekeDtoMap();
                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getPavadinimas(), "Nėra pavadinimo");
                getAssertor().hasId(dto.getMatVienetasId(), "Nėra matavimo vieneto");
                Preke item = new Preke();
                if (dto.getId() > 0){
                    String queryString = "from Preke c where c.id = ?1";
                    List<Preke> list =  getSession().createQuery(queryString)
                            .setParameter("1", dto.getId()).list();
                    getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                    item =  list.get(0);
                }
                mapper.fromDto(item, dto);
                getSession().save(item);
                return mapper.toDto(item);
            }
        }.process(dto);

    }
}
