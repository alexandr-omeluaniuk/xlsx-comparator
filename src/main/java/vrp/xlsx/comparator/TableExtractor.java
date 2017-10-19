/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class TableExtractor {
    public Map<String, Table> extract(Workbook wb) throws Exception {
        Sheet sheet = wb.getSheetAt(0);
        System.out.println("Sheet name [" + sheet.getSheetName() + "]");
        int rowNumFirst = sheet.getFirstRowNum();
        int rowNumLast = sheet.getLastRowNum();
        System.out.println("first row number [" + rowNumFirst
                + "], last row number [" + rowNumLast + "]");
        Integer totalTables = 0;
        Map<String, Table> tables = new LinkedHashMap<>();
        Table curTable = new Table();
        for (int i = rowNumFirst; i <= rowNumLast; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                curTable.setLastTableNum(i + 1);
                addTable(curTable, tables);
                totalTables++;
                curTable = new Table();
                continue;
            }
            StringBuilder sb = new StringBuilder();
            List<String> rowContent = new ArrayList<>();
            String rowName = null;
            boolean skipRow = false;
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                String val;
                if (cell != null) {
                    val = cell.getStringCellValue().trim();
                } else {
                    val = "";
                }
                if ("We can download more detail about FLS from Object Permission file.".equals(val.trim())) {
                    skipRow = true;
                    break;
                }
                sb.append(val).append(" :: ");
                if (rowName == null && !val.trim().isEmpty()) {
                    rowName = val;
                } else {
                    rowContent.add(val);
                }
            }
            if (skipRow) {
                continue;
            }
            if (sb.toString().trim().isEmpty()
                    || (sb.toString().contains("Field Name")
                    && sb.toString().contains("Readable")
                    && sb.toString().contains("Editable"))) {
                curTable.setLastTableNum(i + 1);
                addTable(curTable, tables);
                totalTables++;
                curTable = new Table();
                if (sb.toString().contains("Field Name")
                    && sb.toString().contains("Readable")
                    && sb.toString().contains("Editable")) {
                    curTable.setName(sb.toString());
                }
            } else {
                if (curTable.getName() == null) {
                    curTable.setName(sb.toString()
                            .replace("Driver Safety Staff - ", "")
                            .replace("States Staff - ", "")
                            .replace("Driver Safety Volunteer - LMS Admin", "LMS Admin/Participant")
                            .replace("States Volunteer - LMS Participant", "LMS Admin/Participant")
                    );
                } else {
                    //System.out.println(sb.toString());
                }
                if (rowName == null) {
                    System.out.println("!!!");
                } else {
                    curTable.getRows().put(rowName, rowContent);
                    curTable.getRowsNum().put(rowName, i + 1);
                    curTable.getOriginRows().put(i + 1, row);
                }
            }
        }
        System.out.println("TOTAL TABLES [" + totalTables + "]");
        for (String tName : tables.keySet()) {
            System.out.println("Table name: " + tName);
            System.out.println("Table rows: " + tables.get(tName).getRows().size());
            System.out.println("");
        }
        return tables;
    }
    private void addTable(Table curTable, Map<String, Table> tables) {
        if (curTable.getName() != null) {
            tables.put(curTable.getName(), curTable);
        }
//        System.out.println("");
//        System.out.println("=========================================");
//        System.out.println("");
    }
}
