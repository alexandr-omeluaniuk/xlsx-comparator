/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ss
 */
public class TableDifferenceExchenger {
    public static void readChanges(Map<String, Table> tDS,
            Map<String, Table> tState, XSSFWorkbook wb) throws Exception {
        for (String sKey : tDS.keySet()) {
            if (!tState.keySet().contains(sKey)) {
                System.out.println("!!! 'State' file not contains [" + sKey + "] table");
            }
        }
        for (String tKey : tState.keySet()) {
            if (!tDS.keySet().contains(tKey)) {
                System.out.println("!!! 'DS' file not contains [" + tKey + "] table");
            }
        }
        Set<String> tNames = tDS.keySet();
        int shift = 0;
        for (String tName : tNames) {
            if (tName.contains(":: Profile Name :: LMS Admin/Participant :: :: :: :: :: :: :: :: ::")
                    || tName.contains(":: :: LMS Admin/Participant :: :: :: :: :: ::")) {
                continue;
            }
            Table tableDS = tDS.get(tName);
            Table tableState = tState.get(tName);
            Set<String> rowNames = new HashSet<>();
            rowNames.addAll(tableDS.getRows().keySet());
            rowNames.addAll(tableState.getRows().keySet());
            for (String rowName : rowNames) {
                if (tableDS.getRows().containsKey(rowName)
                        && tableState.getRows().containsKey(rowName)) {     // both has row
                    // diff for two rows
                    Integer dsNum = tableDS.getRowsNum().get(rowName);
                    Integer stateNum = tableState.getRowsNum().get(rowName);
                    Row originDS = tableDS.getOriginRows().get(dsNum);
                    Row originState = tableState.getOriginRows().get(stateNum);
                    if (originDS.getLastCellNum() != originState.getLastCellNum()) {
                        throw new IllegalArgumentException("Special case, handle later!");
                    } else {
                        for (int i = 0; i < originState.getLastCellNum(); i++) {
                            Cell dsCell = originDS.getCell(i);
                            Cell stateCell = originState.getCell(i);
                            String dsCol = "";
                            String stateCol = "";
                            if (dsCell != null) {
                                dsCol = dsCell.getStringCellValue();
                            }
                            if (stateCell != null) {
                                stateCol = stateCell.getStringCellValue();
                            }
                            if (!dsCol.trim().equals(stateCol.trim())) {
                                stateCell.setCellValue(stateCol + " [" + dsCol + "]");
                                XSSFCellStyle style = wb.createCellStyle();
                                style.cloneStyleFrom(stateCell.getCellStyle());
                                style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 130, 130)));
                                style.setWrapText(true);
                                stateCell.setCellStyle(style);
                            }
                        }
                    }
                } else if (!tableDS.getRows().containsKey(rowName)) {   // DS has't row
                    Row originState = tableState.getOriginRows().get(tableState.getRowsNum().get(rowName));
                    for (int i = 0; i < originState.getLastCellNum(); i++) {
                        Cell cell = originState.getCell(i);
                        if (cell != null) {
                            XSSFCellStyle style = wb.createCellStyle();
                            style.cloneStyleFrom(cell.getCellStyle());
                            style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 27)));
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                        }
                    }
                } else if (!tableState.getRows().containsKey(rowName)) {    // State has't row
                    Sheet sheet = wb.getSheetAt(0);
                    Row originDS = tableDS.getOriginRows().get(tableDS.getRowsNum().get(rowName));
                    sheet.shiftRows(tableState.getLastTableNum() + shift, sheet.getLastRowNum(), 1);
                    System.out.println("******************* " + tableState.getName() + " | " + (tableState.getLastTableNum() + shift));
                    Row originState = sheet.getRow(tableState.getLastTableNum() + shift - 1);
                    if (originState == null) {
                        originState = sheet.createRow(tableState.getLastTableNum() + shift - 1);
                    }
                    for (int i = 0; i < originDS.getLastCellNum(); i++) {
                        Cell cell = originDS.getCell(i);
                        if (cell != null) {
                            originState.createCell(i);
                            originState.getCell(i).setCellValue(cell.getStringCellValue());
                            
                        }
                    }
                    shift++;
                } else {
                    throw new IllegalArgumentException("unreal case!!!");
                }
            }
        }
        FileOutputStream output =new FileOutputStream(new File(wb.getSheetName(0) + "_correct.xlsx"));
        wb.write(output);
        output.close();
    }
}
