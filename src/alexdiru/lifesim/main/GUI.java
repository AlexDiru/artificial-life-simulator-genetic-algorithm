package alexdiru.lifesim.main;

import alexdiru.lifesim.enums.GraphType;
import alexdiru.lifesim.enums.WorldGenerationMethod;
import alexdiru.lifesim.jgap.ga.GeneManager;
import alexdiru.lifesim.swingcomp.CreateNewWorldPanel;
import alexdiru.lifesim.swingcomp.GenerationGrowthLineGraphPanel;
import alexdiru.lifesim.swingcomp.SimulationSummaryPanel;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import org.jgap.Gene;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;

public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private int frameXSize = 1920;
	private int frameYSize = 1000;
	private int renderXSize = 900;
	private int renderYSize = 900;
	
	private Evolver evolver;
	
	private LibGDXApplicationListener applicationListener;
	private JFrame showCreateNewWorldFrame = null;
	private JFrame showSummaryFrame = null;
	private JLabel generationProgressLabel;
	
	private JTree behaviourTree;
	private DefaultTreeModel behaviourTreeModel;
	
	private Gene[] fittestGenes = null;
    private Thread t = null; //Thread to run the world on

    public void setTree(DefaultMutableTreeNode root) {
		behaviourTreeModel.setRoot(root);
		for (int i = 0; i < behaviourTree.getRowCount(); i++) 
	         behaviourTree.expandRow(i);
	}

    public Thread getEvolverThread() {
        return t;
    }

	
    public GUI() {
    	
    	//Set the system look and feel
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	}  catch (Exception e) {
    	}
    	
        setTitle("Artificial Life Simulator");
       setLocationRelativeTo(null);
       this.setLocation(0, 0);
       setDefaultCloseOperation(EXIT_ON_CLOSE);     
       
       JPanel layout = new JPanel();
       getContentPane().add(layout);
       layout.setLayout(new GridLayout(1,2));
       
       Container openglContainer = new Container();
       JPanel swingContainer = new JPanel();
       
       //Create the world here to allow it to be passed to the application listener
         evolver = new Evolver(this);
       applicationListener = new LibGDXApplicationListener(this,openglContainer,evolver.getWorld(),renderXSize, renderYSize);
       LwjglAWTCanvas can = new LwjglAWTCanvas(applicationListener,true);
        can.getCanvas().setSize(renderXSize, renderYSize);
       
       //Swing Controls
       swingContainer.setLayout(new GridLayout(0,1));
  
       openglContainer.add(can.getCanvas());

       layout.add(openglContainer);
       layout.add(swingContainer);
       
       Box verticalBox = Box.createVerticalBox();
       swingContainer.add(verticalBox);
       
       JPanel panel = new JPanel();
       verticalBox.add(panel);
       
       JButton btnPlaypause = new JButton("Play/Pause");
       panel.add(btnPlaypause);
       btnPlaypause.setAlignmentX(Component.CENTER_ALIGNMENT);
       
       JButton btnRestart = new JButton("Restart");
       btnRestart.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent e) {
            evolver.getWorld().restart();
       	}
       });
       panel.add(btnRestart);
       btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);

       JButton btnShowSimulationSummary = new JButton("Show Data");
        btnShowSimulationSummary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSimulationSummary();
            }
        });
        panel.add(btnShowSimulationSummary);
       
       JLabel lblSimulationSpeed = new JLabel("Simulation Speed:");
       panel.add(lblSimulationSpeed);
       lblSimulationSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
       
       final JSpinner spinnerSimulationSpeed = new JSpinner();
       spinnerSimulationSpeed.addChangeListener(new ChangeListener() {
       	public void stateChanged(ChangeEvent arg0) {
       		evolver.getWorld().setSimulationSpeed((Integer)spinnerSimulationSpeed.getValue());
       	}
       });
       panel.add(spinnerSimulationSpeed);
       
       JButton btnCreateNewWorld = new JButton("Create New World");
       btnCreateNewWorld.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		showCreateNewWorldFrame();
       	}
       });
       panel.add(btnCreateNewWorld);
       
       final JCheckBox btnAutoModeToggle = new JCheckBox("Auto Mode");
       btnAutoModeToggle.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
            evolver.getWorld().toggleAutoMode();
       	}
       });
       panel.add(btnAutoModeToggle);
       
       final JCheckBox cb = new JCheckBox("Render");
       cb.setSelected(true);
       cb.addActionListener(new ActionListener() {
          	public void actionPerformed(ActionEvent arg0) {
          		applicationListener.toggleRender();
          	}
       });
       panel.add(cb);
       
       JCheckBox chckbxFollowLifeForm = new JCheckBox("Follow Life Form");
       chckbxFollowLifeForm.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent arg0) {
    		   applicationListener.toggleFollowLifeForm();
    	   }
       });
       panel.add(chckbxFollowLifeForm);
       
       generationProgressLabel = new JLabel();
       generationProgressLabel.setHorizontalAlignment(SwingConstants.LEFT);
       generationProgressLabel.setText("TEST");
       verticalBox.add(generationProgressLabel);
       
       Box horizontalBox = Box.createHorizontalBox();
       verticalBox.add(horizontalBox);
       Dimension d = new Dimension();
       
       JPanel panel_1 = new JPanel();
       FlowLayout flowLayout_3 = (FlowLayout) panel_1.getLayout();
       flowLayout_3.setAlignment(FlowLayout.LEFT);
       verticalBox.add(panel_1);
       
       Box verticalBox_1 = Box.createVerticalBox();
       panel_1.add(verticalBox_1);
       //verticalBox_1.setPreferredSize(d);
       
       JPanel chartPanel = new GenerationGrowthLineGraphPanel(evolver.getDataset(), GraphType.AVERAGE);
       verticalBox_1.add(chartPanel);
       FlowLayout flowLayout_2 = (FlowLayout) chartPanel.getLayout();
       flowLayout_2.setAlignment(FlowLayout.LEFT);
       
       JPanel maximumChartPanel = new GenerationGrowthLineGraphPanel(evolver.getDataset(), GraphType.MAXIMUM);
       verticalBox_1.add(maximumChartPanel);
       FlowLayout flowLayout = (FlowLayout) maximumChartPanel.getLayout();
       flowLayout.setAlignment(FlowLayout.LEFT);
       
       JPanel minimumChartPanel = new GenerationGrowthLineGraphPanel(evolver.getDataset(), GraphType.MINIMUM);
       verticalBox_1.add(minimumChartPanel);
       FlowLayout flowLayout_1 = (FlowLayout) minimumChartPanel.getLayout();
       flowLayout_1.setAlignment(FlowLayout.LEFT);
       d.setSize(verticalBox_1.getMinimumSize().getWidth(), verticalBox_1.getPreferredSize().getHeight());
       behaviourTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("No fittest gene in first generation"));
       behaviourTreeModel.addTreeModelListener(new TreeModelListener() {
			 public void treeNodesChanged(TreeModelEvent e) {
		        DefaultMutableTreeNode node;
		        node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
		        try {
		            int index = e.getChildIndices()[0];
		            node = (DefaultMutableTreeNode)(node.getChildAt(index));
		        } catch (NullPointerException ex) {
		        }
		    }
		    public void treeNodesInserted(TreeModelEvent e) {
		    }
		    public void treeNodesRemoved(TreeModelEvent e) {
		    }
		    public void treeStructureChanged(TreeModelEvent e) {
		    }
		});
       
       JPanel behaviourTreePanel = new JPanel();
       panel_1.add(behaviourTreePanel);
       behaviourTreePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
       
       behaviourTree = new JTree();
       behaviourTreePanel.add(behaviourTree);
       
       behaviourTree.setModel(behaviourTreeModel);
       
       JButton btnLoad = new JButton("Load");
       btnLoad.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
       		loadBehaviour();	
       	}
       });
       behaviourTreePanel.add(btnLoad);
       
       JButton btnSave = new JButton("Save");
       btnSave.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               saveBehaviour();
           }
       });
       behaviourTreePanel.add(btnSave);
       
       btnPlaypause.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent arg0) {
            evolver.getWorld().togglePauseSimulation();
       	}
       });
      
       setSize(frameXSize, frameYSize);

        evolver.getWorld().generateWorld(new WorldGenerationSettings(), WorldGenerationMethod.EMPTY);
       //world.generateDrunkenWalkWorld(this,new WorldGenerationSettings());
       
       //The GUI has been created, so start the simulation's thread
       startWorld();
    }

    public void startWorld() {
       if (t != null)
           return;
       t = new Thread(evolver);
        t.start();
    }
    
    public static String convertTextToMultiLine(String text) {
    	return "<html>" + text.replaceAll("\n", "<br>");
    }
    
    //Updates the GUI compenents every cycle of the world
    public void updateComponents(World world) {
		generationProgressLabel.setText(GUI.convertTextToMultiLine(world.getSimulationProgressText()));
    }	
    
    public void showCreateNewWorldFrame() {
    	//Pause the simulation
        evolver.getWorld().pauseSimulation();

		//If a frame generated by this function already exists, ignore second button press
		if (showCreateNewWorldFrame != null) {
			showCreateNewWorldFrame.setVisible(true);
			return;
		}
		
		//Create a new frame for the world creation
		showCreateNewWorldFrame = new JFrame();
		showCreateNewWorldFrame.setAlwaysOnTop(true);
		showCreateNewWorldFrame.getContentPane().add(new CreateNewWorldPanel(evolver.getWorld()));
		showCreateNewWorldFrame.pack();
		showCreateNewWorldFrame.setVisible(true);
    }
    
	public void showSimulationSummary() {
		//The problem of this frame being closed meant that the next round wasn't able to be started
		//So a window listener is used to automatically start the next round if the frame is closed
		synchronized (evolver.getWorld()) {
            if (showSummaryFrame != null) {
                showSummaryFrame.setVisible(true);
                return;
            }

			//Create a new frame for the life form
			showSummaryFrame = new JFrame();
			showSummaryFrame.setAlwaysOnTop(true);

			showSummaryFrame.setSize(1000,666);
			showSummaryFrame.getContentPane().add(new SimulationSummaryPanel(this, evolver));
			showSummaryFrame.setVisible(true);
		}
	}

	//Hides the frame containing the summary of a round/generation
	public void closeSimulationSummaryFrame() {
		showSummaryFrame.setVisible(false);
	}
	
	public LibGDXApplicationListener getApplicationListener() {
		return applicationListener;
	}

	public void setFittestGenes(Gene[] genes) {
		fittestGenes = genes;
	}

	private void loadBehaviour() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("C:/Users/Alex/Dropbox/EclipseWorkspace/life-simulation"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            evolver.getGeneticEngine().loadFittest(chooser.getSelectedFile().getAbsolutePath());
	}
	
	private void saveBehaviour() {
        if (fittestGenes != null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("C:/Users/Alex/Dropbox/EclipseWorkspace/life-simulation"));
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                GeneManager.saveToFile(chooser.getSelectedFile() + ".txt", fittestGenes);

        }
	}

    public SimulationSummaryPanel getSimulationSummaryPanel() {
        if (showSummaryFrame == null)
            return null;
        return (SimulationSummaryPanel)showSummaryFrame.getContentPane().getComponent(0);
    }
}