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
            while (iSheet.hasNext()) {
                XSSFSheet hoja = (XSSFSheet) iSheet.next();
                if (!hoja.getSheetName().equals("Content") && !hoja.getSheetName().equals("Notes")
                        && !hoja.getSheetName().equals("Guidance") && !hoja.getSheetName().equals("Changes")) {
//                    System.out.println(hoja.getSheetName());
                    if (hoja.getSheetName().equals("Dosages")) {
                        extraerDosages(dosages, hoja);
                    } /*else if(hoja.getSheetName().equals("Dosages")){
                        
                    }*/else {
                        Family familia = extraerFamily(hoja);
                        if (familia != null && !familia.getGroups().isEmpty()) {
                            families.add(familia);
                        }
                    }
                }
            }
            documento.setDosages(dosages);
            documento.setFamilies(families);
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
        boolean vengoDeGrupo = false;
        Group grupo = new Group();
        ArrayList<AntimicrobialAgent> micros = new ArrayList<AntimicrobialAgent>();
        DosagesAntimicrobialAgent medic;
        while (iFila.hasNext()) {
            XSSFRow fila = (XSSFRow) iFila.next();
            if (fila != null) {//al haber hecho el do-while es posible que el next llege al final antes de comprobar
                if (idxF >= 4) {
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

                        if (fuente.getFontHeightInPoints() == grupoS) {
                            if (!vengoDeGrupo) {
                                if (!micros.isEmpty()) {
                                    grupo.setAntimicrobialAgents(micros);
                                    micros = new ArrayList<AntimicrobialAgent>();
                                    grupos.add(grupo);
                                }
                                grupo = new Group(celdaNombre.getStringCellValue().trim());
                                vengoDeGrupo = true;
                            }
                        }
                        if (fuente.getFontHeightInPoints() == medicS && celdaNombre.getStringCellValue().trim().length() > 0) {
                            medic = new DosagesAntimicrobialAgent(celdaNombre.getStringCellValue().trim());
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
        boolean definido = false;
        XSSFCell celda = getCelda(hoja, Columnas.G, 3);
        if (celda != null) {
            definido = celda.getStringCellValue().trim().contains("Disk diffusion");
        }
        String familiName = getCelda(hoja, Columnas.A, 1).getStringCellValue().trim();
        if(definido){
            System.out.println(procesarString(familiName) + ": DEFINIDO");
        }else{
            System.out.println(procesarString(familiName) + ": NO DEFINIDO");
        }
        return familia;
    }

    private static String procesarString(String valor){
        String procesado = "";
        if(valor != null){
            procesado = valor.trim().replaceAll("\n", " ").replaceAll("\t", " ");
        }
        return procesado;
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
