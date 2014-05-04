package alexdiru.lifesim.swingcomp;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alexdiru.lifesim.main.LifeForm;

public class LifeFormBehaviourPanel extends JPanel {
	private JTextField textFieldBehaviourFile;

	/**
	 * Create the panel.
	 */
	public LifeFormBehaviourPanel(LifeForm lifeForm) {
		JLabel lblNewLabel = new JLabel("Life Form Behaviour");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		add(lblNewLabel);
		
		JLabel lblLoadFromFile = new JLabel("Load From File:");
		add(lblLoadFromFile);
		
		textFieldBehaviourFile = new JTextField();
		add(textFieldBehaviourFile);
		textFieldBehaviourFile.setColumns(10);
		
		JButton btnBehaviourFileBrowse = new JButton("Browse");
		add(btnBehaviourFileBrowse);
		
		JButton btnBehaviourFileLoad = new JButton("Load");
		add(btnBehaviourFileLoad);
		
		JLabel lblBehaviour = new JLabel("");//lifeForm.getGPProgram().getChromosome(0).toStringNorm(0));
		add(lblBehaviour);
	}
}