package alexdiru.lifesim.swingcomp;

import alexdiru.lifesim.datamining.CustomTreeComparison;
import alexdiru.lifesim.datamining.EditDistance;
import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.GeneManager;
import org.apache.commons.io.FileUtils;
import org.jgap.Gene;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GenotypeComparerPanel extends JPanel {

    private JTextField genotypeFileTextBox;
    private HashMap<String, Gene[]> chromosomeMap = new HashMap<String, Gene[]>();
    private JList aList;
    private JList bList;
    private DefaultListModel listModel;
    private JPanel aPanel;
    private JPanel bPanel;
    private JLabel editDistanceLabel;
    private JLabel customDistanceLabel;

	/**
	 * Create the panel.
	 */
	public GenotypeComparerPanel() {
		setLayout(null);
		
		JLabel lblGenotypeFile = new JLabel("Genotype File:");
		lblGenotypeFile.setBounds(207, 9, 70, 14);
		add(lblGenotypeFile);
		
		genotypeFileTextBox = new JTextField("C:\\Users\\Alex\\Dropbox\\EclipseWorkspace\\life-simulation\\SimulationData\\Simulation20140219142724064\\chrom210.txt");
		genotypeFileTextBox.setBounds(282, 6, 206, 20);
		add(genotypeFileTextBox);
		genotypeFileTextBox.setColumns(25);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(493, 5, 67, 23);
		add(btnBrowse);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGenotypes();
			}
		});
		btnLoad.setBounds(565, 5, 55, 23);
		add(btnLoad);

        listModel = new DefaultListModel();
		
		bList = new JList(listModel);
		JScrollPane scrollPane = new JScrollPane(bList);
		scrollPane.setBounds(658, 41, 160, 228);
		add(scrollPane);

        aList = new JList(listModel);
        JScrollPane scrollPaneA = new JScrollPane(aList);
        scrollPaneA.setBounds(10, 41, 158, 226);
        add(scrollPaneA);

		aPanel = new JPanel();
		aPanel.setBounds(10, 278, 349, 456);
		add(aPanel);
		
		bPanel = new JPanel();
		bPanel.setBounds(469, 280, 349, 454);
		add(bPanel);
		
		JButton btnCompare = new JButton("Compare");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    compareGenotypes();
            }
		});
		btnCompare.setBounds(341, 94, 147, 109);
		add(btnCompare);

        editDistanceLabel = new JLabel("Edit Distance: ");
        editDistanceLabel.setBounds(341,210,147,25);
        add(editDistanceLabel);

        customDistanceLabel = new JLabel("Custom Distance: ");
        customDistanceLabel.setBounds(341,235,147,25);
        add(customDistanceLabel);

	}
	
	private void loadGenotypes() {
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(genotypeFileTextBox.getText()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (String line : lines) {
            String[] cells = line.split(",");

            String name = cells[0];

            chromosomeMap.put(name, GeneManager.readGenes(cells));
        }

        //Fill the lists with the names
        List<String> names = new ArrayList<String>();
        names.addAll(chromosomeMap.keySet());
        Collections.sort(names);

        for (String name : names)
            listModel.addElement(name);
    }

    /**
     * Shows the genotypes in the tree model of the two selected
     */
    public void compareGenotypes() {
        try {
            aPanel.removeAll();
            bPanel.removeAll();

            LifeFormTree aTree = new LifeFormTree();
            LifeFormTree bTree = new LifeFormTree();

            Gene[] aGenes = chromosomeMap.get(aList.getSelectedValue());
            Gene[] bGenes = chromosomeMap.get(bList.getSelectedValue());

            CATBehaviourTree aBehaviourTree = CATBehaviourTree.createFromGenes(null, aGenes);
            CATBehaviourTree bBehaviourTree = CATBehaviourTree.createFromGenes(null, bGenes);

            aTree.setTree(GeneManager.createTreeModel(aBehaviourTree, aGenes));
            bTree.setTree(GeneManager.createTreeModel(bBehaviourTree, bGenes));

            aPanel.add(aTree);
            bPanel.add(bTree);

            aPanel.revalidate();
            aPanel.repaint();
            bPanel.revalidate();
            bPanel.repaint();

            editDistanceLabel.setText("Edit Distance: " + EditDistance.compute(aBehaviourTree, bBehaviourTree));
            customDistanceLabel.setText("Custom Distance: " + CustomTreeComparison.compute(aGenes, bGenes));

        } catch (NullPointerException ex) {
        }
    }
}