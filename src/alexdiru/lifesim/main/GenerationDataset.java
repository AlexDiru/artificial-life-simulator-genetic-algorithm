package alexdiru.lifesim.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 29/11/13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class GenerationDataset extends GrowthOverGenerationsDataset implements Serializable {

    private List<Double> maxFitness;
    private List<Double> minFitness;
    private List<Double> avgFitness;
    private List<Double> varFitness;
    private List<Integer> totalChromosomes;
    private List<Integer> sameChromosomes;

    public GenerationDataset() {
        super();
        reset();
    }

    public void add(double minFitness, double avgFitness, double maxFitness, double varFitness, int totalChromosomes, int sameChromosomes) {
        this.minFitness.add(minFitness);
        this.maxFitness.add(maxFitness);
        this.avgFitness.add(avgFitness);
        this.varFitness.add(varFitness);
        this.totalChromosomes.add(totalChromosomes);
        this.sameChromosomes.add(sameChromosomes);
        addGeneration(minFitness, avgFitness, maxFitness);
    }

    public void reset() {
        super.reset();
        minFitness = new ArrayList<Double>();
        maxFitness = new ArrayList<Double>();
        avgFitness = new ArrayList<Double>();
        varFitness = new ArrayList<Double>();
        totalChromosomes = new ArrayList<Integer>();
        sameChromosomes = new ArrayList<Integer>();
    }

    public JTable createTable() {
         JTable table = new JTable();
        addRows(table);
        return table;
    }

    public void addRows(JTable table) {
        String[] columnNames = { "Generation", "Minimum Fitness", "Maximum Fitness", "Average Fitness", "Variance Fitness", "Total Chromosomes", "Unique Chromosomes" };
        ((DefaultTableModel)table.getModel()).setColumnIdentifiers(columnNames);

        while (table.getRowCount() > 0)
            ((DefaultTableModel)table.getModel()).removeRow(table.getRowCount() - 1);

        Object[][] rows = getRows();

        for (int i = 0; i < rows.length; i++)
            ((DefaultTableModel)table.getModel()).addRow(rows[i]);

        adjustColumnPreferredWidths(table);

        ((DefaultTableModel)table.getModel()).fireTableDataChanged();
    }

    public static void adjustColumnPreferredWidths(JTable table) {
        if (table == null || table.getColumnCount() == 0) {
            return;
        }

        // strategy - get max width for cells in column and
        // make that the preferred width
        TableColumnModel columnModel = table.getColumnModel();

        for (int col = 0; col < table.getColumnCount(); col++) {
            try {
                int maxwidth = 0;
                for (int row = 0; row < table.getRowCount(); row++) {
                    TableCellRenderer rend = table.getCellRenderer(row, col);
                    Object value = table.getValueAt(row, col);
                    Component comp = rend.getTableCellRendererComponent(table, value, false, false, row, col);
                    maxwidth = Math.max(comp.getPreferredSize().width, maxwidth);
                }

                TableColumn column = columnModel.getColumn(col);
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    headerRenderer = table.getTableHeader().getDefaultRenderer();
                }
                Object headerValue = column.getHeaderValue();
                Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, col);
                maxwidth = Math.max(maxwidth, headerComp.getPreferredSize().width);

                column.setPreferredWidth(maxwidth);

            } catch (Exception e) {
            }
        }
    }

    private Object[][] getRows() {
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int i = 0; i < minFitness.size(); i++)
            dataList.add(new Object[] { (i + 1), minFitness.get(i), maxFitness.get(i), avgFitness.get(i), varFitness.get(i), totalChromosomes.get(i), sameChromosomes.get(i) });
        return dataList.toArray(new Object[dataList.size()][]);
    }
}
