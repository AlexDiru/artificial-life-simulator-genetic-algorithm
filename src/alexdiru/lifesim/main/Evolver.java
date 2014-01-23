package alexdiru.lifesim.main;

import alexdiru.lifesim.jgap.ga.GAEngine;
import alexdiru.lifesim.jgap.ga.GeneManager;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 30/11/13
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */
public class Evolver implements Runnable {
    /**
     * The engine to handle the evolution of the GA chromosomes
     */
    protected GAEngine geneticEngine;

    private GUI gui;

    private World world;

    /**
     * Stores the number of life forms simulated in the current generation
     * Since the simulation is multi-threaded this must be atomic
     */
    private static AtomicInteger numberOfLifeFormsSimulatedThisGeneration = new AtomicInteger();

    /**
     * Whether the thread is running or not
     */
    private boolean runEnded;

    public boolean hasRunEnded() {
        return runEnded;
    }

    public void setNumberOfLifeFormsSimulatedThisGeneration(int numberOfLifeFormsSimulatedThisGeneration) {
        this.numberOfLifeFormsSimulatedThisGeneration.set(numberOfLifeFormsSimulatedThisGeneration);
    }

    public int getNumberOfLifeFormsSimulatedThisGeneration() {
        return numberOfLifeFormsSimulatedThisGeneration.get();
    }

    /**
     * The current generation the life forms are at
     */
    protected int currentGeneration = 1;

    /**
     * To store the fitness of the life forms over generations, so the min/max/avg fitness of each generation can be graphed
     */
    protected GenerationDataset dataset = new GenerationDataset();

    public Evolver(GUI gui) {
        this.gui = gui;
        world = new World(gui, this);
        geneticEngine = new GAEngine(this, world);
    }

    public void reset() {
        geneticEngine.setup();
        dataset.reset();
        currentGeneration = 1;
    }


    public GenerationDataset getDataset() {
        return dataset;
    }

    /**
     * Runs the thread for the world
     * Until the set number of generations has been simulated:
     * Evolve a generation and when that is done, update the graphs and prepare for the next generation
     */
    @Override
    public void run() {


        runEnded = false;

        while(currentGeneration < SimulationSettings.maxGeneration && !geneticEngine.isStopSimulation()) {
           geneticEngine.evolve();

            dataset.add(getMinFitnessCurrentGeneration(), getAverageFitnessCurrentGeneration(), getMaxFitnessCurrentGeneration(), getVarFitnessCurrentGeneration(), geneticEngine.getGenotype().getPopulation().size(), GeneManager.countUniqueGenes(geneticEngine.getGenotype().getPopulation()));            //}


            if (geneticEngine.isStopSimulation())
                break;

            currentGeneration++;

            //Update the simulation data table if it's open
            if (gui.getSimulationSummaryPanel() != null)
                gui.getSimulationSummaryPanel().refreshTable();

            //Change map if necessary
            if (SimulationSettings.useDifferentMapForEachGeneration)
                world.regenerateWorld();

            //Prepare for the next generation
            world.prepareForNextGeneration();
        }

        runEnded = true;
    }

    /**
     * Gets the lowest fitness of a life form in this current generation
     * @return The lowest fitness
     */
    protected double getMinFitnessCurrentGeneration() {
        double minFitness = Double.MAX_VALUE;

        for (int i = 0; i < geneticEngine.getGenotype().getPopulation().getChromosomes().size(); i++) {
            IChromosome c = geneticEngine.getGenotype().getPopulation().getChromosome(i);

            if (c == null || c.getFitnessValueDirectly() == -1 || c.getFitnessValueDirectly() == FitnessFunction.NO_FITNESS_VALUE)
                continue;

            if (c.getFitnessValueDirectly() < minFitness)
                minFitness = c.getFitnessValueDirectly();
        }
        return minFitness;
    }
    /**
     * Gets the average fitness of all life forms this current generation
     * @return The average fitness
     */
    protected double getAverageFitnessCurrentGeneration() {
        double totalFitness = 0;

        for (int i = 0; i < geneticEngine.getGenotype().getPopulation().size(); i++) {
            IChromosome c = geneticEngine.getGenotype().getPopulation().getChromosome(i);

            if (c == null || c.getFitnessValueDirectly() == -1 || c.getFitnessValueDirectly() == FitnessFunction.NO_FITNESS_VALUE)
                continue;

            totalFitness += c.getFitnessValueDirectly();
        }

        return totalFitness/(double)SimulationSettings.populationSize;
    }

    /**
     * Gets the highest fitness of a life form in this current generation
     * @return The maximum fitness
     */
    protected double getMaxFitnessCurrentGeneration() {
        double maxFitness = 0;

        for (int i = 0; i < geneticEngine.getGenotype().getPopulation().size(); i++) {
            IChromosome c = geneticEngine.getGenotype().getPopulation().getChromosome(i);

            if (c == null || c.getFitnessValueDirectly() == -1 || c.getFitnessValueDirectly() == FitnessFunction.NO_FITNESS_VALUE)
                continue;

            if (c.getFitnessValueDirectly() > maxFitness)
                maxFitness = c.getFitnessValueDirectly();
        }
        return maxFitness;
    }

    /**
     * Gets the variance fitness of all life forms this current generation
     */
    private double getVarFitnessCurrentGeneration() {
        double average = getAverageFitnessCurrentGeneration();
        double sum = 0;

        for (int i = 0; i < geneticEngine.getGenotype().getPopulation().size(); i++) {
            IChromosome c = geneticEngine.getGenotype().getPopulation().getChromosome(i);

            if (c == null || c.getFitnessValueDirectly() == -1 || c.getFitnessValueDirectly() == FitnessFunction.NO_FITNESS_VALUE)
                continue;

            sum += (c.getFitnessValueDirectly() - average) * (c.getFitnessValueDirectly() - average);
        }

        return sum/(geneticEngine.getGenotype().getPopulation().size() - 1);
    }

    public World getWorld() {
        return world;
    }

    public GAEngine getGeneticEngine() {
        return geneticEngine;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public void incrementNumberOfLifeFormsSimulatedThisGeneration() {
        numberOfLifeFormsSimulatedThisGeneration.incrementAndGet();
    }
}
