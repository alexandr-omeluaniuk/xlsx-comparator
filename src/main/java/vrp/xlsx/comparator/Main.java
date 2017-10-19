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
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static vrp.xlsx.comparator.TableDifferencePrinter.ANSI_PURPLE;
import static vrp.xlsx.comparator.TableDifferencePrinter.ANSI_RED;
import static vrp.xlsx.comparator.TableDifferencePrinter.ANSI_RESET;
import static vrp.xlsx.comparator.TableDifferencePrinter.ANSI_YELLOW;

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
            compare(args.length > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void compare(boolean isStaff) throws Exception {
        String ds, state;
        if (isStaff) {
            ds = "DS staff.xlsx";
            state = "States Staff.xlsx";
        } else {
            ds = "DS Volunteer.xlsx";
            state = "States Volunteer.xlsx";
        }
        System.out.println("STATE FILE [" + state + "]");
        System.out.println("DS FILE [" + ds + "]");
        System.out.println("=============== DS ==============================");
        XSSFWorkbook wbDS = new XSSFWorkbook(Main.class
                .getResourceAsStream("/" + ds));
        Map<String, Table> mapDS = new TableExtractor()
                .extract(wbDS);
        System.out.println("");
        System.out.println("=============== STATE ===========================");
        XSSFWorkbook wbState = new XSSFWorkbook(Main.class
                .getResourceAsStream("/" + state));
        Map<String, Table> mapState = new TableExtractor()
                .extract(wbState);
        String content = TableDifferencePrinter.print(mapDS, mapState);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File((isStaff ? "staff" : "volunteer") + "-diff.html")), "UTF-8"));
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        TableDifferenceExchenger.readChanges(mapDS, mapState, wbState);
    }
}
