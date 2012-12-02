package com.aireno.vapas.service.saskaitos;

import com.aireno.dto.PrekeDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.vapas.service.SaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.Saskaita;
import com.aireno.vapas.service.persistance.SaskaitaList;
import com.aireno.vapas.service.persistance.SaskaitosPreke;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class SaskaitaServiceImpl extends ServiceBase implements SaskaitaService {
    SaskaitaDtoMap getMapper() {
        return new SaskaitaDtoMap();
    }

    @Override
    public List<SaskaitaListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<SaskaitaListDto>>() {
            @Override
            protected List<SaskaitaListDto> processInt(String[] request) throws Exception {
                List<SaskaitaList> result = getSession().createQuery("from SaskaitaList").list();
                SaskaitaDtoMap mapper = getMapper();
                List<SaskaitaListDto> res = new ArrayList<SaskaitaListDto>();
                for (SaskaitaList item : result) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public SaskaitaDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, SaskaitaDto>() {
            @Override
            protected SaskaitaDto processInt(Long id) throws Exception {
                String queryString = "from Saskaita c where c.id = ?1";
                List<Saskaita> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                Saskaita item = list.get(0);
                SaskaitaDto result = getMapper().toDto(item);
                queryString = "from SaskaitosPreke c where c.saskaitaId = ?1";
                List<SaskaitosPreke> listP = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                for (SaskaitosPreke itemP : listP) {
                    result.getPrekes().add(getMapper().toPrekeDto(itemP));
                }
                return result;
            }
        }.process(id);
    }

    @Override
    public SaskaitaDto saugoti(SaskaitaDto dto) throws Exception {
        return new ProcessorBase<SaskaitaDto, SaskaitaDto>() {
            @Override
            protected SaskaitaDto processInt(SaskaitaDto dto) throws Exception {

                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getNumeris(), "Nėra sąsk. numerio");
                getAssertor().isTrue(dto.getTiekejasId() > 0, "Nėra tiekėjo");
                getAssertor().isTrue(dto.getImoneId() > 0, "Nėra įmonės");
                getAssertor().isTrue(dto.getData() != null, "Nėra datos");
                Saskaita item = new Saskaita();
                if (dto.getId() > 0) {
                    String queryString = "from Saskaita c where c.id = ?1";
                    List<Saskaita> list = getSession().createQuery(queryString)
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

    @Override
    public void tvirtinti(Long id) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
