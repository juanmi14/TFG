/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceltoxml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import logic.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 *
 * @author Juanmi
 */
public class ExcelToXML {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XSSFWorkbook excel = null;
        BreakpointEUCAST documento = new BreakpointEUCAST(2016);
        Dosages dosages = new Dosages();
        ArrayList<Family> families = new ArrayList<Family>();
        try {
            excel = new XSSFWorkbook(new File(args[0]));
            Iterator<Sheet> iSheet = excel.sheetIterator();
            while (iSheet.hasNext()) {//recorro TODO el documento hoja por hoja
                XSSFSheet hoja = (XSSFSheet) iSheet.next();
                if (!hoja.getSheetName().equals("Content") && !hoja.getSheetName().equals("Notes")
                        && !hoja.getSheetName().equals("Guidance") && !hoja.getSheetName().equals("Changes")) {
//                    System.out.println(hoja.getSheetName());
                    if (hoja.getSheetName().equals("Dosages")) {
                        extraerDosages(dosages, hoja);
                    } /*else if(hoja.getSheetName().equals("Dosages")){
                        
                    }*/else {//si NO es DOSAGES, CONTENT, NOTES, GUIDANCE o CHANGES -> FAMILIA
                        Family familia = extraerFamily(hoja);
                        if (familia != null && familia.getGroups() != null && !familia.getGroups().isEmpty()) {//si alguna familia no contiene datos no se guarda ni procesa
                            families.add(familia);
                        }
                    }
                }
            }
            documento.setDosages(dosages);
            documento.setFamilies(families);
            
            
//            System.out.println(">Dosages: " + documento.getDosages());
//            System.out.println(">Families: " + documento.getFamilies());
//            XSSFSheet hoja = excel.getSheetAt(4);
//            List<String> datos = new ArrayList<String>();
//
//            List<String> links = new ArrayList<String>();
//
//            //recorrido
//            Iterator filas = hoja.rowIterator();
//            while (filas.hasNext()) {
//                XSSFRow fila = (XSSFRow) filas.next();
//                Iterator celdas = fila.cellIterator();
//                while (celdas.hasNext()) {
//                    XSSFCell celda = (XSSFCell) celdas.next();
//                    System.out.print(celda.toString() + " || ");
//                    datos.add(String.valueOf(celda));
//                    Hyperlink linkAddress = celda.getHyperlink();
//                    if (linkAddress != null) {
//                        links.add(linkAddress.getAddress());
//                    }
//                }
//                System.out.println();
//            }
//            System.out.println(datos);
//            System.out.println(links);

//            XSSFCell b8 = getCelda(hoja, Columnas.E, 11);
//            XSSFRichTextString valor = b8.getRichStringCellValue();
//            if (valor.numFormattingRuns() > 1) {
//                System.out.println("valor: " + getValor(valor));
//                System.out.println("superindice: " + getSuperIndice(valor));
//            }
        } catch (IOException ex) {
            Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (excel != null) {
                try {
                    excel.close();
                } catch (IOException ex) {
                    Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static XSSFCell getCelda(XSSFSheet hoja, Columnas columna, int fila) {
        XSSFCell celda = null;
        int cell = 0, row = 0;
        cell = columna.ordinal();
        row = fila - 1;
        celda = hoja.getRow(row).getCell(cell);
        return celda;
    }

    private static void extraerDosages(Dosages dosages, XSSFSheet hoja) {
        //los datos empiezan en la casilla A5
        ArrayList<Group> grupos = new ArrayList<Group>();
        Iterator iFila = hoja.rowIterator();
        int idxF = 0;
        boolean vengoDeGrupo = false;//así puedo controlar el inicio de un grupo
        Group grupo = new Group();
        ArrayList<AntimicrobialAgent> micros = new ArrayList<AntimicrobialAgent>();
        DosagesAntimicrobialAgent medic;
        while (iFila.hasNext()) {
            XSSFRow fila = (XSSFRow) iFila.next();
            if (fila != null) {
                if (idxF >= 4) {//fila en la que empieza el contenido (5)
                    //el contenido es:
                    //0-AntimicrobialAgent
                    //1-StandardDose
                    //2-HighDose
                    XSSFCell celdaNombre = fila.getCell(0);
                    XSSFCell celdaStandard = fila.getCell(1);
                    XSSFCell celdaHigh = fila.getCell(2);
                    if (celdaNombre != null) {
                        //obtengo los tamaños de letra para diferenciar los grupos de los medicamentos
                        XSSFFont fuente = celdaNombre.getCellStyle().getFont();
                        short grupoS = 10;
                        short medicS = 8;
                        
                        String nombre = procesarString(celdaNombre.getStringCellValue());

                        if (fuente.getFontHeightInPoints() == grupoS) {
                            if (!vengoDeGrupo) {
                                if (!micros.isEmpty()) {
                                    grupo.setAntimicrobialAgents(micros);
                                    micros = new ArrayList<AntimicrobialAgent>();
                                    grupos.add(grupo);
                                }
                                grupo = new Group(nombre);
                                vengoDeGrupo = true;
                            }
                        }else if (fuente.getFontHeightInPoints() == medicS && nombre.length() > 0) {
                            medic = new DosagesAntimicrobialAgent(nombre);
                            String standard = celdaStandard.getStringCellValue().trim();
                            String high = celdaHigh.getStringCellValue().trim();
                            Hyperlink link = celdaNombre.getHyperlink();
                            if (standard != null && !standard.isEmpty()) {
                                medic.setStandarDose(standard);
                            }
                            if (high != null && !high.isEmpty()) {
                                medic.setHighDose(high);
                            }
                            if (link != null) {
                                medic.setLink(link.getAddress());
                            }
                            micros.add(medic);
                            vengoDeGrupo = false;
                        }
                    }
                }
                idxF++;
            }
        }
        //el último grupo se añande después del bucle
        grupo.setAntimicrobialAgents(micros);
        grupos.add(grupo);
        dosages.setGroups(grupos);
    }

    private static Family extraerFamily(XSSFSheet hoja) {
        Family familia = null;
        //lo primero es detectar si es DEFINIDO o NO DEFINIDO
        //ya que tienen contenido diferente por lo que no se procesan igual
        //las NO DEFINIDO tienen el disk diffusion en D3 en lugar de G3
        familia = new Family(procesarString(getCelda(hoja, Columnas.A, 1).getStringCellValue()));
        XSSFCell celdaDisk = getCelda(hoja, Columnas.G, 3);
        if (celdaDisk != null) {
            familia.setDefined(celdaDisk.getStringCellValue().trim().contains("Disk diffusion"));
        }
        if(familia.isDefined()){
            procesarDefinida(familia, celdaDisk, hoja);
            System.out.println(">>----------------------------" + familia + "----------------------------<<");
            System.out.println(familia.getGroups());
        }else{
            procesarNoDefinida(familia, hoja);
            System.out.println(">>----------------------------" + familia + "----------------------------<<");
        }
        
//        System.out.println(">>Nombre: " + familia.getName());
//        if(familia.isDefined()){
//            System.out.println(procesarString(familiName) + ": DEFINIDO");
//        }else{
//            System.out.println(procesarString(familiName) + ": NO DEFINIDO");
//        }
//        System.out.println(familia);
        return familia;
    }

    private static void procesarDefinida(Family familia, XSSFCell celdaDisk, XSSFSheet hoja) {
        //1-tiene un diskDisfution estándar
//        System.out.println(familia.getName());
        DiskDifution df = new DiskDifution();
        String content[] = celdaDisk.getStringCellValue().split("\n");
        df.setMedium(content[1].replace("Medium: ", ""));
        df.setInoculum(content[2].replace("Inoculum: ", ""));
        df.setIncubation(content[3].replace("Incubation: ", ""));
        df.setReading(content[4].replace("Reading: ", ""));
        df.setQualityControl(content[5].replace("Quality control: ", ""));
        //2-recorrer los grupos
        //cada grupo tiene sus notas y puede tener alguna como superscript
        //cuando se detecte un grupo, recorrer sus agentes, extrayendo enlaces, mediciones y notas
        //el primer grupo empieza en A5
        //las notas están para cada grupo en G(f+2), A5 -> G7, A28 -> G30
        ArrayList<Group> grupos = new ArrayList<Group>();//los grupos de la familia
        Iterator iFila = hoja.rowIterator();
        int idxF = 0;
        boolean vengoDeGrupo = true;//así puedo controlar el inicio de un grupo
        //2.1-busco un grupo
        //2.2-proceso y añado sus micros
        //2.3-encuentro otro grupo
        Group grupo = new Group();
        List<Note> listaNotas = new ArrayList<Note>();
        TreeMap<String, String> mapaNotas = new TreeMap<String, String>();
        FamilyAntimicrobialAgent medic;
        while (iFila.hasNext()) {
            XSSFRow fila = (XSSFRow) iFila.next();
            if (fila != null) {
                if (idxF >= 4) {//fila en la que empieza el contenido (5)
                    XSSFCell celdaNombre = fila.getCell(0);
                    if (celdaNombre != null) {
                        //el contenido es para cada agente
                        //0-AntimicrobialAgent
                        //1-S MIC
                        //2-R MIC
                        //3-Disk content
                        //4-S zone
                        //5-R zone
                        
                        //obtengo los tamaños de letra para diferenciar los grupos de los medicamentos
                        XSSFFont fuente = celdaNombre.getCellStyle().getFont();
                        short grupoS = 10;
                        short medicS = 8;
                        short stopS = 12;
                        
                        String nombre = procesarString(celdaNombre.getStringCellValue());
//
                        if(fuente.getFontHeightInPoints() == stopS){//esto corresponde a notas en la parte inferior de alguna familia
                            break;
                        }

                        if (fuente.getFontHeightInPoints() == grupoS && nombre.length() > 0) {
                            //hay casos particulares en que unas notas tienen tamaño 10, hay que controlarlo
                            if(fila.getCell(1) != null && fila.getCell(1).getStringCellValue().contains("MIC")){
                                if(!vengoDeGrupo){//cuando detecto que llego a un nuevo grupo, guardo el anterior
                                    familia.addGroup(grupo);
                                }
                                grupo = new Group();
                                //obtener sus notas
                                int idFNotas = idxF + 3;
                                //en algunos casos no hay en +3 si no en +4
                                //hay que buscar en +3, si no, en +4, si no, no hay notas
                                XSSFCell celdaNotas = getCelda(hoja, Columnas.G, idFNotas);
                                String notas = celdaNotas.getStringCellValue();
                                String tachados = getValoresNotasTachados(celdaNotas.getRichStringCellValue());
                                if(notas != null && notas.isEmpty()){
                                    celdaNotas = getCelda(hoja, Columnas.G, idFNotas + 1);
                                    if(celdaNotas != null){
                                        notas += celdaNotas.getStringCellValue();
                                        tachados += getValoresNotasTachados(celdaNotas.getRichStringCellValue());
                                    }
                                }
                                //quito las notas tachadas de las buenas
                                notas = notas.replace(tachados, "");
                                
                                //ahora tengo que tratarlas quitando saltos de línea y extrayendo sus índices y valores
                                mapaNotas = obtenerNotas(notas);
                                System.out.println("-----------------NOTAS------------------ >" + nombre + "<\n" + notas);
                                //también puede tener "ownnotes" como superscript
                                XSSFRichTextString valor = celdaNombre.getRichStringCellValue();
                                if(valor.numFormattingRuns() > 1){
                                    grupo.setName(getValor(valor));
//                                    grupo.setOwnNotes(getSuperIndice(valor));
                                    obtenerIndiceNotas(getSuperIndice(valor));
                                }else{
                                    grupo.setName(nombre);
                                }
                                
                                grupo.setNotes(notas);
                                //System.out.println(grupo);
                                vengoDeGrupo = true;
                            }
                        }else if (fuente.getFontHeightInPoints() == medicS && nombre.length() > 0) {
                            String stCelda;
                            XSSFRichTextString valorCelda;
                            Hyperlink linkCelda;
                            String superScript = null;
                            
                            //celda S MIC
                            XSSFCell celdaS = fila.getCell(1);
                            try{
                                valorCelda = celdaS.getRichStringCellValue();
                                if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
                                    stCelda = getValor(valorCelda);
                                    superScript = getSuperIndice(valorCelda);
                                }
                                else
                                    stCelda = celdaS.getStringCellValue();
                            }catch(IllegalStateException e){
                                stCelda = String.valueOf((int)celdaS.getNumericCellValue());
                            }
//                            if(stCelda.equals("NA") || stCelda.equals("-"))
//                                stCelda = "";
                            linkCelda = celdaS.getHyperlink();
                            
                            S s = new S();
                            s.setValue(stCelda);
                            if (superScript != null && !superScript.isEmpty()) {
                                listaNotas = new ArrayList<Note>();
                                List<String> nts = obtenerIndiceNotas(superScript);
                                for (String n : nts) {
                                    listaNotas.add(new Note(n, mapaNotas.get(n)));
                                }
                                s.setNote(listaNotas);
                            }
                            s.setLink((linkCelda != null ? linkCelda.getAddress() : null));
                            
                            
                            //celda R MIC
                            XSSFCell celdaR = fila.getCell(2);
                            try{
                                valorCelda = celdaR.getRichStringCellValue();
                                if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
                                    stCelda = getValor(valorCelda);
                                    superScript = getSuperIndice(valorCelda);
                                }
                                else
                                    stCelda = celdaR.getStringCellValue();
                            }catch(IllegalStateException e){
                                stCelda = String.valueOf((int)celdaR.getNumericCellValue());
                            }
//                            if(stCelda.equals("NA") || stCelda.equals("-"))
//                                stCelda = "";
                            linkCelda = celdaR.getHyperlink();
                            
                            R r = new R();
                            r.setValue(stCelda);
//                            r.setNote(superScript);
                            r.setLink((linkCelda != null ? linkCelda.getAddress() : null));
                            
                            MICBreakpoint mbp = new MICBreakpoint(s, r);
                            
                            
                            //Disk content
                            String diskContent;
                            XSSFCell celdaDC = fila.getCell(3);
                            try{
                                valorCelda = celdaDC.getRichStringCellValue();
                                if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
                                    diskContent = getValor(valorCelda);
                                }
                                else
                                    diskContent = celdaDC.getStringCellValue();
                            }catch(IllegalStateException e){
                                diskContent = String.valueOf((int)celdaDC.getNumericCellValue());
                            }
                            
                            if(diskContent != null && diskContent.isEmpty())
                                diskContent = "-";
                            
                            
                            //celda S Diameter
                            celdaS = fila.getCell(4);
                            try{
                                valorCelda = celdaS.getRichStringCellValue();
                                if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
                                    stCelda = getValor(valorCelda);
                                    superScript = getSuperIndice(valorCelda);
                                }
                                else
                                    stCelda = celdaS.getStringCellValue();
                            }catch(IllegalStateException e){
                                stCelda = String.valueOf((int)celdaS.getNumericCellValue());
                            }
//                            if(stCelda.equals("NA") || stCelda.equals("-"))
//                                stCelda = "";
                            linkCelda = celdaS.getHyperlink();
                            
                            s = new S();
                            s.setValue(stCelda);
//                            s.setNote(superScript);
                            s.setLink((linkCelda != null ? linkCelda.getAddress() : null));

                            
                            //celda R Diameter
                            celdaR = fila.getCell(5);
                            try{
                                valorCelda = celdaR.getRichStringCellValue();
                                if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
                                    stCelda = getValor(valorCelda);
                                    superScript = getSuperIndice(valorCelda);
                                }
                                else
                                    stCelda = celdaR.getStringCellValue();
                            }catch(IllegalStateException e){
                                stCelda = String.valueOf((int)celdaR.getNumericCellValue());
                            }
//                            if(stCelda.equals("NA") || stCelda.equals("-"))
//                                stCelda = "";
                            linkCelda = celdaR.getHyperlink();
                            
                            r = new R();
                            r.setValue(stCelda);
//                            r.setNote(superScript);
                            r.setLink((linkCelda != null ? linkCelda.getAddress() : null));
                            
                            ZoneDiameterBreakpoint zoneDBp = new ZoneDiameterBreakpoint(s, r);
                            
                            //creo el objeto de medicamento
                            //DEBO CONTROLAR EL NOMBRE (superscript)
                            medic = new FamilyAntimicrobialAgent(nombre);
                            
                            medic.setLink((celdaNombre.getHyperlink() != null ? celdaNombre.getHyperlink().getAddress() : null));
                            medic.setMicBreakpoint(mbp);
                            medic.setDiskContent(diskContent);
                            medic.setZoneDiameterBreakpoint(zoneDBp);
                            
                            //lo añado al grupo
                            grupo.addAntimicrobialAgent(medic);
                            
//                            medic = new FamilyAntimicrobialAgent(nombre);
////                            String sMic = fila.getCell(1).getStringCellValue().trim();
////                            Hyperlink link = celdaNombre.getHyperlink();
////                            
////                            if (sMic != null && !sMic.isEmpty()) {
////                                
////                            }
////                            if (link != null) {
////                                medic.setLink(link.getAddress());
////                            }
////                            
////                            grupo.addAntimicrobialAgent(medic);
                            
                            vengoDeGrupo = false;
                        }
                    }
                }
                idxF++;
            }
        }
        //hay que añadir el último grupo
        familia.addGroup(grupo);
    }
    
    private static TreeMap<String, String> obtenerNotas(String notas){
        TreeMap<String, String> mapa = new TreeMap<String, String>();
        String filas[] = notas.split("\n");
        //tener en cuenta que hay notas que ocupan varias filas
        //se guarda el id y se le añaden las notas hasta encontrar otro id
        //si el id tiene "/" deben crearse dos notas diferentes
                String lastId = "";
                String lastId2 = "";
        for(String s: filas){
            if(!s.isEmpty()){
                String iSt = s.substring(0, s.contains(".") ? s.indexOf(".") : 0);
                if(!iSt.isEmpty() && iSt.length() <= 3){
                    String id = iSt;
                    lastId = id;
                    if(iSt.contains("/")){
                        id = iSt.substring(0, iSt.indexOf("/"));
                        String id2 = iSt.substring(iSt.indexOf("/") + 1);
                        mapa.put(id2, s.substring(s.indexOf(".") + 1));
                        lastId = id;
                        lastId2 = id2;
                    }
                    mapa.put(id, s.substring(s.indexOf(".") + 1));
                }else{//no empieza por ***. -> forma parte de la/las nota/s anterior/es
                    if(!lastId.isEmpty()){
                        String cnt = mapa.get(lastId);
                        //mapa.remove(lastId);
                        mapa.put(lastId, cnt + s);
                    }
                    if(!lastId2.isEmpty()){
                        String cnt = mapa.get(lastId2);
                        //mapa.remove(lastId);
                        mapa.put(lastId2, cnt + s);
                    }
                }
            }
        }
        return mapa;
    }

