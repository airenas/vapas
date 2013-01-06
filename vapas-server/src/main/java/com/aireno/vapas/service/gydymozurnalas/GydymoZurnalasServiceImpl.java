package com.aireno.vapas.service.gydymozurnalas;

import com.aireno.Constants;
import com.aireno.base.LookupDto;
import com.aireno.dto.*;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.service.GydymoZurnalasService;
import com.aireno.vapas.service.NurasymasService;
import com.aireno.vapas.service.base.ProcessorBase;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class GydymoZurnalasServiceImpl extends ServiceBase implements GydymoZurnalasService {
/*    @Override
    public List<NurasymasListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<NurasymasListDto>>() {
            @Override
            protected List<NurasymasListDto> processInt(String[] request) throws Exception {
                List<NurasymasList> result = getSession().createQuery("from NurasymasList").list();
                Collections.sort(result, new Comparator<NurasymasList>() {
                    @Override
                    public int compare(NurasymasList o1, NurasymasList o2) {
                        return o2.getData().compareTo(o1.getData());
                    }
                });
                NurasymasDtoMap mapper = getMapper();
                List<NurasymasListDto> res = new ArrayList<NurasymasListDto>();
                for (NurasymasList item : result) {
                    res.add(mapper.toListDto(item));
                }
                return res;
            }
        }.process(keywords);
    }*/

    NurasymasDtoMap getMapper() {
        return new NurasymasDtoMap();
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
                getAssertor().isFalse(item.getStatusasNotNull() == SaskaitaStatusas.Patvirtinta, "Nurašymas jau patvirtintas. Negalima saugoti");
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
                    for (Likutis itemL : listL) {
                        suma += itemL.getKiekis().doubleValue();
                    }

                    getAssertor().isFalse(itemP.getKiekis().doubleValue() > suma, "Nėra pajamuota prekių su serija "
                            + itemP.getSerija() + ". Reikia " + ANumberUtils.DecimalToString(itemP.getKiekis()) + " yra " +
                            ANumberUtils.DecimalToString(suma));

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

    @Override
    public Boolean generuotiAtaskaita(long id) throws Exception {
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
                getAssertor().isTrue(item.getStatusasNotNull() == SaskaitaStatusas.Patvirtinta, "Nurašymas dar nepatvirtintas.");

                // saugom prekes
                String queryStringP = "from NurasymoPreke c where c.nurasymasId = ?1";
                List<NurasymoPreke> listP = getSession().createQuery(queryStringP)
                        .setParameter("1", item.getId()).list();

                getAssertor().isTrue(listP.size() > 0, "Nėra prekių");

                WordprocessingMLPackage template = getTemplate("template/nurasymas.docx");

                String toAdd = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
                replacePlaceholder(template, toAdd, "{DATA}");
                replacePlaceholder(template, item.getNumeris(), "{NUMERIS}");
                replacePlaceholder(template, gautiImone(item.getImoneId(), getSession()).getPavadinimas()
                        , "{IMONE}");
                replacePlaceholder(template, new SimpleDateFormat("yyyy").format(item.getData()), "{METAI}");
                replacePlaceholder(template, new SimpleDateFormat("MM").format(item.getData()), "{MENUO}");

                List<Map<String, String>> textToAdd = new ArrayList<Map<String, String>>();

                int i = 1;
                for (NurasymoPreke itemP : listP) {

                    Map<String, String> repl1 = new HashMap<String, String>();
                    repl1.put("{NR}", Integer.toString(i));
                    Preke preke = gautiIrasa(itemP.getPrekeId(),
                            Preke.class, getSession());
                    MatavimoVienetas mvienetas = gautiIrasa(preke.getMatVienetasId(),
                            MatavimoVienetas.class, getSession());
                    repl1.put("{PAV}", preke.getPavadinimas());
                    repl1.put("{SERIJA}", itemP.getSerija());
                    repl1.put("{MATVNT}", mvienetas.getKodas());
                    DecimalFormat format = new DecimalFormat( "0.00" );
                    repl1.put("{KIEKIS}", format.format(itemP.getKiekis()));

                    i++;
                    textToAdd.add(repl1);
                }

                replaceTable(new String[]{"{NR}", "{PAV}", "{SERIJA}", "{MATVNT}", "{KIEKIS}"},
                        textToAdd, template);

                writeDocxToStream(template, "documents/nurasymas_" + Long.toString(item.getId()) + ".docx");


                return true;
            }
        }.process(id);
    }

    @Override
    public List<StringLookupItemDto> sarasasLaisvuPrekiuSeriju(LaisvuSerijuRequest req) throws Exception {
        return new ProcessorBase<LaisvuSerijuRequest, List<StringLookupItemDto>>() {
            @Override
            protected List<StringLookupItemDto> processInt(LaisvuSerijuRequest req) throws Exception {
                String queryString = "from Likutis c where c.prekeId = ?1 and c.imoneId = ?2";
                List<Likutis> list = getSession().createQuery(queryString)
                        .setParameter("1", req.prekeId).setParameter("2", req.imoneId).list();
                List<StringLookupItemDto> result = new ArrayList<>();
                HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
                // formuojame
                for (Likutis item : list) {
                    String serija = item.getSerija();
                    BigDecimal kiek = new BigDecimal(0);
                    if (map.containsKey(serija)) {
                        kiek = map.get(serija);
                    }

                   // if (item.isArSaskaita()) {
                        kiek = kiek.add(item.getKiekis());
                    // else {
                    //    kiek = kiek.subtract(item.getKiekis());
                   // }
                    map.put(serija, kiek);
                }

                for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                    String key = entry.getKey();
                    BigDecimal value = entry.getValue();
                    if (value.compareTo(new BigDecimal(0)) > 0) {
                        StringLookupItemDto dto = new StringLookupItemDto(key);
                        dto.setPavadinimas(String.format("%s (%s)", key, ANumberUtils.DecimalToString(value)));
                        result.add(dto);
                    }
                }
                return result;


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
                String queryString = "from GyvunoRusis c where c.id = ?1";
                List<GyvunoRusis> list = getSession().createQuery(queryString)
                        .setParameter("1", id).list();
                getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
                GyvunoRusis item = list.get(0);
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
