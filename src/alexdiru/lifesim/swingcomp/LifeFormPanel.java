package alexdiru.lifesim.swingcomp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import alexdiru.lifesim.main.LibGDXApplicationListener;
import alexdiru.lifesim.main.LifeForm;
import alexdiru.lifesim.main.GUI;

public class LifeFormPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	
	//Used to link the buttons into the simulation
	private LibGDXApplicationListener applicationListener;
	
	//Used to open a new JFrame for the Show Behaviour
	private GUI gui;
	
	//Used to update the labels of the panel
	private LifeForm lifeForm;
	
	//Makes sure only one frame created by Show Behaviour can be active at the same time
	private static JFrame showBehaviourFrame = null;
	
	//Labels of the life forms statistics
	JLabel lblNameData = new JLabel("NAME DATA");
	JLabel lblHealthData = new JLabel("HEALTH DATA");
	JLabel lblPositionData = new JLabel("POSITION DATA");
	JLabel lblCycles = new JLabel("CYCLES DATA");
	
	public LifeFormPanel(GUI gui, LibGDXApplicationListener applicationListener, LifeForm lifeForm) {
		this.gui = gui;
		this.applicationListener = applicationListener;
		this.lifeForm = lifeForm;
		
		update();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 60, 75, 0};
		gridBagLayout.rowHeights = new int[] {10, 23, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 1;
		add(lblName, gbc_lblName);
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		
		GridBagConstraints gbc_lblNameData = new GridBagConstraints();
		gbc_lblNameData.anchor = GridBagConstraints.WEST;
		gbc_lblNameData.insets = new Insets(0, 0, 5, 5);
		gbc_lblNameData.gridx = 2;
		gbc_lblNameData.gridy = 1;
		add(lblNameData, gbc_lblNameData);
		
		JLabel lblHealth = new JLabel("Health:");
		lblHealth.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblHealth = new GridBagConstraints();
		gbc_lblHealth.anchor = GridBagConstraints.WEST;
		gbc_lblHealth.insets = new Insets(0, 0, 5, 5);
		gbc_lblHealth.gridx = 1;
		gbc_lblHealth.gridy = 2;
		add(lblHealth, gbc_lblHealth);
		
		GridBagConstraints gbc_lblHealthData = new GridBagConstraints();
		gbc_lblHealthData.anchor = GridBagConstraints.WEST;
		gbc_lblHealthData.insets = new Insets(0, 0, 5, 5);
		gbc_lblHealthData.gridx = 2;
		gbc_lblHealthData.gridy = 2;
		add(lblHealthData, gbc_lblHealthData);
		
		JLabel lblNewLabel = new JLabel("Position:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		add(lblNewLabel, gbc_lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		GridBagConstraints gbc_lblPositionData = new GridBagConstraints();
		gbc_lblPositionData.anchor = GridBagConstraints.WEST;
		gbc_lblPositionData.insets = new Insets(0, 0, 5, 5);
		gbc_lblPositionData.gridx = 2;
		gbc_lblPositionData.gridy = 3;
		add(lblPositionData, gbc_lblPositionData);
		
		JButton btnGoToOn = new JButton("Go To On Map");
		btnGoToOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				goToOnMap();
			}
		});
		
		JButton btnShowBehaviour = new JButton("Show Behaviour");
		btnShowBehaviour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBehaviour();
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Cycles:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		lblCycles = new JLabel("0");
		GridBagConstraints gbc_lblCycles = new GridBagConstraints();
		gbc_lblCycles.insets = new Insets(0, 0, 5, 5);
		gbc_lblCycles.gridx = 2;
		gbc_lblCycles.gridy = 4;
		add(lblCycles, gbc_lblCycles);
		GridBagConstraints gbc_btnShowBehaviour = new GridBagConstraints();
		gbc_btnShowBehaviour.gridwidth = 2;
		gbc_btnShowBehaviour.anchor = GridBagConstraints.CENTER;
		gbc_btnShowBehaviour.insets = new Insets(0, 0, 5, 5);
		gbc_btnShowBehaviour.gridx = 1;
		gbc_btnShowBehaviour.gridy = 5;
		add(btnShowBehaviour, gbc_btnShowBehaviour);
		GridBagConstraints gbc_btnGoToOn = new GridBagConstraints();
		gbc_btnGoToOn.insets = new Insets(0, 0, 0, 5);
		gbc_btnGoToOn.gridwidth = 2;
		gbc_btnGoToOn.anchor = GridBagConstraints.CENTER;
		gbc_btnGoToOn.gridx = 1;
		gbc_btnGoToOn.gridy = 6;
		add(btnGoToOn, gbc_btnGoToOn);

		setPreferredSize(getPreferredSize());
	}
	
	//Show behavior
	public void showBehaviour() {
		
		//If a frame generated by this function already exists, dispose it
		if (showBehaviourFrame != null)
			showBehaviourFrame.dispose();
		
		//Create a new frame for the life form
		showBehaviourFrame = new JFrame();
		showBehaviourFrame.setAlwaysOnTop(true);
		showBehaviourFrame.getContentPane().add(new LifeFormBehaviourPanel(lifeForm));
		showBehaviourFrame.pack();
		showBehaviourFrame.setVisible(true);
	}
	
	//This function will pan the simulation map to centre it on the life form
	public void goToOnMap() {
		applicationListener.centreMapOn(lifeForm);
	}

	//This updates the components of the panel with each cycle of the simulation
	public void update() {
		//	lblNameData.setText(lifeForm.getName());
		lblHealthData.setText(lifeForm.getStamina() + "");
		lblPositionData.setText(lifeForm.getXPosition() + ", " + lifeForm.getYPosition());
		//lblCycles.setText(lifeForm.getSurvivedCycles() + "");
	}
}
