package com.aireno.vapas.service.ataskaitos;

import com.aireno.Constants;
import com.aireno.dto.AtaskaitaListDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.vapas.service.AtaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.nurasymai.NurasymasDtoMap;
import com.aireno.vapas.service.persistance.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.hibernate.Session;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 19.41
 * To change this template use File | Settings | File Templates.
 */
public class AtaskaitaServiceImpl extends ServiceBase implements AtaskaitaService {
    NurasymasDtoMap getMapper() {
        return new NurasymasDtoMap();
    }

    @Override
    public List<AtaskaitaListDto> sarasas(String[] keywords) throws Exception {
        return new ProcessorBase<String[], List<AtaskaitaListDto>>() {
            @Override
            protected List<AtaskaitaListDto> processInt(String[] request) throws Exception {
                File folder = new File("documents");
                File[] f = folder.listFiles();
                List<AtaskaitaListDto> res = new ArrayList<AtaskaitaListDto>();
                for (File item : f) {
                    AtaskaitaListDto dto = new AtaskaitaListDto();
                    dto.setFailas(item.getAbsolutePath());
                    dto.setPavadinimas(item.getName());
                    //dto.setData(item.);
                    res.add(dto);
                }
                return res;
            }
        }.process(keywords);
    }

    @Override
    public Boolean atidaryti(String filename) throws Exception {
        Desktop.getDesktop().open(new File(filename));
        return true;
    }

    @Override
    public Boolean generuotiNurasymus(GeneruotiRequest request) throws Exception {
        return new ProcessorBase<GeneruotiRequest, Boolean>() {
            @Override
            protected Boolean processInt(GeneruotiRequest request) throws Exception {
                getAssertor().isTrue(request.imoneId > 0, "Nenurodyta įmonė");
                getAssertor().isNotNullStr(request.numeris, "Nenurodytas numeris");
                Date data = request.date;
                String imone = gautiImone(request.imoneId, getSession()).getPavadinimas();
                String numeris = request.numeris;
                SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                String failas = String.format("documents/nurasymas_%s_%s.docx", imone, df1.format(data));

                String queryString = "from Likutis c where c.imoneId = ?1 " +
                        "and data > ?2 and data < ?3 and arSaskaita = ?4";
                Date from = DateUtils.truncate(data, Calendar.MONTH);
                Date to = DateUtils.addMonths(from, 1);
                List<Likutis> list = getSession().createQuery(queryString)
                        .setParameter("1", request.imoneId)
                        .setParameter("2", from)
                        .setParameter("3", to).setParameter("4", false).list();

                WordprocessingMLPackage template = getTemplate("template/nurasymas.docx");

                String toAdd = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
                replacePlaceholder(template, toAdd, "{DATA}");
                replacePlaceholder(template, request.numeris, "{NUMERIS}");
                replacePlaceholder(template, imone, "{IMONE}");
                replacePlaceholder(template, new SimpleDateFormat("yyyy").format(request.date), "{METAI}");
                replacePlaceholder(template, new SimpleDateFormat("MM").format(request.date), "{MENUO}");

                List<Map<String, String>> textToAdd = new ArrayList<Map<String, String>>();

                List<ReportN> result = new ArrayList<ReportN>();
                for (Likutis itemP : list) {
                    Likutis pirminis = getRepo().get(Likutis.class, itemP.getPirminisId());
                    SaskaitosPreke sPreke = getRepo().get(SaskaitosPreke.class, pirminis.getSaskaitosPrekesId());
                    ReportN pr = raskPreke(result, itemP, sPreke.getSerija());
                    pr.values += (-itemP.getKiekis().doubleValue());
                }

                int i = 1;
                for (ReportN itemP : result) {

                    Map<String, String> repl1 = new HashMap<String, String>();
                    repl1.put("{NR}", Integer.toString(i));
                    Preke preke = gautiIrasa(itemP.prekeId,
                            Preke.class, getSession());
                    MatavimoVienetas mvienetas = gautiIrasa(preke.getMatVienetasId(),
                            MatavimoVienetas.class, getSession());
                    repl1.put("{PAV}", preke.getPavadinimas());
                    repl1.put("{SERIJA}", itemP.serija);
                    repl1.put("{MATVNT}", mvienetas.getKodas());
                    DecimalFormat format = new DecimalFormat("0.00");
                    repl1.put("{KIEKIS}", format.format(itemP.values));

                    i++;
                    textToAdd.add(repl1);
                }

                replaceTable(new String[]{"{NR}", "{PAV}", "{SERIJA}", "{MATVNT}", "{KIEKIS}"},
                        textToAdd, template);

                writeDocxToStream(template, failas);


                return true;
            }
        }.process(request);
    }

    private ReportN raskPreke(List<ReportN> result, Likutis itemP, String serija) {
        for (ReportN n : result) {
            if (n.prekeId == itemP.getPrekeId() && StringUtils.equals(n.serija, serija)){
                return n;
            }
        }
        ReportN n = new ReportN();
        n.prekeId = itemP.getPrekeId();
        n.serija = serija;
        result.add(n);
        return n;
    }

    private class ReportN {
        public String serija;
        public long prekeId;
        public double values;
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
                    DecimalFormat format = new DecimalFormat("0.00");
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
