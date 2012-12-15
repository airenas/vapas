package com.aireno.vapas.service.nurasymai;

import com.aireno.dto.*;
import com.aireno.vapas.service.NurasymasService;
import com.aireno.vapas.service.SaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.*;
import com.aireno.vapas.service.saskaitos.SaskaitaDtoMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

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
public class NurasymasServiceImpl extends ServiceBase implements NurasymasService {
    NurasymasDtoMap getMapper() {
        return new NurasymasDtoMap();
    }

    @Override
    public List<NurasymasListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<NurasymasListDto>>() {
            @Override
            protected List<NurasymasListDto> processInt(String[] request) throws Exception {
                List<NurasymasList> result = getSession().createQuery("from NurasymasList").list();
                NurasymasDtoMap mapper = getMapper();
                List<NurasymasListDto> res = new ArrayList<NurasymasListDto>();
                for (NurasymasList item : result) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public NurasymasDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, NurasymasDto>() {
            @Override
            protected NurasymasDto processInt(Long id) throws Exception {
                String queryString = "from Nurasymas c where c.id = ?1";
                List<Nurasymas> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                Nurasymas item = list.get(0);
                NurasymasDto result = getMapper().toDto(item);
                queryString = "from NurasymoPreke c where c.nurasymasId = ?1";
                List<NurasymoPreke> listP = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                for (NurasymoPreke itemP : listP) {
                    result.getPrekes().add(getMapper().toPrekeDto(itemP));
                }
                return result;
            }
        }.process(id);
    }

    @Override
    public NurasymasDto saugoti(NurasymasDto dto) throws Exception {
        return new ProcessorBase<NurasymasDto, NurasymasDto>() {
            @Override
            protected NurasymasDto processInt(NurasymasDto dto) throws Exception {

                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getNumeris(), "Nėra nurašymo numerio");
                getAssertor().isTrue(dto.getImoneId() > 0, "Nėra įmonės");
                getAssertor().isTrue(dto.getData() != null, "Nėra datos");
                Nurasymas item = new Nurasymas();
                if (dto.getId() > 0) {
                    String queryString = "from Nurasymas c where c.id = ?1";
                    List<Nurasymas> list = getSession().createQuery(queryString)
                            .setParameter("1", dto.getId()).list();
                    getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");

                    item = list.get(0);
                    getAssertor().isFalse(item.getStatusas() == SaskaitaStatusas.Patvirtinta, "Nurasymas jau patvirtintas. Negalima saugoti");
                }
                getMapper().fromDto(item, dto);
                getSession().save(item);
                // saugom prekes
                String queryString = "from NurasymoPreke c where c.nurasymasId = ?1";
                List<NurasymoPreke> listP = getSession().createQuery(queryString)
                        .setParameter("1", item.getId()).list();

                List<NurasymoPreke> newP = new ArrayList<NurasymoPreke>();
                List<NurasymoPreke> updatedP = new ArrayList<NurasymoPreke>();
                for (NurasymoPrekeDto itemPDto : dto.getPrekes()) {
                    NurasymoPreke itemP = raskPaimkPreke(listP, itemPDto);
                    if (itemP == null) {
                        itemP = new NurasymoPreke();
                    }
                    getAssertor().isNotNullStr(itemPDto.getSerija(), "Nėra serijos");
                    getAssertor().isTrue(itemPDto.getKiekis().doubleValue() > 0, "Nėra kiekio");
                    getAssertor().isTrue(itemPDto.getPrekeId() > 0, "Nėra prekės");

                    getMapper().fromPrekeDto(itemP, itemPDto, item);
                    if (itemP.getMatavimoVienetasId() == 0) {
                        MatavimoVienetas mv = gautiMatavimoVieneta(itemP.getPrekeId(), getSession());
                        itemP.setMatavimoVienetasId(mv.getId());
                        itemP.setMatavimoVienetas(mv.getKodas());
                    }

                    getSession().save(itemP);
                    newP.add(itemP);
                }
                for (NurasymoPreke itemP : listP) {
                    getSession().delete(itemP);
                }

                NurasymasDto result = getMapper().toDto(item);
                for (NurasymoPreke itemP : newP) {
                    result.getPrekes().add(getMapper().toPrekeDto(itemP));
                }
                return result;
            }
        }.process(dto);

    }

