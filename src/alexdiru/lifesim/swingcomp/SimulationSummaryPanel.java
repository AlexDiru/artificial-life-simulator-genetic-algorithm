package alexdiru.lifesim.swingcomp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import alexdiru.lifesim.enums.GraphType;
import alexdiru.lifesim.main.Evolver;
import alexdiru.lifesim.main.GUI;
import alexdiru.lifesim.main.LifeForm;
import alexdiru.lifesim.main.World;

public class SimulationSummaryPanel extends JPanel {

    private Evolver evolver;
    private GUI gui;
    private JTable table;

	/**
	 * Create the panel.
	 */
	public SimulationSummaryPanel(final GUI gui, final Evolver evolver) {
		this.evolver = evolver;
        this.gui = gui;

        table = evolver.getDataset().createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public void refreshTable() {
        evolver.getDataset().addRows(table);
    }

}