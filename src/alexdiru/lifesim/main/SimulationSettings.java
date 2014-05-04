package alexdiru.lifesim.main;

import alexdiru.lifesim.datamining.DistanceMatrix;
import alexdiru.lifesim.jgap.ga.GeneManager;
import org.jgap.IChromosome;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SimulationSettings {

	//Fitness = Stamina + (MaxUsedEnergy - UsedEnergy)
	public static int maxUsedEnergy = 20000;
	
	//Energy -= MoveDistance * Factor
	public static float moveEnergyDecreaseFactor = 0.25f;
	
	//Energy -= TurnAngle * Factor
	public static float turnEnergyDecreaseFactor = 0.25f;
	
	public static boolean useDifferentMapForEachGeneration = false;
	
	public static int simulationPositions = 3;
	
	public static int populationSize = 50;

	public static int maxGeneration = 10000;

    //public static boolean automaticallyPickupFood = true;


    /**
     * The percentage chance two chromosomes are crossed over at evolution stage
     */
    public static int crossoverRate = 20;

    /**
     * The chance a chromosome has for it's senses to mutate (regular gene mutation), = 1/mutationRate
     */
    public static int senseMutationRate = 4;

    /**
     * The chance a chromosome has for it's behaviour to mutate = 1/mutationRate
     */
    public static int behaviourMutationRate = 3;

    public static final int RANDOM_SEED = 1;

    /**
     * Directory to store simulation files in
     */
    private static String simulationDirectory = "";
    public static String initialChromosomeFile = "";

    public static void setupNewSimulationDirectory() {
        String timeStamp = new Timestamp((new Date()).getTime()).toString();
        timeStamp = timeStamp.replace("-","").replace(" ", "").replace(":","").replace(".","");
        simulationDirectory = "SimulationData/Simulation" + timeStamp + "/";

        File file = new File(simulationDirectory);
        file.mkdirs();
    }

    public static void saveData(Evolver evolver, int generation) {
        List<IChromosome> currentPopulation = evolver.getGeneticEngine().getGenotype().getPopulation().getChromosomes();
        GeneManager.saveChromosomes(simulationDirectory + "chrom" + generation + ".txt", currentPopulation);
        GeneManager.saveFittestChromosomeAsGeneration(simulationDirectory + "fittest" + generation + ".txt", currentPopulation);
        DistanceMatrix.saveDistanceMatrix(simulationDirectory + "distmat" + generation + ".txt", currentPopulation);
    }

    public static void saveWorld(World world, List<IChromosome> initialChromosomes) {
        GeneManager.storeInitialChromosomes(initialChromosomes);
        world.save(simulationDirectory + "map.txt");
    }
}
