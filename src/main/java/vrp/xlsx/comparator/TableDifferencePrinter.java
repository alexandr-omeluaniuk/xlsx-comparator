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
import java.util.Set;

/**
 *
 * @author ss
 */
public class TableDifferencePrinter {
    public static void print(Map<String, Table> source, Map<String, Table> target) {
        for (String sKey : source.keySet()) {
            if (!target.keySet().contains(sKey)) {
                System.out.println("!!! 'State' file not contains [" + sKey + "] table");
            }
        }
        for (String tKey : target.keySet()) {
            if (!source.keySet().contains(tKey)) {
                System.out.println("!!! 'DS' file not contains [" + tKey + "] table");
            }
        }
        // compare tables
        Map<String, List<String>> notFoundDS = new HashMap<>();
        Map<String, List<String>> notFoundState = new HashMap<>();
        Set<String> tNames = source.keySet();
        for (String tName : tNames) {
            Table sTable = source.get(tName);
            Table tTable = target.get(tName);
            //System.out.println("");
            //System.out.println("************ " + tName);
            for (String rowName : sTable.getRows().keySet()) {
                if (!tTable.getRows().keySet().contains(rowName)) {
                    if (!notFoundState.containsKey(tName)) {
                        notFoundState.put(tName, new ArrayList<>());
                    }
                    notFoundState.get(tName).add(rowName);
                    //System.out.println("'State' table, row not found: " + rowName);
                }
            }
            for (String rowName : tTable.getRows().keySet()) {
                if (!sTable.getRows().keySet().contains(rowName)) {
                    if (!notFoundDS.containsKey(tName)) {
                        notFoundDS.put(tName, new ArrayList<>());
                    }
                    notFoundDS.get(tName).add(rowName);
                    //System.out.println("'DS', row not found: " + rowName);
                }
            }
        }
        StringBuilder formatContent = new StringBuilder("%7s. |");
        formatContent.append("%-40s |");
        for (int i = 0; i < 6; i++) {
            formatContent.append("%-10s |");
        }
        System.out.println("#################################################");
        System.out.println("'DS' file hasn't rows from 'State' file");
        for (String tName : notFoundDS.keySet()) {
            System.out.println("");
            System.out.println("\t\t" + tName);
            for (String rowName : notFoundDS.get(tName)) {
                Table t = target.get(tName);
                List<Object> strArgs = new ArrayList<>();
                strArgs.add(t.getRowsNum().get(rowName));
                strArgs.add(rowName);
                for (String col : t.getRows().get(rowName)) {
                    strArgs.add(col);
                }
                while (strArgs.size() < 8) {
                    strArgs.add("");
                }
                System.out.println(String.format(formatContent.toString(),
                        strArgs.toArray(new Object[0])));
            }
        }
        System.out.println("#################################################");
        System.out.println("'State' file hasn't rows from 'DS' file");
        for (String tName : notFoundState.keySet()) {
            System.out.println("");
            System.out.println("\t\t" + tName);
            for (String rowName : notFoundState.get(tName)) {
                Table t = source.get(tName);
                List<Object> strArgs = new ArrayList<>();
                strArgs.add(t.getRowsNum().get(rowName));
                strArgs.add(rowName);
                for (String col : t.getRows().get(rowName)) {
                    strArgs.add(col);
                }
                while (strArgs.size() < 8) {
                    strArgs.add("");
                }
                System.out.println(String.format(formatContent.toString(),
                        strArgs.toArray(new Object[0])));
            }
        }
    }
}
