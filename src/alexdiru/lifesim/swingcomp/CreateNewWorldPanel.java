package alexdiru.lifesim.swingcomp;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import alexdiru.lifesim.enums.WorldGenerationMethod;
import alexdiru.lifesim.main.GUI;
import alexdiru.lifesim.main.SimulationSettings;
import alexdiru.lifesim.main.World;
import alexdiru.lifesim.main.WorldGenerationSettings;

import com.badlogic.gdx.Gdx;

public class CreateNewWorldPanel extends JPanel {
	
	private JSpinner spinnerOpenSteps;
	private JSpinner spinnerLifeForms;
	private JSpinner spinnerFood;
	private JLabel lblLifeFormsError;
	private JLabel lblOpenStepsError;
    private JTextField textFieldFileToLoad;
    private JTextField textFieldChromFileToLoad;
	private JLabel lblFileError;
	private JLabel lblXSizeError;
	private JLabel lblYSizeError;
	private JSpinner spinnerXSize;
	private JSpinner spinnerYSize;
	private JLabel lblPoison;
	private JSpinner spinnerPoison;
	private JLabel lblFoodError;
	private JLabel lblPoisonError;

	public CreateNewWorldPanel( final World world) {

		//Default the controls to have the settings of the current world
		WorldGenerationSettings settings = world.getGenerationSettings();
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {90, 30, 90, 90, 90, 90};
		gridBagLayout.rowHeights = new int[] {0, 60, 30, 30, 30, 30, 30, 30, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		final GUI p = world.getGui();
		JLabel lblCreateNewWorld = new JLabel("Create New World");
		lblCreateNewWorld.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblCreateNewWorld = new GridBagConstraints();
		gbc_lblCreateNewWorld.gridwidth = 9;
		gbc_lblCreateNewWorld.fill = GridBagConstraints.VERTICAL;
		gbc_lblCreateNewWorld.insets = new Insets(0, 0, 5, 0);
		gbc_lblCreateNewWorld.gridx = 0;
		gbc_lblCreateNewWorld.gridy = 0;
		add(lblCreateNewWorld, gbc_lblCreateNewWorld);
		
		textFieldFileToLoad = new JTextField();
        textFieldFileToLoad.setText("C:/Users/Alex/Desktop/testmap.txt");
		GridBagConstraints gbc_textFieldFileToLoad = new GridBagConstraints();
		gbc_textFieldFileToLoad.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFileToLoad.gridwidth = 3;
		gbc_textFieldFileToLoad.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldFileToLoad.gridx = 0;
		gbc_textFieldFileToLoad.gridy = 1;
		add(textFieldFileToLoad, gbc_textFieldFileToLoad);
		textFieldFileToLoad.setColumns(10);

        textFieldChromFileToLoad = new JTextField();
        textFieldChromFileToLoad.setText("C:\\Users\\Alex\\Dropbox\\EclipseWorkspace\\life-simulation\\SimulationData\\Simulation20140501220553758\\fittest1.txt");
        GridBagConstraints gbc_textFieldChromFileToLoad = new GridBagConstraints();
        gbc_textFieldChromFileToLoad.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldChromFileToLoad.gridwidth = 3;
        gbc_textFieldChromFileToLoad.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldChromFileToLoad.gridx = 0;
        gbc_textFieldChromFileToLoad.gridy = 2;
        add(textFieldChromFileToLoad, gbc_textFieldChromFileToLoad);
        textFieldChromFileToLoad.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblFileError.setText("");
				JFileChooser fileChooser = new JFileChooser();
				//Default to map directory
				fileChooser.setCurrentDirectory(new File(Gdx.files.internal("assets/Maps").path()));
				int returnVal = fileChooser.showOpenDialog(CreateNewWorldPanel.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                textFieldFileToLoad.setText(file.getAbsolutePath());
	            }
			}
		});
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.gridx = 3;
		gbc_btnBrowse.gridy = 1;
		add(btnBrowse, gbc_btnBrowse);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                world.stopSim();

				lblFileError.setText("");
                System.out.println("Loading map");
				boolean success = world.loadMap(textFieldFileToLoad.getText(), textFieldChromFileToLoad.getText());
				if (!success)
					lblFileError.setText("Please make sure file exists and is not corrupt");
				else
					lblFileError.setText("World loaded successfully");
                world.getEvolver().setCurrentGeneration(1);
                world.getEvolver().getGeneticEngine().setup();
                world.getGui().startWorld(true);
                //world.startSim();
			}
		});
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoad.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoad.gridx = 4;
		gbc_btnLoad.gridy = 1;
		add(btnLoad, gbc_btnLoad);
		
		lblFileError = new JLabel("");
		GridBagConstraints gbc_lblFileError = new GridBagConstraints();
		gbc_lblFileError.anchor = GridBagConstraints.WEST;
		gbc_lblFileError.gridwidth = 6;
		gbc_lblFileError.insets = new Insets(0, 0, 5, 5);
		gbc_lblFileError.gridx = 0;
		gbc_lblFileError.gridy = 2;
		add(lblFileError, gbc_lblFileError);
		
		JLabel lblXSize = new JLabel("X Size");
		GridBagConstraints gbc_lblXSize = new GridBagConstraints();
		gbc_lblXSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblXSize.gridx = 0;
		gbc_lblXSize.gridy = 3;
		add(lblXSize, gbc_lblXSize);
		
		spinnerXSize = new JSpinner();
		GridBagConstraints gbc_spinnerXSize = new GridBagConstraints();
		gbc_spinnerXSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerXSize.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerXSize.gridx = 2;
		gbc_spinnerXSize.gridy = 3;
		spinnerXSize.setValue(settings.getXSize());
		add(spinnerXSize, gbc_spinnerXSize);
		
		lblXSizeError = new JLabel("");
		GridBagConstraints gbc_lblXSizeError = new GridBagConstraints();
		gbc_lblXSizeError.gridwidth = 3;
		gbc_lblXSizeError.insets = new Insets(0, 0, 5, 5);
		gbc_lblXSizeError.gridx = 3;
		gbc_lblXSizeError.gridy = 3;
		add(lblXSizeError, gbc_lblXSizeError);
		
		JLabel lblYSize = new JLabel("Y Size");
		GridBagConstraints gbc_lblYSize = new GridBagConstraints();
		gbc_lblYSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblYSize.gridx = 0;
		gbc_lblYSize.gridy = 4;
		add(lblYSize, gbc_lblYSize);
		
		spinnerYSize = new JSpinner();
		GridBagConstraints gbc_spinnerYSize = new GridBagConstraints();
		gbc_spinnerYSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerYSize.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerYSize.gridx = 2;
		gbc_spinnerYSize.gridy = 4;
		spinnerYSize.setValue(settings.getYSize());
		add(spinnerYSize, gbc_spinnerYSize);
		
		lblYSizeError = new JLabel("");
		GridBagConstraints gbc_lblYSizeError = new GridBagConstraints();
		gbc_lblYSizeError.gridwidth = 3;
		gbc_lblYSizeError.insets = new Insets(0, 0, 5, 5);
		gbc_lblYSizeError.gridx = 3;
		gbc_lblYSizeError.gridy = 4;
		add(lblYSizeError, gbc_lblYSizeError);
		
		JLabel lblOpenSteps = new JLabel("Open Steps");
		GridBagConstraints gbc_lblOpenSteps = new GridBagConstraints();
		gbc_lblOpenSteps.insets = new Insets(0, 0, 5, 5);
		gbc_lblOpenSteps.gridx = 0;
		gbc_lblOpenSteps.gridy = 5;
		add(lblOpenSteps, gbc_lblOpenSteps);
		
		spinnerOpenSteps = new JSpinner();
		GridBagConstraints gbc_spinnerOpenSteps = new GridBagConstraints();
		gbc_spinnerOpenSteps.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerOpenSteps.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerOpenSteps.gridx = 2;
		gbc_spinnerOpenSteps.gridy = 5;
		add(spinnerOpenSteps, gbc_spinnerOpenSteps);
		spinnerOpenSteps.setValue((Integer)settings.getOpenSteps());
		
		lblOpenStepsError = new JLabel();
		GridBagConstraints gbc_lblOpenStepsError = new GridBagConstraints();
		gbc_lblOpenStepsError.gridwidth = 3;
		gbc_lblOpenStepsError.insets = new Insets(0, 0, 5, 5);
		gbc_lblOpenStepsError.gridx = 3;
		gbc_lblOpenStepsError.gridy = 5;
		add(lblOpenStepsError, gbc_lblOpenStepsError);
		
		JLabel lblNewLabel = new JLabel("Life Forms");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 6;
		add(lblNewLabel, gbc_lblNewLabel);
		
		spinnerLifeForms = new JSpinner();
		GridBagConstraints gbc_spinnerLifeForms = new GridBagConstraints();
		gbc_spinnerLifeForms.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerLifeForms.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerLifeForms.gridx = 2;
		gbc_spinnerLifeForms.gridy = 6;
		add(spinnerLifeForms, gbc_spinnerLifeForms);
		spinnerLifeForms.setValue((Integer)settings.getNumberOfLifeForms());
		
		lblLifeFormsError = new JLabel();
		GridBagConstraints gbc_lblLifeFormsError = new GridBagConstraints();
		gbc_lblLifeFormsError.gridwidth = 3;
		gbc_lblLifeFormsError.insets = new Insets(0, 0, 5, 5);
		gbc_lblLifeFormsError.gridx = 3;
		gbc_lblLifeFormsError.gridy = 6;
		add(lblLifeFormsError, gbc_lblLifeFormsError);
		
		JLabel lblFood = new JLabel("Food %");
		GridBagConstraints gbc_lblFood = new GridBagConstraints();
		gbc_lblFood.insets = new Insets(0, 0, 5, 5);
		gbc_lblFood.gridx = 0;
		gbc_lblFood.gridy = 7;
		add(lblFood, gbc_lblFood);
		
		spinnerFood = new JSpinner();
		GridBagConstraints gbc_spinnerFood = new GridBagConstraints();
		gbc_spinnerFood.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerFood.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerFood.gridx = 2;
		gbc_spinnerFood.gridy = 7;
		add(spinnerFood, gbc_spinnerFood);
		spinnerFood.setValue((Integer)settings.getPercentageOfFood());
		
		lblFoodError = new JLabel("");
		GridBagConstraints gbc_lblFoodError = new GridBagConstraints();
		gbc_lblFoodError.gridwidth = 3;
		gbc_lblFoodError.insets = new Insets(0, 0, 5, 5);
		gbc_lblFoodError.gridx = 3;
		gbc_lblFoodError.gridy = 7;
		add(lblFoodError, gbc_lblFoodError);
		
		lblPoison = new JLabel("Poison %");
		GridBagConstraints gbc_lblPoison = new GridBagConstraints();
		gbc_lblPoison.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoison.gridx = 0;
		gbc_lblPoison.gridy = 8;
		add(lblPoison, gbc_lblPoison);
		
		spinnerPoison = new JSpinner();
		GridBagConstraints gbc_spinnerPoison = new GridBagConstraints();
		gbc_spinnerPoison.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPoison.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPoison.gridx = 2;
		gbc_spinnerPoison.gridy = 8;
		add(spinnerPoison, gbc_spinnerPoison);
		spinnerPoison.setValue((Integer)settings.getPercentageOfPoison());
		
		JButton btnGenerateEmpty = new JButton("Generate Empty World");
		btnGenerateEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearAllErrorLabels();
				boolean cerr = checkCommonErrors();
				boolean eerr = checkEmptyWorldErrors();

				if (cerr || eerr)
					return;

                world.stopSim();

                WorldGenerationSettings settings = new WorldGenerationSettings();
				settings.setXSize((Integer) spinnerXSize.getValue());
				settings.setYSize((Integer) spinnerYSize.getValue());
				settings.setOpenSteps((Integer)spinnerOpenSteps.getValue());
				SimulationSettings.populationSize = (Integer)spinnerLifeForms.getValue();
				settings.setPercentageOfFood((Integer) spinnerFood.getValue());
				settings.setPercentageOfPoison((Integer) spinnerPoison.getValue());
				world.generateWorld(settings, WorldGenerationMethod.EMPTY);

                world.startSim();
			}
		});
		
		lblPoisonError = new JLabel("");
		GridBagConstraints gbc_lblPoisonError = new GridBagConstraints();
		gbc_lblPoisonError.gridwidth = 3;
		gbc_lblPoisonError.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoisonError.gridx = 3;
		gbc_lblPoisonError.gridy = 8;
		add(lblPoisonError, gbc_lblPoisonError);
		GridBagConstraints gbc_btnGenerateEmpty = new GridBagConstraints();
		gbc_btnGenerateEmpty.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGenerateEmpty.gridwidth = 3;
		gbc_btnGenerateEmpty.insets = new Insets(0, 0, 5, 5);
		gbc_btnGenerateEmpty.gridx = 0;
		gbc_btnGenerateEmpty.gridy = 9;
		add(btnGenerateEmpty, gbc_btnGenerateEmpty);
		
		JButton btnGenerateDrunken = new JButton("Generate Drunken Walk World");
		
		btnGenerateDrunken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllErrorLabels();
				boolean cerr = checkCommonErrors();
				boolean derr = checkDrunkenWalkErrors();

				if (cerr || derr)
					return;
				
				world.pauseSimulation();
				
				//Get the settings
				WorldGenerationSettings settings = new WorldGenerationSettings();
				settings.setOpenSteps((Integer)spinnerOpenSteps.getValue());
				settings.setNumberOfLifeForms((Integer)spinnerLifeForms.getValue());
				settings.setPercentageOfFood((Integer)spinnerFood.getValue());
				settings.setPercentageOfPoison((Integer)spinnerPoison.getValue());
				world.generateWorld(settings, WorldGenerationMethod.DRUNKENWALK);
				
				world.togglePauseSimulation();
			}
		});
		GridBagConstraints gbc_btnGenerateDrunken = new GridBagConstraints();
		gbc_btnGenerateDrunken.gridwidth = 3;
		gbc_btnGenerateDrunken.insets = new Insets(0, 0, 0, 5);
		gbc_btnGenerateDrunken.fill = GridBagConstraints.BOTH;
		gbc_btnGenerateDrunken.gridx = 0;
		gbc_btnGenerateDrunken.gridy = 10;
		//add(btnGenerateDrunken, gbc_btnGenerateDrunken);
	}
	


	private void clearAllErrorLabels() {
		lblXSizeError.setText("");
		lblYSizeError.setText("");
		lblOpenStepsError.setText("");
		lblLifeFormsError.setText("");
		lblFoodError.setText("");
		lblPoisonError.setText("");
	}
	
	private boolean checkEmptyWorldErrors() {
		boolean errorsFound = false;

		if ((Integer)spinnerXSize.getValue() < 1) {
			errorsFound = true;
			lblXSizeError.setText("Must be greater than 0");
		}
		if ((Integer)spinnerYSize.getValue() < 1) {
			errorsFound = true;
			lblYSizeError.setText("Must be greater than 0");
		}
		
		return errorsFound;
	}
	
	private boolean checkDrunkenWalkErrors() {
		boolean errorsFound = false;
		
		if ((Integer)spinnerOpenSteps.getValue() < 20) {
			errorsFound = true;
			lblOpenStepsError.setText("Must be greater than 20");
		}
		
		return errorsFound;
	}
	
	private boolean checkCommonErrors() {
		boolean errorsFound = false;
		
		if ((Integer)spinnerLifeForms.getValue() < 1) {
			errorsFound = true;
			lblLifeFormsError.setText("Must be greater than 0");
		}
		
		if ((Integer)spinnerFood.getValue() < 0 || (Integer)spinnerFood.getValue() > 100) {
			errorsFound = true;
			lblFoodError.setText("Must be between 0 and 100");
		}
		
		if ((Integer)spinnerPoison.getValue() < 0 || (Integer)spinnerPoison.getValue() > 100) {
			errorsFound = true;
			lblPoisonError.setText("Must be between 0 and 100");
		}

		return errorsFound;
	}

}
