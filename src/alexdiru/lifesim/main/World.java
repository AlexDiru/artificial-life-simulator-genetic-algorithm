package alexdiru.lifesim.main;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.*;
import java.util.ArrayList;
import java.util.Random;

import alexdiru.lifesim.enums.WorldGenerationMethod;
import org.jgap.IChromosome;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;


public class World implements Serializable {

    /**
     * Life form being simulated in the world if running
     */
    private LifeForm lifeForm;

    /**
     * 'Tile' width of the map, actual width is xTileSize * Renderer.TILE_SIZE
     */
    protected int xTileSize;

    /**
     * 'Tile' height of the map, actual height is yTileSize * Renderer.TILE_SIZE
     */
    protected int yTileSize;

    /**
     * The obstacles on the map
     */
    protected ArrayList<Obstacle> obstacles;

    /**
     * The items on the map (such as food, poison)
     */
    protected ArrayList<Item> items;

    /**
     * The water on the map
     */
    protected ArrayList<Water> waters;

    /**
     * The GUI the simulation is displayed in
     */
    protected transient GUI gui;

    /**
     * The set of the positions that the life forms are tested from on the map
     */
    protected ArrayList<Point> simulationStartPositions;

    /**
     * Random number generator
     */
    protected transient Random random = new Random();

    /**
     * Settings used to control the settings of the generated simulation world
     */
    private transient WorldGenerationSettings generationSettings;

    private transient Evolver evolver;

    /**
     * The intended number of cycles to simulate each second
     */
    protected Integer simulationSpeed;

    /**
     * Whether the simulation is paused (toggled with the Play/Pause button)
     */
    protected Boolean simulationPaused;

    /**
     * Whether or not the view summary after a generation/round is skipped
     */
    protected Boolean autoMode;

    private boolean render;
	
	/**
	 * The index of the current position the life form has started the simulation from
	 */
	private Integer simulationStartPosition;

    private World() {

    }

	public World(GUI gui, Evolver evolver) {
        this.gui = gui;
        this.evolver = evolver;
        items = new ArrayList<Item>();
        waters = new ArrayList<Water>();
        obstacles = new ArrayList<Obstacle>();
        resetWorld();
        simulationSpeed = 10;
        simulationPaused = true;
        autoMode = false;
        simulationStartPosition = 0;
        render = true;
	}
	
	/**
	 * Simulates a single chromosome in the world
	 * @param chromosome The chromosome to simulate
	 * @return The fitness of the chromosome
	 */
	public double simulate(IChromosome chromosome) {
		//Create the life form
		prepareWorldForNewLifeForm();
		lifeForm = prepareNewLifeForm(chromosome);
		//Simulate the life form
		simulateLifeForm(lifeForm);

        evolver.incrementNumberOfLifeFormsSimulatedThisGeneration();
		//Return the simulated life form's fitness
	    return lifeForm.evaluateFitness();
	}
	
	/**
	 * Called once the map has been generated (either algorithmically or loaded from a file)
	 * Sets up the genetic algorithm engine, and defaults some variables
	 */
	protected void postMapGenerationSetup() {
        evolver.reset();
		evolver.setNumberOfLifeFormsSimulatedThisGeneration(0);
	}
	
	/**
	 * Cleans up the world ready for another world to be regenerated
	 * This is to avoid the current content having any effect on the new content
	 */
	protected void resetWorld() {
		obstacles.clear();
		items.clear();
		waters.clear();
	}
	
	/**
	 * Restarts the map to its original setup
	 */
	protected void restart() {
        for (Obstacle obstacle : obstacles)
            obstacle.restart();

        for (Item item : items)
            item.restart();

        for (Water water : waters)
            water.restart();
	}
	
	/**
	 * Prepares the world ready to simulate a new life form
	 */
	private void prepareWorldForNewLifeForm() {
		restart();
	}
	
	/**
	 * Prepares the world ready to simulate the next position of the life form
	 */
	private void prepareWorldForNextPosition() {
		prepareWorldForNewLifeForm();
	}
	
	/**
	 * Prepares a new life form for the simulation
	 * @param chromosome The chromosome to simulate
	 */
	private LifeForm prepareNewLifeForm(IChromosome chromosome) {
		LifeForm lifeForm = new LifeForm(this);
		lifeForm.setChromosome(chromosome);
		simulationStartPosition = 0;
		prepareNewPosition(lifeForm);

        return lifeForm;
	}
	