//    private static void obtenerValoresCelda(XSSFRichTextString valorCelda, XSSFCell celda, String stCelda, String superScript){
//        try{
//            valorCelda = celda.getRichStringCellValue();
//            if(valorCelda.numFormattingRuns() > 1){//si tiene superscript
//                stCelda = getValor(valorCelda);
//                superScript = getSuperIndice(valorCelda);
//            }
//            else
//                stCelda = celda.getStringCellValue();
//        }catch(IllegalStateException e){
//            stCelda = String.valueOf((int)celda.getNumericCellValue());
//        }
//    }
    
    private static void procesarNoDefinida(Family familia, XSSFSheet hoja) {
        //las NO DEFINIDO tienen lo mismo que las DEFINIDO salvo en las mediciones
        //en este caso solo tienen MIC breakpoint (mg/L) S<= R>
        //el primer grupo empieza en A5
        //las notas están para cada grupo en D(f+2), A5 -> D7, A28 -> D30
    }

    private static String procesarString(String valor){
        String procesado = "";
        if(valor != null){
            procesado = valor.trim().replaceAll("\n", " ").replaceAll("\t", " ");
        }
        return procesado;
    }

    private static List<String> obtenerIndiceNotas(String notas) {
        List<String> lNotas = null;
        if(notas != null && !notas.isEmpty()){
            lNotas = new ArrayList<String>();
            if(notas.contains(",")){
                for(String nota: notas.split(",")){
                    lNotas.add(nota);
                }
            }else{
                lNotas.add(notas);
            }
        }
        return lNotas;
    }
    
    private enum Columnas {
        A, B, C, D, E, F, G, H, I
    }

    private static String getValor(XSSFRichTextString valor) {
        String val = "";
        for (int i = 0; i < valor.numFormattingRuns() - 1; i++) {
            int lenVal = valor.getLengthOfFormattingRun(i);
            int iVal = valor.getIndexOfFormattingRun(i);
            val += valor.toString().substring(iVal, lenVal + iVal);
        }
        return val;
    }
    
