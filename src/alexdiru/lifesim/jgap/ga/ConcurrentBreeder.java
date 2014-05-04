package alexdiru.lifesim.jgap.ga;

import alexdiru.lifesim.helpers.ThreadHelper;
import alexdiru.lifesim.main.Evolver;
import alexdiru.lifesim.main.SimulationSettings;
import alexdiru.lifesim.main.World;
import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.impl.GABreeder;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to simulate life forms concurrently, set in the JGAP Configuration
 */
public class ConcurrentBreeder extends GABreeder {

    /**
     * The number of threads to use
     */
    private int numThreads;

    /**
     * The fitness functions to run in each thread
     */
    private GAConcurrentFitnessFunction[] fitnessFunctions;

    /**
     * The threads to run the simulations on
     */
    private Thread[] threads;


    /**
     *
     * @param world The world to run the simulations in
     * @param numThreads The number of threads to use
     */
    public ConcurrentBreeder(Evolver evolver, World world, int numThreads) {
        super();
        this.numThreads = numThreads;

        //Sometimes the constructor is called with this values as null - a temporary configuration used to appease
        //JGAP, if these values are null, there is no point creating any threads so we stop here
        if (world == null || evolver == null)
            return;

        //Create the fitness functions
        fitnessFunctions = new GAConcurrentFitnessFunction[numThreads];
        threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            fitnessFunctions[i] = new GAConcurrentFitnessFunction(evolver, world.clone(), null, false);
        }
    }

    /**
     * Override GABreeder's updateChromosomes, sets the fitness of the chromosomes passed to it
     * @param a_pop The population to set the fitness of
     * @param a_conf The configuration the simulation has
     */
    public void updateChromosomesConcurrently(Population a_pop, Configuration a_conf) {
        if(a_pop.size() != SimulationSettings.populationSize) {
            return;
        }

        System.out.println(a_pop.size());

        //Estimate the size of the population subset each thread will use
        int popSize = a_pop.size()/numThreads;

        for (int i = 0; i < numThreads; i++) {
            //Get a subset of the population for each thread to evaluate
            List<IChromosome> population = new ArrayList<IChromosome>();
            if (i != numThreads - 1)
                for (int c = i * popSize; c < (i + 1) * popSize; c++)
                    population.add((IChromosome)a_pop.getChromosome(c).clone());
            else
                for (int c = i * popSize; c < a_pop.size(); c++)
                    population.add((IChromosome)a_pop.getChromosome(c).clone());

            //Run the thread on the population subset
            fitnessFunctions[i].setPopulation(population);
            threads[i] = new Thread(fitnessFunctions[i]);
            threads[i].run();
        }

        //Join all the threads to make sure the simulating is done
        ThreadHelper.waitForThreads(threads);
    }

    public void stopThreads() {
        for (int i = 0; i < numThreads; i++)
            if (threads[i] != null)
                threads[i].stop();
    }
}