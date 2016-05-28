/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceltoxml;

import java.io.*;

import org.apache.poi.xssf.usermodel.*;

public class Prueba {

    public Prueba(String filename) throws FileNotFoundException,
            IOException {
        File file = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFRichTextString rts = null;
        XSSFFont font = null;
        int runLength = 0;
        int runIndex = 0;
        try {
            file = new File(filename);
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);

            fis.close();
            fis = null;

            sheet = workbook.getSheetAt(4);
            row = sheet.getRow(7);
            cell = row.getCell(1);

            rts = cell.getRichStringCellValue();

            if (rts.numFormattingRuns() > 1) {
                for (int i = 0; i < rts.numFormattingRuns(); i++) {

                    // Firstly pull out that piece of text the font relates to
                    runLength = rts.getLengthOfFormattingRun(i);
                    runIndex = rts.getIndexOfFormattingRun(i);
                    System.out.println(rts.toString().substring(runIndex,
                            (runIndex + runLength)));

                    // Now, get at the font information using the index
                    // number of the formatting run. Note that it appears as
                    // though if the default font is applied to the run
                    // then a null object will be returned for the font. Can
                    // handle this in the slightly ugly manner shown below.
                    try {
                        font = rts.getFontOfFormattingRun(i);
                    } catch (NullPointerException npe) {
                        // Create an XSSFFont object to 'stand in' for themissing
                        // default font object. Note that the superscriptproperty
                        // has to be set off manually. If this is not done then
                        // the font is regarded as being a superscripted one.
                        font = workbook.getFontAt(XSSFFont.DEFAULT_CHARSET);
                        font.setTypeOffset(XSSFFont.SS_NONE);
                    }
                    if (font.getTypeOffset() == XSSFFont.SS_SUPER) {
                        System.out.println("\tSuperscripted");
                    } else {
                        System.out.println("\tNOT Superscripted");
                    }
                }
            }
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }
}
