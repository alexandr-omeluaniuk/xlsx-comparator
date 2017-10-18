/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.HashMap;
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
        Workbook wb = new XSSFWorkbook(Main.class
                .getResourceAsStream("/DS staff.xlsx"));
        Sheet sheet = wb.getSheetAt(0);
        System.out.println("Sheet name [" + sheet.getSheetName() + "]");
        int rowNumFirst = sheet.getFirstRowNum();
        int rowNumLast = sheet.getLastRowNum();
        System.out.println("first row number [" + rowNumFirst
                + "], last row number [" + rowNumLast + "]");
        Integer totalTables = 0;
        Map<String, Table> tables = new HashMap<>();
        Table curTable = new Table();
        for (int i = rowNumFirst; i <= rowNumLast; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                addTable(curTable, tables);
                totalTables++;
                curTable = new Table();
                continue;
            }
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    sb.append(cell.getStringCellValue()).append(" :: ");
                }
            }
            if (sb.toString().trim().isEmpty()) {
                addTable(curTable, tables);
                totalTables++;
                curTable = new Table();
            } else {
                if (curTable.getName() == null) {
                    curTable.setName(sb.toString());
                } else {
                    System.out.println(sb.toString());
                }
            }
            if ("lmsilt__vILT_Account__c :: true :: true :: true :: true :: true :: true :: ".equals(sb.toString())) {
                System.err.println("");
            }
        }
        System.out.println("TOTAL TABLES [" + totalTables + "]");
        for (String tName : tables.keySet()) {
            System.out.println(tName);
        }
    }
    private static void addTable(Table curTable, Map<String, Table> tables) {
        if (curTable.getName() != null) {
            tables.put(curTable.getName(), curTable);
        }
        System.out.println("");
        System.out.println("=========================================");
        System.out.println("");
    }
}
