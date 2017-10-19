/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    public static void print(Map<String, Table> tDS, Map<String, Table> tState) {
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
        Set<String> tNames = tDS.keySet();
//        Map<String, List<String>> notFoundDS = new HashMap<>();
//        Map<String, List<String>> notFoundState = new HashMap<>();
//        for (String tName : tNames) {
//            Table sTable = source.get(tName);
//            Table tTable = target.get(tName);
//            //System.out.println("");
//            //System.out.println("************ " + tName);
//            for (String rowName : sTable.getRows().keySet()) {
//                if (!tTable.getRows().keySet().contains(rowName)) {
//                    if (!notFoundState.containsKey(tName)) {
//                        notFoundState.put(tName, new ArrayList<>());
//                    }
//                    notFoundState.get(tName).add(rowName);
//                    //System.out.println("'State' table, row not found: " + rowName);
//                }
//            }
//            for (String rowName : tTable.getRows().keySet()) {
//                if (!sTable.getRows().keySet().contains(rowName)) {
//                    if (!notFoundDS.containsKey(tName)) {
//                        notFoundDS.put(tName, new ArrayList<>());
//                    }
//                    notFoundDS.get(tName).add(rowName);
//                    //System.out.println("'DS', row not found: " + rowName);
//                }
//            }
//        }
//        StringBuilder formatContent = new StringBuilder("%7s. |");
//        formatContent.append("%-40s |");
//        for (int i = 0; i < 6; i++) {
//            formatContent.append("%-10s |");
//        }
//        System.out.println("#################################################");
//        System.out.println("'DS' file hasn't rows from 'State' file");
//        for (String tName : notFoundDS.keySet()) {
//            System.out.println("");
//            System.out.println("\t\t" + tName);
//            for (String rowName : notFoundDS.get(tName)) {
//                Table t = target.get(tName);
//                List<Object> strArgs = new ArrayList<>();
//                strArgs.add(t.getRowsNum().get(rowName));
//                strArgs.add(rowName);
//                for (String col : t.getRows().get(rowName)) {
//                    strArgs.add(col);
//                }
//                while (strArgs.size() < 8) {
//                    strArgs.add("");
//                }
//                System.out.println(String.format(formatContent.toString(),
//                        strArgs.toArray(new Object[0])));
//            }
//        }
//        System.out.println("#################################################");
//        System.out.println("'State' file hasn't rows from 'DS' file");
//        for (String tName : notFoundState.keySet()) {
//            System.out.println("");
//            System.out.println("\t\t" + tName);
//            for (String rowName : notFoundState.get(tName)) {
//                Table t = source.get(tName);
//                List<Object> strArgs = new ArrayList<>();
//                strArgs.add(t.getRowsNum().get(rowName));
//                strArgs.add(rowName);
//                for (String col : t.getRows().get(rowName)) {
//                    strArgs.add(col);
//                }
//                while (strArgs.size() < 8) {
//                    strArgs.add("");
//                }
//                System.out.println(String.format(formatContent.toString(),
//                        strArgs.toArray(new Object[0])));
//            }
//        }
        // compare exist
        StringBuilder formatContent = new StringBuilder("%-7s |");
        formatContent.append("%7s. |");
        formatContent.append("%-50s |");
        for (int i = 0; i < 6; i++) {
            formatContent.append("%-10s |");
        }
        System.out.println("#################################################");
        for (String tName : tNames) {
            Table tableDS = tDS.get(tName);
            Table tableState = tState.get(tName);
            System.out.println("\t\t" + tName);
            Set<String> rowNames = new HashSet<>();
            rowNames.addAll(tableDS.getRows().keySet());
            rowNames.addAll(tableState.getRows().keySet());
            for (String rowName : rowNames) {
                if (tableDS.getRows().containsKey(rowName)
                        && tableState.getRows().containsKey(rowName)) {
                    // diff for two rows
                    List<String> dsRow = tableDS.getRows().get(rowName);
                    List<String> stateRow = tableState.getRows().get(rowName);
                    if (dsRow.size() != stateRow.size()) {
                        System.out.println(ANSI_PURPLE + String.format(formatContent.toString(),
                                createRow(rowName, tableDS, T_DS).toArray(new Object[0])) + ANSI_RESET);
                        System.out.println(ANSI_PURPLE + String.format(formatContent.toString(),
                                createRow(rowName, tableState, T_STATE).toArray(new Object[0])) + ANSI_RESET);
                    } else {
                        boolean isDiffer = false;
                        List<Object> argDS = new ArrayList<>();
                        List<Object> argState = new ArrayList<>();
                        for (int i = 0; i < dsRow.size(); i++) {
                            String dsCol = dsRow.get(i).trim();
                            String stateCol = stateRow.get(i).trim();
                            if (!dsCol.trim().equals(stateCol.trim())) {
                                isDiffer = true;
                                argDS.add(ANSI_YELLOW + dsCol + ANSI_RESET);
                                argState.add(ANSI_YELLOW + stateCol + ANSI_RESET);
                            } else {
                                argDS.add(dsCol);
                                argState.add(stateCol);
                            }
                        }
                        if (isDiffer) {
                            System.out.println(String.format(formatContent.toString(),
                                    createRow(rowName, tableDS.getRowsNum().get(rowName), argDS, T_DS).toArray(new Object[0])));
                            System.out.println(String.format(formatContent.toString(),
                                    createRow(rowName, tableState.getRowsNum().get(rowName), argState, T_STATE).toArray(new Object[0])));
                        }
                    }
                } else if (!tableDS.getRows().containsKey(rowName)) {
                    System.out.println(ANSI_RED + String.format(formatContent.toString(),
                            createEmptyRow(T_DS).toArray(new Object[0])) + ANSI_RESET);
                    System.out.println(String.format(formatContent.toString(),
                            createRow(rowName, tableState, T_STATE).toArray(new Object[0])));
                    
                } else if (!tableState.getRows().containsKey(rowName)) {
                    System.out.println(String.format(formatContent.toString(),
                            createRow(rowName, tableDS, T_DS).toArray(new Object[0])));
                    System.out.println(ANSI_RED + String.format(formatContent.toString(),
                            createEmptyRow(T_STATE).toArray(new Object[0])) + ANSI_RESET);
                }
            }
            System.out.println("");
            System.out.println("");
        }
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
        strArgs.add("X");
        while (strArgs.size() < COLS) {
            strArgs.add("");
        }
        return strArgs;
    }
}
