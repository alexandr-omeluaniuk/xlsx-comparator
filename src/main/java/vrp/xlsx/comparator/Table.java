/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrp.xlsx.comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ss
 */
public class Table {
    private String name;
    private Map<String, List<String>> rows = new HashMap<>();
    private Map<String, Integer> rowsNum = new HashMap<>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the rows
     */
    public Map<String, List<String>> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(Map<String, List<String>> rows) {
        this.rows = rows;
    }

    /**
     * @return the rowsNum
     */
    public Map<String, Integer> getRowsNum() {
        return rowsNum;
    }

    /**
     * @param rowsNum the rowsNum to set
     */
    public void setRowsNum(Map<String, Integer> rowsNum) {
        this.rowsNum = rowsNum;
    }
}
