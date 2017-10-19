/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author ss
 */
public class TableDifferencePrinter {
    private static final String T_DS = "DS";
    private static final String T_STATE = "State";
    private static final int COLS = 9;
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    public static String print(Map<String, Table> tDS, Map<String, Table> tState) {
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
        // compare tables
        StringBuilder sb = new StringBuilder();
        Set<String> tNames = tDS.keySet();
        StringBuilder formatContent = new StringBuilder("%-7s |");
        formatContent.append("%7s. |");
        formatContent.append("%-50s |");
        for (int i = 0; i < 6; i++) {
            formatContent.append("%-10s |");
        }
        sb.append("#################################################\n");
        for (String tName : tNames) {
            Table tableDS = tDS.get(tName);
            Table tableState = tState.get(tName);
            sb.append("\t\t").append(tName).append("\n");
            Set<String> rowNames = new HashSet<>();
            rowNames.addAll(tableDS.getRows().keySet());
            rowNames.addAll(tableState.getRows().keySet());
            Map<Integer, String[]> sort = new TreeMap<>();
            String dsStr, stateStr;
            for (String rowName : rowNames) {
                dsStr = null;
                stateStr = null;
                if (tableDS.getRows().containsKey(rowName)
                        && tableState.getRows().containsKey(rowName)) {
                    // diff for two rows
                    List<String> dsRow = tableDS.getRows().get(rowName);
                    List<String> stateRow = tableState.getRows().get(rowName);
                    if (dsRow.size() != stateRow.size()) {
                        dsStr = ANSI_PURPLE + String.format(formatContent.toString(),
                                createRow(rowName, tableDS, T_DS).toArray(new Object[0])) + ANSI_RESET;
                        stateStr = ANSI_PURPLE + String.format(formatContent.toString(),
                                createRow(rowName, tableState, T_STATE).toArray(new Object[0])) + ANSI_RESET;
                    } else {
                        boolean isDiffer = false;
                        List<Object> argDS = new ArrayList<>();
                        List<Object> argState = new ArrayList<>();
                        for (int i = 0; i < dsRow.size(); i++) {
                            String dsCol = dsRow.get(i).trim();
                            String stateCol = stateRow.get(i).trim();
                            if (!dsCol.trim().equals(stateCol.trim())) {
                                isDiffer = true;
                                while (dsCol.length() < 10) {
                                    dsCol += " ";
                                }
                                dsCol = ANSI_YELLOW + dsCol + ANSI_RESET;
                                argDS.add(dsCol);
                                while (stateCol.length() < 10) {
                                    stateCol += " ";
                                }
                                stateCol = ANSI_YELLOW + stateCol + ANSI_RESET;
                                argState.add(stateCol);
                            } else {
                                argDS.add(dsCol);
                                argState.add(stateCol);
                            }
                        }
                        if (isDiffer) {
                            dsStr = String.format(formatContent.toString(),
                                    createRow(rowName, tableDS.getRowsNum().get(rowName), argDS, T_DS).toArray(new Object[0]));
                            stateStr = String.format(formatContent.toString(),
                                    createRow(rowName, tableState.getRowsNum().get(rowName), argState, T_STATE).toArray(new Object[0]));
                        }
                    }
                } else if (!tableDS.getRows().containsKey(rowName)) {
                    dsStr = ANSI_RED + String.format(formatContent.toString(),
                            createEmptyRow(T_DS).toArray(new Object[0])) + ANSI_RESET;
                    stateStr = String.format(formatContent.toString(),
                            createRow(rowName, tableState, T_STATE).toArray(new Object[0]));
                    
                } else if (!tableState.getRows().containsKey(rowName)) {
                    dsStr = String.format(formatContent.toString(),
                            createRow(rowName, tableDS, T_DS).toArray(new Object[0]));
                    stateStr = ANSI_RED + String.format(formatContent.toString(),
                            createEmptyRow(T_STATE).toArray(new Object[0])) + ANSI_RESET;
                } else {
                    throw new IllegalArgumentException("unreal case!!!");
                }
                Integer rowNum = tableDS.getRowsNum().get(rowName);
                if (rowNum == null) {
                    rowNum = tableState.getRowsNum().get(rowName);
                }
                if (dsStr != null && stateStr != null) {
                    sort.put(rowNum, new String[] {dsStr + "\n", stateStr + "\n"});
                }
            }
            for (Integer key : sort.keySet()) {
                sb.append(sort.get(key)[0]);
                sb.append(sort.get(key)[1]);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
    private static List<Object> createRow(String rowName, Table t, String type) {
        List<Object> strArgs = new ArrayList<>();
        strArgs.add(type);
        strArgs.add(t.getRowsNum().get(rowName));
        strArgs.add(rowName);
        for (String col : t.getRows().get(rowName)) {
            strArgs.add(col);
        }
        while (strArgs.size() < COLS) {
            strArgs.add("");
        }
        return strArgs;
    }
    private static List<Object> createRow(String rowName, Integer rowNum, List<Object> cols, String type) {
        List<Object> strArgs = new ArrayList<>();
        strArgs.add(type);
        strArgs.add(rowNum);
        strArgs.add(rowName);
        strArgs.addAll(cols);
        while (strArgs.size() < COLS) {
            strArgs.add("");
        }
        return strArgs;
    }
    private static List<Object> createEmptyRow(String type) {
        List<Object> strArgs = new ArrayList<>();
        strArgs.add(type);
        strArgs.add("----");
        while (strArgs.size() < COLS) {
            strArgs.add("");
        }
        return strArgs;
    }
}
