package alexdiru.lifesim.swingcomp;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JButton;

public class CreateNewSimulationPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public CreateNewSimulationPanel() {
		
		JLabel lblNewLabel = new JLabel("Create New Simulation");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		add(lblNewLabel);
		
		JLabel lblSimulationsPerGeneration = new JLabel("Simulations Per Generation:");
		add(lblSimulationsPerGeneration);
		
		JSpinner spinner = new JSpinner();
		add(spinner);
		
		JButton btnCreate = new JButton("Create");
		add(btnCreate);

	}

}