    private MatavimoVienetas gautiMatavimoVieneta(long prekeId, Session session) throws Exception {
        String queryString = "from Preke c where c.id = ?1";
        List<Preke> list = session.createQuery(queryString)
                .setParameter("1", prekeId).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        Preke item = list.get(0);

        queryString = "from MatavimoVienetas c where c.id = ?1";
        List<MatavimoVienetas> listMv = session.createQuery(queryString)
                .setParameter("1", item.getMatVienetasId()).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas matavimo vienetas pagal id " + item.getMatVienetasId() + " įrašas");
        MatavimoVienetas item1 = listMv.get(0);
        return item1;
    }

    private NurasymoPreke raskPaimkPreke(List<NurasymoPreke> listP, NurasymoPrekeDto itemPDto) {
        NurasymoPreke result = null;
        for (NurasymoPreke p : listP) {
            if (p.getId() == itemPDto.getId() || (p.getPrekeId() == itemPDto.getPrekeId()
                    && StringUtils.equals(p.getSerija(), itemPDto.getSerija()))
                    ) {
                result = p;
                break;
            }
        }
        if (result != null) {
            listP.remove(result);
        }
        return result;
    }


    @Override
    public Boolean tvirtinti(Long id) throws Exception {
        return new ProcessorBase<Long, Boolean>() {
            @Override
            protected Boolean processInt(Long id) throws Exception {
                Nurasymas item;
                getAssertor().isTrue(id > 0, "Nėra id");
                String queryString = "from Nurasymas c where c.id = ?1";
                List<Nurasymas> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");

                item = list.get(0);
                getAssertor().isFalse(item.getStatusas() == SaskaitaStatusas.Patvirtinta, "Nurašymas jau patvirtintas. Negalima saugoti");
                item.setStatusas(SaskaitaStatusas.Patvirtinta);
                getSession().save(item);

                // saugom prekes
                String queryStringP = "from NurasymoPreke c where c.nurasymasId = ?1";
                List<NurasymoPreke> listP = getSession().createQuery(queryStringP)
                        .setParameter("1", item.getId()).list();

                getAssertor().isTrue(listP.size() > 0, "Nėra prekių");

                for (NurasymoPreke itemP : listP) {
                    queryStringP = "from Likutis c where c.serija = ?1 and prekeid = ?2 and imoneId = ?3";
                    List<Likutis> listL = getSession().createQuery(queryStringP)
                            .setParameter("1", itemP.getSerija()).setParameter("2", itemP.getPrekeId())
                            .setParameter("3", item.getImoneId()).list();
                    double suma = 0;
                    for (Likutis itemL : listL)
                    {
                        suma += itemL.getKiekis().doubleValue();
                    }
                    getAssertor().isFalse(itemP.getKiekis().doubleValue() > suma, "Nėra pajamuota prekių su serija "
                            + itemP.getSerija() + ". Reikia " + itemP.getKiekis() + " yra " + suma);

                    Likutis l = new Likutis();
                    l.setArSaskaita(false);
                    l.setData(item.getData());
                    l.setDokumentas(item.getNumeris());
                    l.setIrasoId(itemP.getId());
                    l.setKiekis(new BigDecimal(0).subtract(itemP.getKiekis()));
                    l.setMatavimoVienetasId(itemP.getMatavimoVienetasId());
                    l.setPrekeId(itemP.getPrekeId());
                    l.setSerija(itemP.getSerija());
                    l.setImoneId(item.getImoneId());

                    getSession().save(l);
                }

                return true;
            }
        }.process(id);
    }
}