	/**
	 * Prepares a new position for the current life form to start a simulation from
	 */
	private void prepareNewPosition(LifeForm currentLifeForm) {
		currentLifeForm.restart();
		currentLifeForm.onSimulateNextPosition(simulationStartPositions.get(simulationStartPosition));
		//gui.getApplicationListener().centreMapOn(currentLifeForm);
	}

	/**
	 * Simulates the current life form (including all of its start positions)
	 */
	private void simulateLifeForm(LifeForm currentLifeForm) {
        lifeForm = currentLifeForm;
		boolean breakLoop = false;
        int localElapsedCycles = 0;
        simulationStartPosition = 0;
		long previousTime =  System.currentTimeMillis();
		while (!breakLoop && !evolver.getGeneticEngine().isStopSimulation())
            while (simulationSpeed >= 1000 || System.currentTimeMillis() > previousTime + (1000/simulationSpeed)) {
            	if (!simulationPaused) {
					currentLifeForm.update();
                    localElapsedCycles++;

					if (simulationTimedOut(currentLifeForm, localElapsedCycles)) {
                        localElapsedCycles = 0;
						currentLifeForm.addData(currentLifeForm.getStamina(),currentLifeForm.getUsedEnergy());

                        simulationStartPosition++;
						if (simulationStartPosition < simulationStartPositions.size()) {
							prepareWorldForNextPosition();
							prepareNewPosition(currentLifeForm);
						} else {
							breakLoop = true;
							break;
						}
					}
            	}
                previousTime = System.currentTimeMillis();
            }
	}

	/**
	 * Whether the simulation has exceeded its set number of cycles to simulate for
	 * @return Whether the current simulation is over
	 */
	private boolean simulationTimedOut(LifeForm lifeForm, int elapsedCycles) {
		return elapsedCycles > lifeForm.getSenses().getLifeSpan();
	}


    /**
     * Initialises the positions that each life form is tested from on the map, the positions are randomly chosen on the map
     */
    private void setupSimulationPositions() {
        simulationStartPositions = new ArrayList<Point>();
        for (int i = 0; i < SimulationSettings.simulationPositions; i++)
            simulationStartPositions.add(new Point(random.nextInt(xTileSize),random.nextInt(yTileSize)));
    }

    /**
     * Regenerates an entire new map (new food positions), based on the current settings
     * This is used when the world needs to be regenerated between generations (if the regenerate world flag is enabled)
     */
    public void regenerateWorld() {
        items.clear();
        waters.clear();
        obstacles.clear();

        generateFoodAndPoison(generationSettings.getPercentageOfFood(), generationSettings.getPercentageOfPoison(), generationSettings.getTileBasedDistribution());
        generateWater(generationSettings.getPercentageOfWater());

        setupSimulationPositions();
    }


    /**
     * Adds random water to the map
     * @param percentage The density of the water
     */
    private void generateWater(int percentage) {
        for (int x = 0; x < xTileSize; x++)
            for (int y = 0; y < yTileSize; y++)
                if (random.nextInt(100) < percentage)
                    waters.add(new Water(x,y));
    }

    /**
     * Adds random food and poison to the map
     * Assert check: probabilityOfFood + probabilityOfPoison <= 100
     * Assert check: probabilityOfFood >= 0
     * Assert check: probabilityOfPoison >= 0
     * @param probabilityOfFood The density of food
     * @param probabilityOfPoison The density of poison
     */
    private void generateFoodAndPoison(int probabilityOfFood, int probabilityOfPoison, boolean tileBasedDistribution) {
        assert probabilityOfFood + probabilityOfPoison <= 100;
        assert probabilityOfFood >= 0;
        assert probabilityOfPoison >= 0;
        for (int x = 0; x < xTileSize; x++)
            for (int y = 0; y < yTileSize; y++) {
                int r = random.nextInt(100);
                int xPosition;
                int yPosition;

                if (tileBasedDistribution) {
                    xPosition = random.nextInt(xTileSize) *Renderer.TILE_SIZE;
                    yPosition = random.nextInt(yTileSize) *Renderer.TILE_SIZE;
                } else {
                    xPosition = random.nextInt(xTileSize *Renderer.TILE_SIZE);
                    yPosition = random.nextInt(yTileSize *Renderer.TILE_SIZE);
                }

                if (r < probabilityOfFood)
                    items.add(new Food(xPosition,yPosition));
                else if (r < probabilityOfFood + probabilityOfPoison)
                    items.add(new Poison(xPosition,yPosition));
            }
    }

