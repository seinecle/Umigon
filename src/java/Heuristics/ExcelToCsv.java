/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author C. Levallois
 */
public class ExcelToCsv {

    private static BufferedWriter bw;

    public static void echoAsCSV(Sheet sheet) throws IOException {
        Row row;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                bw.write("" + row.getCell(j) + "\t");
            }
            bw.newLine();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        InputStream inp;
        inp = new FileInputStream("private/heuristics.xlsx");
        Workbook wb = WorkbookFactory.create(inp);

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            bw = new BufferedWriter(new FileWriter("private/heuristics/"+ wb.getSheetAt(i).getSheetName()+".txt"));
            System.out.println(wb.getSheetAt(i).getSheetName());
            echoAsCSV(wb.getSheetAt(i));
            bw.close();
        }
        inp.close();
    }
}