//    private static String getValorNotas(XSSFRichTextString valor) {
//        String val = "";
//        int lenVal = 0, iVal = 0;
//        boolean tachado = false;
//        for (int i = 0; i < valor.numFormattingRuns() - 1; i++) {
//            lenVal = valor.getLengthOfFormattingRun(i);
//            iVal = valor.getIndexOfFormattingRun(i);
//            if(!valor.getFontAtIndex(iVal).getStrikeout()){
//                val += valor.toString().substring(iVal, lenVal + iVal);
//            }else{
//                tachado = true;
//            }
//        }
//        if(tachado){
//            val += valor.toString().substring(iVal + 1, lenVal + iVal + 5);
//        }
//        return val;
//    }
    
    private static String getValoresNotasTachados(XSSFRichTextString valor){
        String val = "";
        int lenVal = 0, iVal = 0;
        for (int i = 0; i < valor.numFormattingRuns() - 1; i++) {
            lenVal = valor.getLengthOfFormattingRun(i);
            iVal = valor.getIndexOfFormattingRun(i);
            if(valor.getFontAtIndex(iVal).getStrikeout())
                val += valor.toString().substring(iVal, lenVal + iVal);
        }
        return val;
    }
    
    

    private static String getSuperIndice(XSSFRichTextString valor) {
        int lenSuper = valor.getLengthOfFormattingRun(valor.numFormattingRuns() - 1);
        int iSuper = valor.getIndexOfFormattingRun(valor.numFormattingRuns() - 1);
        return valor.toString().substring(iSuper, lenSuper + iSuper);
    }
}

