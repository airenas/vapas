package com.aireno.vapas.service.gydymozurnalas;

import com.aireno.base.LookupDto;
import com.aireno.dto.*;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.ataskaitos.AtaskaitaServiceImpl;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.Repository;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.persistance.*;
import org.apache.commons.lang.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.hibernate.Session;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class GydomuGyvunuZurnalasServiceImpl extends ServiceBase implements GydomuGyvunuZurnalasService {
    @Override
    public List<GydomuGyvunuZurnalasListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<GydomuGyvunuZurnalasListDto>>() {
            @Override
            protected List<GydomuGyvunuZurnalasListDto> processInt(String[] request) throws Exception {
                List<GydomuGyvunuZurnalasList> result = getSession().createQuery("from GydomuGyvunuZurnalasList").list();
                Collections.sort(result, new Comparator<GydomuGyvunuZurnalasList>() {
                    @Override
                    public int compare(GydomuGyvunuZurnalasList o1, GydomuGyvunuZurnalasList o2) {
                        return o2.getRegistracijosData().compareTo(o1.getRegistracijosData());
                    }
                });
                GydomuGyvunuZurnalasDtoMap mapper = getMapper();
                List<GydomuGyvunuZurnalasListDto> res = new ArrayList<GydomuGyvunuZurnalasListDto>();
                for (GydomuGyvunuZurnalasList item : result) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public List<String> sarasasLaikytoju(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<String>>() {
            @Override
            protected List<String> processInt(String[] request) throws Exception {
                List<String> result = getSession().createQuery("select distinct laikytojas from GydomuGyvunuZurnalas").
                        list();
                Collections.sort(result);
                GydomuGyvunuZurnalasDtoMap mapper = getMapper();
                List<String> res = new ArrayList<String>();
                for (String item : result) {
                    String dto = new String(item);
                    //dto.setPavadinimas(item);
                    res.add(dto);
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public List<String> sarasasDiagnozes(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<String>>() {
            @Override
            protected List<String> processInt(String[] request) throws Exception {
                List<String> result = getSession().createQuery("select distinct diagnoze from GydomuGyvunuZurnalas").
                        list();
                Collections.sort(result);

                List<String> res = new ArrayList<String>();
                for (String item : result) {
                    String dto = new String(item);

                    res.add(dto);
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public IslaukaResponse gautiIslauka(List<ZurnaloVaistasDto> request) throws Exception {
        return new ProcessorBase<List<ZurnaloVaistasDto>, IslaukaResponse>() {
            @Override
            protected IslaukaResponse processInt(List<ZurnaloVaistasDto> request) throws Exception {
                Long pienui = null;
                Long mesai = null;
                if (request.size() == 0) {
                    return new IslaukaResponse(null, null);
                }
                List<Long> array = new ArrayList<Long>(request.size());
                for (int i = 0; i < request.size(); i++) {
                    array.add(request.get(i).getPrekeId());
                }

                List<Preke> result = getRepo().prepareQuery(Preke.class, "id in (?1)")
                        .setParameterList("1", array).list();

                for (Preke i : result) {
                    if (ANumberUtils.defaultValue(i.getIslaukaPienui()) > ANumberUtils.defaultValue(pienui)) {
                        pienui = i.getIslaukaPienui();
                    }
                    if (ANumberUtils.defaultValue(i.getIslaukaMesai()) > ANumberUtils.defaultValue(mesai)) {
                        mesai = i.getIslaukaMesai();
                    }
                }
                return new IslaukaResponse(mesai, pienui);
            }
        }.process(request);
    }

    GydomuGyvunuZurnalasDtoMap getMapper() {
        return new GydomuGyvunuZurnalasDtoMap();
    }

    @Override
    public List<LookupDto> gyvunoRusys(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<LookupDto>>() {
            @Override
            protected List<LookupDto> processInt(String[] request) throws Exception {
                List<GyvunoRusis> result = getSession().createQuery("from GyvunoRusis").list();
                /* Collections.sort(result, new Comparator<NurasymasList>() {
                    @Override
                    public int compare(NurasymasList o1, NurasymasList o2) {
                        return o2.getData().compareTo(o1.getData());
                    }
                });*/
                List<LookupDto> res = new ArrayList<LookupDto>();
                for (GyvunoRusis item : result) {
                    LookupDto dto = new LookupItemDto();
                    dto.setId(item.getId());
                    dto.setPavadinimas(item.getPavadinimas());
                    res.add(dto);
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public GydomuGyvunuZurnalasDto gauti(Long id) throws Exception {
        return new ProcessorBase<Long, GydomuGyvunuZurnalasDto>() {
            @Override
            protected GydomuGyvunuZurnalasDto processInt(Long id) throws Exception {
                GydomuGyvunuZurnalas item = getRepo().get(GydomuGyvunuZurnalas.class, id);
                GydomuGyvunuZurnalasDto result = getMapper().toDto(item);
                List<ZurnaloVaistas> vaistai = getRepo().getList(ZurnaloVaistas.class, "zurnaloId", id);
                List<ZurnaloGyvunas> gyvunai = getRepo().getList(ZurnaloGyvunas.class, "zurnaloId", id);
                result.getVaistai().clear();
                for (ZurnaloVaistas itemP : vaistai) {
                    result.getVaistai().add(getMapper().toPrekeDto(itemP));
                }
                result.getGyvunai().clear();
                for (ZurnaloGyvunas itemP : gyvunai) {
                    result.getGyvunai().add(getMapper().toGyvunasDto(itemP));
                }
                return result;
            }
        }.process(id);
    }

    @Override
    public GydomuGyvunuZurnalasDto saugoti(GydomuGyvunuZurnalasDto dto) throws Exception {
        return new ProcessorBase<GydomuGyvunuZurnalasDto, GydomuGyvunuZurnalasDto>() {
            @Override
            protected GydomuGyvunuZurnalasDto processInt(GydomuGyvunuZurnalasDto dto) throws Exception {

                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isTrue(dto.getImoneId() > 0, "Nėra įmonės");
                getAssertor().isNotNullStr(dto.getLaikytojas(), "Nėra laikytojo");
                getAssertor().isNotNullStr(dto.getDiagnoze(), "Nėra diagnozės");
                getAssertor().isTrue(dto.getRegistracijosData() != null, "Nėra datos");
                GydomuGyvunuZurnalas item = new GydomuGyvunuZurnalas();
                if (dto.getId() > 0) {
                    item = getRepo().get(GydomuGyvunuZurnalas.class, dto.getId());
                }
                getMapper().fromDto(item, dto);
                getSession().save(item);

                // trinam gyvunus
                getRepo().deleteList(ZurnaloGyvunas.class, "zurnaloId", item.getId());
                List<ZurnaloGyvunas> gyvunai = new ArrayList<>();
                // saugom gyvunai
                for (ZurnaloGyvunasDto itemPDto : dto.getGyvunai()) {
                    ZurnaloGyvunas itemP = new ZurnaloGyvunas();

                    //getAssertor().isNotNullStr(itemPDto.getNumeris(), "Nėra numerio");
                    //getAssertor().isTrue(itemPDto.getAmzius().doubleValue() > 0, "Nėra kiekio");
                    getAssertor().isTrue(itemPDto.getGyvunoRusisId() > 0, "Nėra gyvūno rūšies");

                    getMapper().fromGyvunasDto(itemP, itemPDto, item);

                    getSession().save(itemP);
                    gyvunai.add(itemP);
                }

                // trinam likucius
                getRepo().deleteList(Likutis.class, "zurnaloId", item.getId());
                // trinam prekes
                getRepo().deleteList(ZurnaloVaistas.class, "zurnaloId", item.getId());
                List<ZurnaloVaistas> vaistai = new ArrayList<>();
                // saugom prekes
                for (ZurnaloVaistasDto itemPDto : dto.getVaistai()) {
                    ZurnaloVaistas itemP = new ZurnaloVaistas();

                    getAssertor().isNotNullStr(itemPDto.getReceptas(), "Nėra recepto");
                    getAssertor().isTrue(itemPDto.getKiekis().doubleValue() > 0, "Nėra kiekio");
                    getAssertor().isTrue(itemPDto.getPrekeId() > 0, "Nėra prekės");

                    getMapper().fromPrekeDto(itemP, itemPDto, item);
                    if (itemP.getMatavimoVienetasId() == 0) {
                        MatavimoVienetas mv = gautiMatavimoVieneta(itemP.getPrekeId(), getSession());
                        itemP.setMatavimoVienetasId(mv.getId());
                    }

                    getSession().save(itemP);
                    vaistai.add(itemP);
                }

                // perkuriame likuti

                for (ZurnaloVaistas itemP : vaistai) {
                    issaugotiPanaudojima(item, itemP, getRepo());
                }

                // updatinam pagalbinius laukus
                item.setGydymas(AtaskaitaServiceImpl.gautiGydymoStr(vaistai, getRepo()));
                item.setGyvunuSarasas(AtaskaitaServiceImpl.gautiGyvunasStr(gyvunai, getRepo()));

                item.setGydymas(StringUtils.substring(item.getGydymas(), 0, 400));
                item.setGyvunuSarasas(StringUtils.substring(item.getGyvunuSarasas(), 0, 400));
                getSession().save(item);
                GydomuGyvunuZurnalasDto result = getMapper().toDto(item);
                result.getGyvunai().clear();
                for (ZurnaloGyvunas itemP : gyvunai) {
                    result.getGyvunai().add(getMapper().toGyvunasDto(itemP));
                }
                result.getVaistai().clear();
                for (ZurnaloVaistas itemP : vaistai) {
                    result.getVaistai().add(getMapper().toPrekeDto(itemP));
                }
                return result;
            }
        }.process(dto);

    }

    private void issaugotiPanaudojima(GydomuGyvunuZurnalas item, ZurnaloVaistas itemP, Repository repo) throws Exception {
        List<Likutis> likuciai = repo.prepareQuery(Likutis.class, "prekeId = ?1 and imoneId = ?2")
                .setParameter("1", itemP.getPrekeId()).setParameter("2", item.getImoneId()).list();

        // surandam pajamavimus
        List<LikutisHelper> likuciaiPajamuoti = new ArrayList<>();
        HashMap<Long, LikutisHelper> likuciaiPajamuotiHash = new HashMap<>();
        for (Likutis itemL : likuciai) {
            if (itemL.isArSaskaita()) {
                LikutisHelper likutisHelper = new LikutisHelper(itemL);
                likuciaiPajamuoti.add(likutisHelper);
                likuciaiPajamuotiHash.put(itemL.getId(), likutisHelper);
            }
        }

        // surandam panaudojimus
        for (Likutis itemL : likuciai) {
            if (!itemL.isArSaskaita()) {
                LikutisHelper likutisHelper = likuciaiPajamuotiHash.get(itemL.getPirminisId());
                likutisHelper.panaudoti(-itemL.getKiekis().doubleValue());
            }
        }

        // rusiuojame pagal pajamavimo data
        Collections.sort(likuciaiPajamuoti, new Comparator<LikutisHelper>() {
            @Override
            public int compare(LikutisHelper o1, LikutisHelper o2) {
                return o1.likutis.getData().compareTo(o2.likutis.getData());
            }
        });
        // kuriame likucio irasus
        double kiekis = itemP.getKiekis().doubleValue();
        int lIndex = 0;
        double likutis = 0;
        while (ANumberUtils.greater(kiekis, 0) && likuciaiPajamuoti.size() > lIndex) {
            LikutisHelper likutisHelper = likuciaiPajamuoti.get(lIndex);
            lIndex++;
            if (ANumberUtils.lessOrEqual(likutisHelper.laisvasKiekis, 0)) {
                continue;
            }
            likutis += likutisHelper.laisvasKiekis;
            double pKiekis = likutisHelper.laisvasKiekis;
            if (pKiekis > kiekis) {
                pKiekis = kiekis;
            }

            kiekis -= pKiekis;
            likutisHelper.panaudoti(pKiekis);
            // kuriame irasa
            Likutis l = new Likutis();
            l.setData(item.getRegistracijosData());
            l.setImoneId(item.getImoneId());
            l.setPrekeId(itemP.getPrekeId());
            l.setArSaskaita(false);
            l.setPirminisId(likutisHelper.likutis.getId());
            l.setKiekis(new BigDecimal(-pKiekis));
            l.setZurnaloId(item.getId());
            l.setZurnaloVaistoId(itemP.getId());
            l.setMatavimoVienetasId(likutisHelper.likutis.getMatavimoVienetasId());
            //l.setDokumentas(String.format("GZ: %s", item.getEilesNumeris()));
            repo.getSession().save(l);
        }
        getAssertor().isTrue(ANumberUtils.lessOrEqual(kiekis, 0), "Nėra pajamuota pakankamai prekių. Reikia: " +
                ANumberUtils.decimalToString(itemP.getKiekis()) + " yra: " +
                ANumberUtils.decimalToString(likutis));

    }

    private class LikutisHelper {
        public Likutis likutis;
        public double laisvasKiekis;

        public LikutisHelper(Likutis itemL) {
            likutis = itemL;
            laisvasKiekis = itemL.getKiekis().doubleValue();
        }

        public void panaudoti(double v) {
            laisvasKiekis -= v;
        }
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

    private Imone gautiImone(long id, Session session) throws Exception {
        String queryString = "from Imone c where c.id = ?1";
        List<Imone> list = session.createQuery(queryString)
                .setParameter("1", id).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        Imone item = list.get(0);
        return item;
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
    public List<String> sarasasReceptai(ReceptaiRequest req) throws Exception {
        return new ProcessorBase<ReceptaiRequest, List<String>>() {
            @Override
            protected List<String> processInt(ReceptaiRequest req) throws Exception {
                List<String> result = getSession().createQuery("select distinct c.receptas from ZurnaloVaistas c " +
                        "where c.prekeId = ?1").setParameter("1", req.prekeId).
                        list();
                Collections.sort(result);
                List<String> res = new ArrayList<>();
                for (String item : result) {
                    //StringLookupItemDto dto = new StringLookupItemDto(item);
                    //
                    res.add(item);
                }
                return res;
            }
        }.process(req);
    }

    @Override
    public List<String> sarasasNumeriai(NumeriaiRequest req) throws Exception {
        return new ProcessorBase<NumeriaiRequest, List<String>>() {
            @Override
            protected List<String> processInt(NumeriaiRequest req) throws Exception {
                List<String> result = getSession().createQuery("select distinct c.numeris from vGyvunas c " +
                        "where c.laikytojas = ?1 and c.gyvunoRusisId = ?2").
                        setParameter("1", req.laikytojas).
                        setParameter("2", req.gyvunoRusisId).
                        list();
                List<String> res = new ArrayList<>();
                for (String item : result) {
                    if (StringUtils.isNotEmpty(item)) {
                        res.add(item);
                    }
                }
                Collections.sort(res);

                return res;
            }
        }.process(req);
    }

    @Override
    public List<String> sarasasAmzius(AmziusRequest req) throws Exception {
        return new ProcessorBase<AmziusRequest, List<String>>() {
            @Override
            protected List<String> processInt(AmziusRequest req) throws Exception {
                List<String> result = getSession().createQuery("select distinct c.amzius from ZurnaloGyvunas c " +
                        "where c.gyvunoRusisId = ?1").setParameter("1", req.gyvunoRusisId).
                        list();

                List<String> res = new ArrayList<>();
                for (String item : result) {
                    if (StringUtils.isNotEmpty(item)) {
                        res.add(item);
                    }
                }
                Collections.sort(res);
                return res;
            }
        }.process(req);
    }

    @Override
    public LookupItemDto saugotiGyvunoRusi(LookupItemDto dto) throws Exception {
        return new ProcessorBase<LookupItemDto, LookupItemDto>() {
            @Override
            protected LookupItemDto processInt(LookupItemDto dto) throws Exception {

                getAssertor().isNotNull(dto, "Nėra įrašo");
                getAssertor().isNotNullStr(dto.getPavadinimas(), "Nėra pavadinimo");
                getAssertor().isTrue(validateUnique("from GyvunoRusis c where c.id != ?1 and pavadinimas = ?2",
                        dto.getPavadinimas(), dto.getId(), getSession()), "Tokia rūšis '%s' jau yra", dto.getPavadinimas());

                GyvunoRusis item = new GyvunoRusis();
                if (dto.getId() > 0) {
                    String queryString = "from GyvunoRusis c where c.id = ?1";
                    List<GyvunoRusis> list = getSession().createQuery(queryString)
                            .setParameter("1", dto.getId()).list();
                    getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");

                    item = list.get(0);
                }
                item.setPavadinimas(dto.getPavadinimas());
                getSession().save(item);

                LookupItemDto result = new LookupItemDto();
                result.setId(item.getId());
                result.setPavadinimas(item.getPavadinimas());
                return result;
            }
        }.process(dto);
    }

    @Override
    public LookupItemDto gautiGyvunoRusi(long id) throws Exception {
        return new ProcessorBase<Long, LookupItemDto>() {
            @Override
            protected LookupItemDto processInt(Long id) throws Exception {
                GyvunoRusis item = getRepo().get(GyvunoRusis.class, id);
                LookupItemDto result = new LookupItemDto();
                result.setId(item.getId());
                result.setPavadinimas(item.getPavadinimas());
                return result;
            }
        }.process(id);
    }

    private <T> T gautiIrasa(long id, Class<T> tClass, Session session) throws Exception {
        String queryString = "from " + tClass.getSimpleName() + " c where c.id = ?1";
        List<T> list = session.createQuery(queryString)
                .setParameter("1", id).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        T item = list.get(0);
        return item;
    }

    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }

    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    private void replaceParagraph(String placeholder, String textToAdd, WordprocessingMLPackage template, ContentAccessor addTo) {
        // 1. get the paragraph
        List<Object> paragraphs = getAllElementFromObject(template.getMainDocumentPart(), P.class);

        P toReplace = null;
        for (Object p : paragraphs) {
            List<Object> texts = getAllElementFromObject(p, Text.class);
            for (Object t : texts) {
                Text content = (Text) t;
                if (content.getValue().equals(placeholder)) {
                    toReplace = (P) p;
                    break;
                }
            }
        }

        // we now have the paragraph that contains our placeholder: toReplace
        // 2. split into seperate lines
        String as[] = StringUtils.splitPreserveAllTokens(textToAdd, '\n');

        for (int i = 0; i < as.length; i++) {
            String ptext = as[i];

            // 3. copy the found paragraph to keep styling correct
            P copy = (P) XmlUtils.deepCopy(toReplace);

            // replace the text elements from the copy
            List<?> texts = getAllElementFromObject(copy, Text.class);
            if (texts.size() > 0) {
                Text textToReplace = (Text) texts.get(0);
                textToReplace.setValue(ptext);
            }

            // add the paragraph to the document
            addTo.getContent().add(copy);
        }

        // 4. remove the original one
        ((ContentAccessor) toReplace.getParent()).getContent().remove(toReplace);

    }

    private void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd,
                              WordprocessingMLPackage template) throws Docx4JException, JAXBException {
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

        // 1. find the table
        Tbl tempTable = getTemplateTable(tables, placeholders[0]);
        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = (Tr) rows.get(1);

            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }

    private Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
        for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext(); ) {
            Object tbl = iterator.next();
            List<?> textElements = getAllElementFromObject(tbl, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
                    return (Tbl) tbl;
            }
        }
        return null;
    }

    private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List<?> textElements = getAllElementFromObject(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue != null)
                text.setValue(replacementValue);
        }

        reviewtable.getContent().add(workingRow);
    }
}
