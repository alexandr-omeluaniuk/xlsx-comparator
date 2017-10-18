/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ss
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            compare();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void compare() throws Exception {
        System.out.println("=============== DS ==============================");
        Map<String, Table> mapDS = new TableExtractor()
                .extract("DS staff.xlsx");
        System.out.println("");
        System.out.println("=============== STATE ===========================");
        Map<String, Table> mapState = new TableExtractor()
                .extract("States Staff.xlsx");
        TableDifferencePrinter.print(mapDS, mapState);
    }
}