    /**
     * Generates a map for the world
     * @param settings The settings used to create the world with
     * @param method The algorithm used to create the world
     */
    public void generateWorld(WorldGenerationSettings settings, WorldGenerationMethod method) {
        generationSettings = settings;

        //Reset anything currently on the world, so it won't affect the new content that will be generated
        resetWorld();

        if (method == WorldGenerationMethod.DRUNKENWALK) {
            DrunkenWalkWorld dww = new DrunkenWalkWorld();
            dww.generate(settings.getOpenSteps());
            xTileSize = dww.getXSize();
            yTileSize = dww.getYSize();
            obstacles = dww.getObstacles();
        } else if (method == WorldGenerationMethod.EMPTY) {
            xTileSize = settings.getXSize();
            yTileSize = settings.getYSize();
        }

        generateFoodAndPoison(settings.getPercentageOfFood(), settings.getPercentageOfPoison(), settings.getTileBasedDistribution());
        generateWater(settings.getPercentageOfWater());

        setupSimulationPositions();

        postMapGenerationSetup();
    }

    //Loads a map and returns the success of the operation
    public boolean loadMap(String mapFile) {
        resetWorld();

        InputStream in = null; //The file to be passed to the stream reader
        String errorMessage = ""; //The error message to tell the user what is wrong with the XML
        xTileSize = -1; //Defaulted to -1 to see if a value is read from XML
        yTileSize = -1; //Defaulted to -1 to see if a value is read from XML

        try {
            //Setup the stream reader
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            in = new FileInputStream(mapFile);
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);

            //Read the XML document
            while (streamReader.hasNext()) {
                //Get the next item in the document
                int next = streamReader.next();
                if (next == XMLStreamReader.START_ELEMENT) {
                    //If the <map> node is read
                    if (streamReader.getLocalName().equals("map")) {
                        //Get the xsize and ysize attributes
                        //i.e. <map xsize="3" ysize="4">
                        //Loop through all the attributes of the node
                        for (int i = 0; i < streamReader.getAttributeCount(); i++) {
                            //xsize found
                            if (streamReader.getAttributeLocalName(i).equals("xsize")) {
                                //Make sure it's an integer
                                try {
                                    xTileSize = Integer.parseInt(streamReader.getAttributeValue(i));
                                } catch (NumberFormatException ex) {
                                    errorMessage += "Attribute \"xsize\" must be an integer\n";
                                }
                            }
                            //ysize found
                            else if (streamReader.getAttributeLocalName(i).equals("ysize")) {
                                //Make sure it's an integer
                                try {
                                    yTileSize = Integer.parseInt(streamReader.getAttributeValue(i));
                                } catch (NumberFormatException ex) {
                                    errorMessage += "Attribute \"ysize\" must be an integer\n";
                                }
                            }

                            //Make sure xsize and ysize attributes were read from the node
                            if (xTileSize == -1)
                                errorMessage += "Attribute \"xsize\" is missing\n";
                            if (yTileSize == -1)
                                errorMessage += "Attribute \"ysize\" is missing\n";
                        }
                        //If an object node is read
                    } else if (streamReader.getLocalName().equals("obstacle") ||
                            streamReader.getLocalName().equals("water") ||
                            streamReader.getLocalName().equals("food") ||
                            streamReader.getLocalName().equals("poison") ||
                            streamReader.getLocalName().equals("lifeform") ||
                            streamReader.getLocalName().equals("lilypad")) {

                        //Read the coordinates and split them by the comma
                        String xyCoordinates = streamReader.getElementText();
                        String[] xyCoordinatesSplit = xyCoordinates.split(",");

                        //Make sure there are two coordinates (x & y)
                        if (xyCoordinatesSplit.length == 2) {
                            //Get the coordinates from the comma separated string
                            try {
                                int x = Integer.parseInt(xyCoordinatesSplit[0]);
                                int y = Integer.parseInt(xyCoordinatesSplit[1]);

                                //Add the object from the coordinates
                                if (streamReader.getLocalName().equals("obstacle"))
                                    obstacles.add(new Obstacle(x,y));
                                else if (streamReader.getLocalName().equals("water"))
                                    waters.add(new Water(x,y));
                                //else if (streamReader.getLocalName().equals("poison"))
                                 //   items.add(new Poison(x,y));
                                //else if (streamReader.getLocalName().equals("food"))
                                  //  items.add(new Food(x,y));
                            } catch (NumberFormatException ex) {
                                errorMessage += "Object coordinates must be an integers\n";
                            }

                        } else {
                            errorMessage += "Object coordinates must be two comma separated integers\n";
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }

        try {
            in.close();
        } catch (Exception e) {
        }

        System.out.println(errorMessage);

        postMapGenerationSetup();

        return true;
    }

    //Returns the obstacles on the map
    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    //Gets the width of the map in number of tiles
    public int getXSize() {
        return xTileSize;
    }

    //Gets the height of the map in number of tiles
    public int getYSize() {
        return yTileSize;
    }

    //Returns the items on the map
    public ArrayList<Item> getItems() {
        return items;
    }

    //Returns the water on the map
    public ArrayList<Water> getWaters() {
        return waters;
    }

    public WorldGenerationSettings getGenerationSettings() {
        return generationSettings;
    }

    //Toggles the auto mode so only one button is needed
    public void toggleAutoMode() {
        autoMode = !autoMode;
    }

    //Whether the View Summary frame appears after each generation or not
    public boolean inAutoMode() {
        return autoMode;
    }

	/**
	 * Pauses the simulation if it is running, otherwise runs it if it's paused
	 */
	public void togglePauseSimulation() {
		simulationPaused = !simulationPaused;
	}
	
	/**
	 * Pauses the simulation
	 */
	public void pauseSimulation() {
		simulationPaused = true;
	}

	/**
	 * Prepares the world for when the next generation of the simulation needs to be started
	 */
	public void prepareForNextGeneration() {
		restart();
        evolver.setNumberOfLifeFormsSimulatedThisGeneration(0);
	}

	public LifeForm getCurrentLifeForm() {
		return lifeForm;
	}

	public void setSimulationSpeed(int value) {
		if (simulationSpeed < 1)
			return;
		simulationSpeed = value;
	}

	public String getSimulationProgressText() {
		//if (currentLifeForm == null)
		//	return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("Generation: ");
		sb.append(evolver.getCurrentGeneration());
		sb.append("\nLife Forms: ");
		sb.append(evolver.getNumberOfLifeFormsSimulatedThisGeneration());
		sb.append("/");
		sb.append(SimulationSettings.populationSize + "(excluding chromosomes created during breeding process)");
		sb.append("\nPositions: ");
		sb.append(simulationStartPosition + 1);
		sb.append("/");
		sb.append(simulationStartPositions.size());
		sb.append("\nCycles: ");
		//sb.append(elapsedCycles + 1);
		sb.append("/");
		//sb.append(currentLifeForm.getSenses().getLifeSpan());
		return sb.toString();
	}

    /**
     * Gets the GUI the world is displayed on
     * @return
     */
	public GUI getGui() {
		return gui;
	}

    /**
     * Resets the world for a new simulation to take place
     */
    public void reset() {
        resetWorld();
        evolver.setNumberOfLifeFormsSimulatedThisGeneration(0);
    }

    /**
     * Sets the GUI the world is displayed on
     * @param gui
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public ArrayList<Point> getSimulationStartPositions() {
        return simulationStartPositions;
    }

    /**
     * Clones the world to be used by the multithreaded simulation
     * @return The cloned world
     */
    public World clone() {
        World world = new World();
        world.xTileSize = xTileSize;
        world.yTileSize = yTileSize;
        world.obstacles = new ArrayList<Obstacle>();
        for (Obstacle obstacle : obstacles)
            world.obstacles.add(obstacle.clone());
        world.items = new ArrayList<Item>();
        for (Item item : items)
            world.items.add(item.clone());
        world.waters = new ArrayList<Water>();
        for (Water water : waters)
            world.waters.add(water.clone());
        world.gui = gui;
        world.evolver = evolver;
        world.generationSettings = generationSettings;
        world.simulationStartPositions = new ArrayList<Point>();
        for (Point point : simulationStartPositions)
            world.simulationStartPositions.add(point);
        world.random = random;
        world.simulationSpeed = simulationSpeed;
        world.autoMode = autoMode;
        world.simulationStartPosition = simulationStartPosition;
        world.render = false;
        return world;
    }
}
