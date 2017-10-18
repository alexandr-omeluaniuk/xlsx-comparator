/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.Map;

/**
 *
 * @author ss
 */
public class TableDifferencePrinter {
    public static void print(Map<String, Table> source, Map<String, Table> target) {
        for (String sKey : source.keySet()) {
            if (!target.keySet().contains(sKey)) {
                System.out.println("!!! target table not contains [" + sKey + "]");
            }
        }
        for (String tKey : target.keySet()) {
            if (!source.keySet().contains(tKey)) {
                System.out.println("!!! source table not contains [" + tKey + "]");
            }
        }
        // compare tables
        
    }
}
