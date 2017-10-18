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