//public static void main(String[] args) {
//        XSSFWorkbook excel = null;
//        try {
////            XSSFWorkbook excel = new XSSFWorkbook(new File("Prueba1.xlsx"));
//            excel = new XSSFWorkbook(new File("v_6.0_Breakpoint_table.xlsx"));
//            XSSFSheet hoja = excel.getSheetAt(4);
//            List<String> datos = new ArrayList<String>();
//
//            List<String> links = new ArrayList<String>();
//
//            //recorrido
//            Iterator filas = hoja.rowIterator();
//            while (filas.hasNext()) {
//                XSSFRow fila = (XSSFRow) filas.next();
//                Iterator celdas = fila.cellIterator();
//                while (celdas.hasNext()) {
//                    XSSFCell celda = (XSSFCell) celdas.next();
//                    System.out.print(celda.toString() + " || ");
//                    datos.add(String.valueOf(celda));
//                    Hyperlink linkAddress = celda.getHyperlink();
//                    if (linkAddress != null) {
//                        links.add(linkAddress.getAddress());
//                    }
//                }
//                System.out.println();
//            }
//            System.out.println(datos);
//            System.out.println(links);
//            System.out.println(hoja.getRow(0).getCell(0));
//            System.out.println(hoja.getRow(4).getCell(0));
//            System.out.println(hoja.getRow(6).getCell(0));
//            System.out.println(hoja.getRow(7).getCell(0));
//            System.out.println(hoja.getRow(7).getCell(1));//B8
//            System.out.println(getCelda(hoja, Columnas.B, 8));
//            
//            
//            // Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
////		switch(celda.getCellType()) {
////		case Cell.CELL_TYPE_NUMERIC:
////		    if( DateUtil.isCellDateFormatted(celda) ){
////		       System.out.println(celda.getDateCellValue());
////		    }else{
////		       System.out.println(celda.getNumericCellValue());
////		    }
////		    break;
////		case Cell.CELL_TYPE_STRING:
////		    System.out.println(celda.getStringCellValue());
////		    break;
////		case Cell.CELL_TYPE_BOOLEAN:
////		    System.out.println(celda.getBooleanCellValue());
////		    break;
////		}
//        } catch (IOException ex) {
//            Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidFormatException ex) {
//            Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (excel != null) {
//                try {
//                    excel.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(ExcelToXML.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
// Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
//		switch(celda.getCellType()) {
//		case Cell.CELL_TYPE_NUMERIC:
//		    if( DateUtil.isCellDateFormatted(celda) ){
//		       System.out.println(celda.getDateCellValue());
//		    }else{
//		       System.out.println(celda.getNumericCellValue());
//		    }
//		    break;
//		case Cell.CELL_TYPE_STRING:
//		    System.out.println(celda.getStringCellValue());
//		    break;
//		case Cell.CELL_TYPE_BOOLEAN:
//		    System.out.println(celda.getBooleanCellValue());
//		    break;
//		}
