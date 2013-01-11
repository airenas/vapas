package com.aireno.vapas.service.saskaitos;

import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.service.SaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.Repository;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.*;

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

                Collections.sort(result, new Comparator<SaskaitaList>() {
                    @Override
                    public int compare(SaskaitaList o1, SaskaitaList o2) {
                        return o2.getData().compareTo(o1.getData());
                    }
                });

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
                Saskaita item = getRepo().get(Saskaita.class, id);
                SaskaitaDto result = getMapper().toDto(item);

                List<SaskaitosPreke> listP = getRepo().getList(SaskaitosPreke.class, "saskaitaId", id);
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
                //getAssertor().isTrue(dto.getTiekejasId() > 0, "Nėra tiekėjo");
                getAssertor().isTrue(dto.getImoneId() > 0, "Nėra įmonės");
                getAssertor().isTrue(dto.getData() != null, "Nėra datos");
                getAssertor().isTrue(validateUnique("from Saskaita c where c.id != ?1 and numeris = ?2",
                        dto.getNumeris(), dto.getId(), getSession()), "Tokia sąskaita '%s' jau yra", dto.getNumeris());

                Saskaita item = new Saskaita();
                if (dto.getId() > 0) {
                    item = getRepo().get(Saskaita.class, dto.getId());
//                    getAssertor().isFalse(item.getStatusas() == SaskaitaStatusas.Patvirtinta, "Sąskaita jau patvirtinta. Negalima saugoti");
                }
                getMapper().fromDto(item, dto);
                getSession().save(item);

                // saugom prekes
                List<SaskaitosPreke> listP = getRepo().getList(SaskaitosPreke.class, "saskaitaId", dto.getId());
                List<SaskaitosPreke> newP = new ArrayList<SaskaitosPreke>();
                List<SaskaitosPreke> updatedP = new ArrayList<SaskaitosPreke>();
                for (SaskaitosPrekeDto itemPDto : dto.getPrekes()) {
                    SaskaitosPreke itemP = raskPaimkPreke(listP, itemPDto);
                    if (itemP == null) {
                        itemP = new SaskaitosPreke();
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
                    // saugom likucius
                    issaugotiLikucius(item, itemP, getRepo());
                }
                for (SaskaitosPreke itemP : listP) {
                    istrintiLikucius(item, itemP, getRepo());
                    getSession().delete(itemP);
                }

                SaskaitaDto result = getMapper().toDto(item);
                for (SaskaitosPreke itemP : newP) {
                    result.getPrekes().add(getMapper().toPrekeDto(itemP));
                }
                // tvarkom likucius


                return result;
            }
        }.process(dto);

    }


    private void issaugotiLikucius(Saskaita item, SaskaitosPreke itemP, Repository repo) throws Exception {

        List<Likutis> likuciai = repo.prepareQuery(Likutis.class, "saskaitosPrekesId = ?1")
                .setParameter("1", itemP.getId()).list();
        Likutis likutis;
        if (likuciai.size() == 0) {
            likutis = new Likutis();
        } else {
            likutis = likuciai.get(0);
        }

        // jei maziau, patikrinti ar nera daug isnaudota
        double dabartinisKiekis = itemP.getKiekis().doubleValue();
        double kiekis = ANumberUtils.DefaultValue(likutis.getKiekis());

        if (kiekis > dabartinisKiekis)
        {
            List<Likutis> likuciaiPanaudota = repo.getList(Likutis.class, "pirminisId", likutis.getId());
            double panaudota = 0;
            for (Likutis itemL: likuciaiPanaudota)
            {
                panaudota += (-itemL.getKiekis().doubleValue());
            }
            getAssertor().isTrue(panaudota < dabartinisKiekis, "Negalima išsaugoti prekės '%s' kiekio pakeitimo į '%s', nes jau panaudota '%s'",
                    gautiPrekesPavadinima(itemP.getPrekeId(), repo),
                    ANumberUtils.DecimalToString(dabartinisKiekis), ANumberUtils.DecimalToString(panaudota));
        }

        // kuriame likucio irasa
        likutis.setData(item.getData());
        likutis.setImoneId(item.getImoneId());
        likutis.setPrekeId(itemP.getPrekeId());
        likutis.setArSaskaita(true);
        likutis.setKiekis(new BigDecimal(dabartinisKiekis));
        likutis.setSaskaitosId(item.getId());
        likutis.setSaskaitosPrekesId(itemP.getId());
        likutis.setDokumentas(item.getNumeris());
        repo.getSession().save(likutis);
    }


    private void istrintiLikucius(Saskaita item, SaskaitosPreke itemP, Repository repo) throws Exception {

        List<Likutis> likuciai = repo.prepareQuery(Likutis.class, "saskaitosPrekesId = ?1")
                .setParameter("1", itemP.getId()).list();
        if (likuciai.size() == 0) {
            return;
        }

        Likutis likutis = likuciai.get(0);

        // jei maziau, patikrinti ar nera isnaudota
        List<Likutis> likuciaiPanaudota = repo.getList(Likutis.class, "pirminisId", likutis.getId());
        getAssertor().isTrue(likuciaiPanaudota.size() == 0, "Negalima ištrinti sąskaitos prekės '%s', ji jau panaudota nurašymuose", gautiPrekesPavadinima(itemP.getPrekeId(), repo));
        repo.delete(Likutis.class, likutis.getId());
    }

    private String gautiPrekesPavadinima(long prekeId, Repository repo) throws Exception {
        return repo.get(Preke.class, prekeId).getPavadinimas();
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
        getAssertor().isTrue(list.size() == 1, "Nerastas matavimo vienatas pagal id " + item.getMatVienetasId() + " įrašas");
        MatavimoVienetas item1 = listMv.get(0);
        return item1;
    }

    private SaskaitosPreke raskPaimkPreke(List<SaskaitosPreke> listP, SaskaitosPrekeDto itemPDto) {
        SaskaitosPreke result = null;
        for (SaskaitosPreke p : listP) {
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
                Saskaita item;
                getAssertor().isTrue(id > 0, "Nėra id");
                String queryString = "from Saskaita c where c.id = ?1";
                List<Saskaita> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");

                item = list.get(0);
                getAssertor().isFalse(item.getStatusas() == SaskaitaStatusas.Patvirtinta, "Sąskaita jau patvirtinta. Negalima saugoti");
                item.setStatusas(SaskaitaStatusas.Patvirtinta);
                getSession().save(item);

                // saugom prekes
                String queryStringP = "from SaskaitosPreke c where c.saskaitaId = ?1";
                List<SaskaitosPreke> listP = getSession().createQuery(queryStringP)
                        .setParameter("1", item.getId()).list();

                getAssertor().isTrue(listP.size() > 0, "Nėra prekių");
                for (SaskaitosPreke itemP : listP) {
                    Likutis l = new Likutis();
                    l.setArSaskaita(true);
                    l.setData(item.getData());
                    l.setDokumentas(item.getNumeris());
                    l.setGaliojaIki(itemP.getGaliojaIki());
                    //l.setIrasoId(itemP.getId());
                    l.setKiekis(itemP.getKiekis());
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
