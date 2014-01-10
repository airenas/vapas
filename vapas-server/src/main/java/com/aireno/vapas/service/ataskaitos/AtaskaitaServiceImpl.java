package com.aireno.vapas.service.ataskaitos;

import com.aireno.Constants;
import com.aireno.dto.AtaskaitaListDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.service.AtaskaitaService;
import com.aireno.vapas.service.base.ProcessorBase;
import com.aireno.vapas.service.base.Repository;
import com.aireno.vapas.service.base.ServiceBase;
import com.aireno.vapas.service.base.SttsAssertorImpl;
import com.aireno.vapas.service.nurasymai.NurasymasDtoMap;
import com.aireno.vapas.service.persistance.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.hibernate.Session;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author Airenas Vaičiūnas
 *         Date: 12.11.17
 *         Time: 19.41
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
                    dto.setData(new Date(item.lastModified()));
                    res.add(dto);
                }
                Collections.sort(res, new Comparator<AtaskaitaListDto>() {
                    @Override
                    public int compare(AtaskaitaListDto o1, AtaskaitaListDto o2) {
                        return - o1.getData().compareTo(o2.getData());
                    }
                });
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
                getAssertor().isNotNull(request.date, "Nenurodyta data");

                // randame menesio zurnalo irasus
               /* String queryString = "from GydomuGyvunuZurnalas c where c.imoneId = ?1 " +
                        "and registracijosData >= ?2 and registracijosData < ?3";
                Date from = DateUtils.truncate(request.date, Calendar.MONTH);
                Date to = DateUtils.addMonths(from, 1);
                List<GydomuGyvunuZurnalas> list = getSession().createQuery(queryString)
                        .setParameter("1", request.imoneId)
                        .setParameter("2", from)
                        .setParameter("3", to).list();
                // randame nenurasytus gydymo zurnalo irasus
                List<Long> array = new ArrayList<Long>(list.size());
                for (int i = 0; i < list.size(); i++) {
                    array.add(list.get(i).getId());
                }
                // randame nenurasytus vaistus
                List<ZurnaloVaistas> vaistai = getRepo().prepareQuery(ZurnaloVaistas.class, "zurnaloId in (?1)")
                        .setParameterList("1", array).list();*/

                Date data = request.date;
                String imone = gautiImone(request.imoneId, getSession()).getPavadinimas();
                String numeris = request.numeris;
                SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                String failas = String.format("documents/nurasymas_%s_%s.docx", imone, df1.format(data));

                String queryString = "from Likutis c where c.imoneId = ?1 " +
                        "and data >= ?2 and data < ?3 and arSaskaita = ?4";
                Date from = DateUtils.truncate(data, Calendar.MONTH);
                Date to = DateUtils.addMonths(from, 1);
                List<Likutis> list = getSession().createQuery(queryString)
                        .setParameter("1", request.imoneId)
                        .setParameter("2", from)
                        .setParameter("3", to).setParameter("4", false).list();

                Collections.sort(list, new Comparator<Likutis>() {
                    @Override
                    public int compare(Likutis o1, Likutis o2) {
                        return new Long(o1.getPrekeId()).compareTo(o2.getPrekeId());
                    }
                });

                WordprocessingMLPackage template = getTemplate("template/nurasymas.docx");

                String toAdd = new SimpleDateFormat(Constants.DATE_FORMAT).format(data);
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

                // sukuriame nurasyma
                Nurasymas nurasymas = new Nurasymas();
                nurasymas.setData(request.date);
                nurasymas.setImoneId(request.imoneId);
                nurasymas.setNumeris(request.numeris);
                getSession().save(nurasymas);

                for (Likutis itemP : list) {
                    itemP.setDokumentas(numeris);
                    itemP.setNurasymoId(nurasymas.getId());
                    getSession().save(itemP);
                }

                // updatinam likucius nustatom numeri
                for (Likutis itemP : list) {
                    itemP.setDokumentas(numeris);
                    getSession().save(itemP);
                }

                int i = 1;
                for (ReportN itemP : result) {

                    Map<String, String> repl1 = new HashMap<String, String>();
                    repl1.put("{NR}", Integer.toString(i));
                    Preke preke = getRepo().get(Preke.class, itemP.prekeId);
                    MatavimoVienetas mvienetas = getRepo().get(MatavimoVienetas.class, preke.getMatVienetasId());
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

    @Override
    public Boolean generuotiZurnala(GeneruotiRequest request) throws Exception {
        return new ProcessorBase<GeneruotiRequest, Boolean>() {
            @Override
            protected Boolean processInt(GeneruotiRequest request) throws Exception {
                getAssertor().isNotNullStr(request.numeris, "Nenurodytas numeris");
                getAssertor().isNotNull(request.date, "Nenurodyta data");
                int numeris;
                try {
                    numeris = Integer.parseInt(request.numeris);
                } catch (Exception e) {
                    throw getAssertor().newException("Numeris turi būti skaičius. Jis naudojamas žurnalo eilės numeriui pildyti");
                }
                Date data = request.date;
                SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                String failas = String.format("documents/zurnalas_%s.docx", df1.format(data));

                String queryString = "from GydomuGyvunuZurnalas c where  " +
                        "c.registracijosData >= ?1 and c.registracijosData < ?2 ";
                Date from = DateUtils.truncate(data, Calendar.MONTH);
                Date to = DateUtils.addMonths(from, 1);
                List<GydomuGyvunuZurnalas> list = getSession().createQuery(queryString)
                        .setParameter("1", from)
                        .setParameter("2", to)
                        .list();

                Collections.sort(list, new Comparator<GydomuGyvunuZurnalas>() {
                    @Override
                    public int compare(GydomuGyvunuZurnalas o1, GydomuGyvunuZurnalas o2) {
                        return o1.getRegistracijosData().compareTo(o2.getRegistracijosData());
                    }
                });

                WordprocessingMLPackage template = getTemplate("template/zurnalas.docx");

                List<Map<String, String>> textToAdd = new ArrayList<Map<String, String>>();


                int i = numeris;
                for (GydomuGyvunuZurnalas itemP : list) {
                    if (itemP.isArNurasymas()) {
                        continue;
                    }
                    List<ZurnaloVaistas> vaistai = getRepo().getList(ZurnaloVaistas.class, "zurnaloId", itemP.getId());
                    List<ZurnaloGyvunas> gyvunai = getRepo().getList(ZurnaloGyvunas.class, "zurnaloId", itemP.getId());
                    Map<String, String> repl1 = new HashMap<String, String>();

                    repl1.put("{NR}", Integer.toString(i));
                    repl1.put("{DATA}", df.format(itemP.getRegistracijosData()));
                    repl1.put("{LAIKYTOJAS}", itemP.getLaikytojas());
                    repl1.put("{DIAGNOZE}", itemP.getDiagnoze());
                    repl1.put("{GYVUNAS}", gautiGyvunasStr(gyvunai, getRepo()));
                    repl1.put("{GYDYMAS}", gautiGydymoStr(vaistai, getRepo()));
                    repl1.put("{ISLAUKA}", gautiIslaukaStr(itemP, getRepo()));
                    textToAdd.add(repl1);
                    i++;
                }

                replaceTable(new String[]{"{NR}", "{DATA}", "{LAIKYTOJAS}",
                        "{DIAGNOZE}", "{GYVUNAS}", "{GYDYMAS}", "{ISLAUKA}"},
                        textToAdd, template);
                writeDocxToStream(template, failas);


                return true;
            }
        }.process(request);
    }

    @Override
    public Boolean generuotiLikucius(GeneruotiRequest request) throws Exception {
        return new ProcessorBase<GeneruotiRequest, Boolean>() {
            @Override
            protected Boolean processInt(GeneruotiRequest request) throws Exception {
                getAssertor().isTrue(request.imoneId > 0, "Nenurodyta įmonė");
                getAssertor().isNotNull(request.date, "Nenurodyta data");
                Date data = request.date;
                String imone = gautiImone(request.imoneId, getSession()).getPavadinimas();
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                String failas = String.format("documents/apskaita_%s_%s.docx", imone, df1.format(data));

                Date from = DateUtils.truncate(data, Calendar.MONTH);
                Date to = DateUtils.addMonths(from, 1);
                String queryString = "select distinct prekeId from Likutis where imoneId = ?1 " +
                        " and data < ?2";
                List<Long> prekeIds = getSession().createQuery(queryString)
                        .setParameter("1", request.imoneId)
                        .setParameter("2", to).list();
                final HashSet<Long> prekeSet = new HashSet<>(prekeIds);
                // gauname visas prekes
                getLog().debug("get all ");
                List<Preke> prekes = getRepo().getList(Preke.class);
                getLog().debug("order and filter by needed");
                Predicate<Preke> filter = new Predicate<Preke>() {
                    @Override
                    public boolean apply(Preke p) {
                        return prekeSet.contains(p.getId());
                    }
                };

                Function<Preke, String> orderFunction = new Function<Preke, String>() {
                    public String apply(Preke it) {
                        return it.getPavadinimas();
                    }
                };
                ImmutableList<Preke> selectedPrekes = ImmutableList.copyOf(
                        Ordering.natural().onResultOf(orderFunction).sortedCopy(
                                Iterables.filter(prekes, filter)));

                getLog().debug("prekes ready");
                WordprocessingMLPackage doc = getTemplate("template/apskaita.docx");
                MainDocumentPart mainDocumentPart = doc.getMainDocumentPart();

                for (Preke preke : selectedPrekes) {
                    WordprocessingMLPackage template = getTemplate("template/apskaitaPart.docx");
                    MatavimoVienetas mvienetas = getRepo().get(MatavimoVienetas.class, preke.getMatVienetasId());
                    List<Likutis> likuciai = getRepo().prepareQuery(Likutis.class, "prekeId = ?1 and imoneId =?2")
                            .setParameter("1", preke.getId()).setParameter("2", request.imoneId).list();
                    /*Collections.sort(likuciai, new Comparator<Likutis>() {
                        @Override
                        public int compare(Likutis o1, Likutis o2) {
                            int i = ADateUtils.trimTime(o1.getData()).compareTo(ADateUtils.trimTime(o2.getData()));
                            if (i != 0) {
                                return i;
                            }
                            return ANumberUtils.defaultValue(o1.getPirminisId())
                                    .compareTo(ANumberUtils.defaultValue(o2.getPirminisId()));
                        }
                    });*/

                    List<ReportL> reportLList = new ArrayList<>();
                    for (Likutis itemP : likuciai) {
                        if (!itemP.getData().before(to)) {
                            continue;
                        }
                        getAssertor().isTrue(itemP.isArSaskaita() || ANumberUtils.defaultValue(itemP.getNurasymoId()) > 0,
                                "Pirmiausiai sugeneruokite nurašymą, likučio prekė '%s' dar neįdėta į nurašymą",
                                getLikucioPrekeInfo(preke, itemP));
                        ReportL item = raskPreke(reportLList, itemP, getPrekesSerija(likuciai, itemP, getRepo()),
                                ANumberUtils.defaultValue(itemP.getNurasymoId()));

                    }
                    // pildom duomenis
                    for (ReportL item : reportLList) {
                        if (item.nurasymoId > 0) {
                            Nurasymas nurasymas = getRepo().get(Nurasymas.class, item.nurasymoId);
                            item.data = nurasymas.getData();
                        } else {
                            item.data = item.itemL.getData();
                        }
                    }
                    // rusiuojame
                    Collections.sort(reportLList, new Comparator<ReportL>() {
                        @Override
                        public int compare(ReportL o1, ReportL o2) {
                            int i = ADateUtils.trimTime(o1.data).compareTo(ADateUtils.trimTime(o2.data));
                            if (i != 0) {
                                return i;
                            }
                            return ANumberUtils.defaultValue(o2.nurasymoId)
                                    .compareTo(ANumberUtils.defaultValue(o1.nurasymoId));
                        }
                    });

                    // ruosiame dokumenta

                    replacePlaceholder(template, preke.getPavadinimas(), "{VAISTAS}");
                    replacePlaceholder(template, mvienetas.getKodas(), "{MVNT}");

                    List<Map<String, String>> textToAdd = new ArrayList<Map<String, String>>();
                    double kiekis = 0;
                    for (ReportL item : reportLList) {

                        Map<String, String> repl1 = new HashMap<String, String>();
                        Saskaita saskaita = null;
                        SaskaitosPreke sPreke = null;
                        if (item.nurasymoId == 0) {
                            kiekis += item.kiekis;
                            saskaita = getRepo().get(Saskaita.class, item.itemL.getSaskaitosId());
                            String tiekejasStr = "";
                            if (ANumberUtils.defaultValue(saskaita.getTiekejasId()) > 0) {
                                Tiekejas tiekejas = getRepo().get(Tiekejas.class, saskaita.getTiekejasId());
                                tiekejasStr = tiekejas.getPavadinimas();
                            }
                            sPreke = getRepo().get(SaskaitosPreke.class, item.itemL.getSaskaitosPrekesId());
                            repl1.put("{DATA}", ADateUtils.dateToString(item.data));
                            repl1.put("{DOKNR}", saskaita.getNumeris() + " " + tiekejasStr);
                            repl1.put("{SERIJA}", sPreke.getSerija());
                            repl1.put("{TINKAIKI}", ADateUtils.dateToString(sPreke.getGaliojaIki()));
                            repl1.put("{SUNAUDOTA}", "");
                            repl1.put("{LIKUTIS}", ANumberUtils.decimalToString(kiekis));
                            repl1.put("{KIEKIS}", ANumberUtils.decimalToString(item.kiekis));
                        } else {
                            kiekis -= (-item.kiekis);
                            repl1.put("{DATA}", ADateUtils.dateToString(item.data));
                            repl1.put("{DOKNR}", item.itemL.getDokumentas());
                            repl1.put("{SERIJA}", item.serija);
                            repl1.put("{TINKAIKI}", "");
                            repl1.put("{SUNAUDOTA}", ANumberUtils.decimalToString(-item.kiekis));
                            repl1.put("{LIKUTIS}", ANumberUtils.decimalToString(kiekis));
                            repl1.put("{KIEKIS}", "");
                        }

                        textToAdd.add(repl1);
                    }

                    replaceTable(new String[]{"{DATA}", "{DOKNR}", "{KIEKIS}", "{TINKAIKI}", "{SERIJA}", "{SUNAUDOTA}", "{LIKUTIS}"},
                            textToAdd, template);
                    for (Object i : template.getMainDocumentPart().getContent()) {
                        mainDocumentPart.addObject(i);
                    }

                }
                writeDocxToStream(doc, failas);
                return true;
            }
        }.process(request);
    }

    private String getLikucioPrekeInfo(Preke preke, Likutis itemP) {
        return String.format("%s [%s]", preke.getPavadinimas(), ADateUtils.dateToString(itemP.getData()));
    }

    @Override
    public Boolean generuotiDabartiniusLikucius(GeneruotiDabLikuciaiRequest request) throws Exception {
        return new ProcessorBase<GeneruotiDabLikuciaiRequest, Boolean>() {
            @Override
            protected Boolean processInt(final GeneruotiDabLikuciaiRequest request) throws Exception {
                getAssertor().isTrue(request.imoneId > 0, "Nenurodyta įmonė");
                Date data = new Date();
                String imone = gautiImone(request.imoneId, getSession()).getPavadinimas();
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                String failas = String.format("documents/likuciai_%s_%s.xlsx", imone, df1.format(data));

                String queryString = "from LikutisList where imone = ?1";

                List<LikutisList> result = getSession().createQuery(queryString)
                        .setParameter("1", imone).list();

                getLog().info("got {} items.", result.size());
                Predicate<LikutisList> filter = new Predicate<LikutisList>() {
                    @Override
                    public boolean apply(LikutisList p) {
                        return !request.arTikTeigiami || p.getKiekis().compareTo(BigDecimal.ZERO) > 0;
                    }
                };

                Function<LikutisList, String> orderFunction = new Function<LikutisList, String>() {
                    public String apply(LikutisList it) {
                        return it.getPreke();
                    }
                };
                ImmutableList<LikutisList> selectedLikuciai = ImmutableList.copyOf(
                        Ordering.natural().onResultOf(orderFunction).sortedCopy(
                                Iterables.filter(result, filter)));
                getLog().info("filtered by ar teigiami {} items.", selectedLikuciai.size());

                Workbook wb = new XSSFWorkbook();
                CreationHelper createHelper = wb.getCreationHelper();
                Sheet sheet = wb.createSheet("Likučiai " + imone);

                int rowCount = 0;
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowCount++);
                // Create a cell and put a value in it.
                addCellToRow(row, createHelper.createRichTextString("Data: "));
                addCellToRow(row, createHelper.createRichTextString(ADateUtils.dateToString(data)));
                row = sheet.createRow(rowCount++);
                addCellToRow(row, createHelper.createRichTextString("Įmonė: "));
                addCellToRow(row, createHelper.createRichTextString(imone));
                row = sheet.createRow(rowCount++);
                addCellToRow(row, createHelper.createRichTextString("Prekė"));
                addCellToRow(row, createHelper.createRichTextString("M. Vienetas"));
                addCellToRow(row, createHelper.createRichTextString("Kiekis"));
                addCellToRow(row, createHelper.createRichTextString("Pajamuota"));
                for (LikutisList item : selectedLikuciai) {
                    row = sheet.createRow(rowCount++);
                    addCellToRow(row, createHelper.createRichTextString(item.getPreke()));
                    addCellToRow(row, createHelper.createRichTextString(item.getMatavimoVienetas()));
                    addCellToRow(row, createHelper
                            .createRichTextString(ANumberUtils.decimalToString(item.getKiekis())));
                    addCellToRow(row, createHelper
                            .createRichTextString(ANumberUtils.decimalToString(item.getPajamuota())));
                }

                getLog().info("done. saving...");


                try (FileOutputStream fileOut = new FileOutputStream(failas)) {
                    wb.write(fileOut);
                    fileOut.close();
                }
                getLog().info("done.");
                return true;
            }
        }.process(request);
    }

    private void addCellToRow(org.apache.poi.ss.usermodel.Row row, RichTextString richTextString) {
        int lCount = row.getLastCellNum();
        if (lCount < 0) {
            lCount = 0;
        }
        org.apache.poi.ss.usermodel.Cell cell = row.createCell(lCount);
        cell.setCellValue(richTextString);
    }

    private ReportL raskPreke(List<ReportL> result, Likutis itemP, String serija, long nurasymoId) {
        if (!itemP.isArSaskaita()) {
            for (ReportL n : result) {
                if (n.prekeId == itemP.getPrekeId() && StringUtils.equals(n.serija, serija)
                        && n.nurasymoId == nurasymoId) {
                    n.kiekis += itemP.getKiekis().doubleValue();
                    return n;
                }
            }
        }
        ReportL n = new ReportL();
        n.prekeId = itemP.getPrekeId();
        n.serija = serija;
        n.nurasymoId = nurasymoId;
        n.kiekis = itemP.getKiekis().doubleValue();
        n.itemL = itemP;
        result.add(n);
        return n;
    }

    private class ReportL {
        public Date data;
        public String serija;
        public long prekeId;
        public long nurasymoId;
        Likutis itemL;
        public String prekePavadinimas;
        public String matavimoVienetas;
        public double kiekis;
    }

    private String getPrekesSerija(List<Likutis> likuciai, Likutis itemP, Repository repo) throws Exception {
        for (Likutis l : likuciai) {
            if (l.getId() == itemP.getPirminisId()) {
                return repo.get(SaskaitosPreke.class, l.getSaskaitosPrekesId()).getSerija();
            }
        }
        return "";
    }

    public static String gautiGydymoStr(List<ZurnaloVaistas> vaistai, Repository repo) throws Exception {
        String result = "";
        for (ZurnaloVaistas v : vaistai) {
            Preke p = null;
            MatavimoVienetas mv = null;

            p = repo.get(Preke.class, v.getPrekeId());
            mv = repo.get(MatavimoVienetas.class, p.getMatVienetasId());
            if (!StringUtils.isEmpty(result)) {
                result += "\n";
            }
            result += String.format("%s\n%s%s", p.getPavadinimas()
                    , formuotiKiekioStr(v.getKiekis(), mv.getKodas(), repo)
                    , v.getReceptas());
        }
        return result;
    }

    private static String formuotiKiekioStr(BigDecimal kiekis, String kodas, Repository repo) throws Exception {
        String template;
        if (ANumberUtils.equal(kiekis, new BigDecimal(1))) {
            template = getSetting("KIEKIS=1_STR", repo);
        } else {
            template = getSetting("KIEKIS>1_STR", repo);
        }
        new SttsAssertorImpl().isNotNullStr(template, "Nėra nustatymuose šablono vaistų kiekio spausdinimui");
        String result;
        try {
            result = StringUtils.replace(template, "{kiekis}", ANumberUtils.decimalToStringTryAsInt(kiekis));
            result = StringUtils.replace(result, "{mvienetas}", kodas);
            result = StringUtils.replace(result, "{newline}", "\n");
        } catch (Exception e) {
            throw new SttsAssertorImpl().newException("Blogas šablonas kiekio spausdinimui '%s'. Klaida: %s", template, e.getLocalizedMessage());
        }
        return result;
    }

    private static String getSetting(String s, Repository repo) throws Exception {
        Nustatymas nustatymas = repo.get(Nustatymas.class, "kodas", s);
        return nustatymas.getReiksme();
    }

    public static String gautiIslaukaStr(GydomuGyvunuZurnalas item, Repository repo) throws Exception {
        String result = "";
        if (item.getIslaukaMesai() != null) {
            result += "m." + ADateUtils.dateToString(item.getIslaukaMesai());
        }
        if (!StringUtils.isEmpty(result)) {
            result += "\n";
        }
        if (item.getIslaukaPienui() != null) {
            result += "p." + ADateUtils.dateToString(item.getIslaukaPienui());
        }

        return result;
    }


    public static String gautiGyvunasStr(List<ZurnaloGyvunas> gyvunai, Repository repo) throws Exception {
        String result = "";
        for (ZurnaloGyvunas g : gyvunai) {
            GyvunoRusis gr = null;
            if (g.getGyvunoRusisId() > 0) {
                gr = repo.get(GyvunoRusis.class, g.getGyvunoRusisId());
            }
            String rusis = "";

            if (gr != null) {
                rusis = gr.getPavadinimas();
            }
            if (!StringUtils.isEmpty(result)) {
                result += "\n";
            }
            result += String.format("%s %s %s", rusis, StringUtils.defaultString(g.getAmzius()),
                    StringUtils.defaultString(g.getNumeris()));
        }
        return result;
    }

    private ReportN raskPreke(List<ReportN> result, Likutis itemP, String serija) {
        for (ReportN n : result) {
            if (n.prekeId == itemP.getPrekeId() && StringUtils.equals(n.serija, serija)) {
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

    private Imone gautiImone(long id, Session session) throws Exception {
        String queryString = "from Imone c where c.id = ?1";
        List<Imone> list = session.createQuery(queryString)
                .setParameter("1", id).list();
        getAssertor().isTrue(list.size() == 1, "Nerastas įrašas");
        Imone item = list.get(0);
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
            if (StringUtils.contains(textElement.getValue(), placeholder)) {
                String value = textElement.getValue();
                value = StringUtils.replace(value, placeholder, name);
                textElement.setValue(value);
            }
        }
    }


    private void replaceParagraph1(String placeholder, String textToAdd, WordprocessingMLPackage template, ContentAccessor addTo) {
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
        if (rows.size() > 1) {
            // this is our template row
            Tr templateRow = (Tr) rows.get(rows.size() - 1);

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

    private static void addRowToTableOld(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List<?> textElements = getAllElementFromObject(workingRow, Text.class);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            for (Object object : textElements) {
                Text text = (Text) object;
                String value = text.getValue();
                if (StringUtils.contains(value, entry.getKey())) {
                    value = StringUtils.replace(value, entry.getKey(), StringUtils.defaultIfEmpty(entry.getValue(), ""));
                    text.setValue(value);
                }
            }
        }
        reviewtable.getContent().add(workingRow);
    }

    private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List<?> textElements = getAllElementFromObject(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String value = text.getValue();
            String textToReplace = replacements.get(value);
            textToReplace = StringUtils.defaultIfEmpty(textToReplace, "");
            if (StringUtils.contains(textToReplace, "\n")) {
                replaceParagraph(text, textToReplace, workingRow);
            } else {
                text.setValue(textToReplace);
            }
        }

        reviewtable.getContent().add(workingRow);
    }

    private static void replaceParagraph(Text text, String textToAdd, Tr workingRow) {
        // 1. get the paragraph
        P toReplace = null;
        List<Object> paragraphs = getAllElementFromObject(workingRow, P.class);
        for (Object p : paragraphs) {
            List<Object> texts = getAllElementFromObject(p, Text.class);
            for (Object t : texts) {
                Text content = (Text) t;
                if (content == text) {
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
            ((ContentAccessor) toReplace.getParent()).getContent().add(copy);
            //toReplace.getContent().add(copy);
            //addTo.getContent().add(copy);
        }

        // 4. remove the original one
        ((ContentAccessor) toReplace.getParent()).getContent().remove(toReplace);

    }


}
