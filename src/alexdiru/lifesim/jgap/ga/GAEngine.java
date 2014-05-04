package alexdiru.lifesim.jgap.ga;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import alexdiru.lifesim.main.Evolver;
import alexdiru.lifesim.main.SimulationSettings;
import org.apache.commons.io.FileUtils;
import org.jgap.*;

import alexdiru.lifesim.main.World;
import org.jgap.impl.IntegerGene;

/**
 * Manages the genetic algorithms of the project
 * @author Alex
 *
 */
public class GAEngine implements Serializable {

	/**
	 * The population of the chromosomes (which are attached to life forms and control their properties)
	 */
	private Genotype gaGenotype;
	
	/**
	 * The world where the simulation is held
	 */
	private World world;

    private Evolver evolver;


    private boolean stopSimulation = false;
    private Configuration configuration;

    public boolean isStopSimulation() {
        return stopSimulation;
    }

	/**
	 * Stores the fittest chromosome of a generation
	 */
	private IChromosome fittest = null;
	
	public GAEngine(Evolver evolver, World world) {
		super();
        this.evolver = evolver;
		this.world = world;
	}

	/**
	 * Sets up the genotype, on creation failure the program will exit
	 */
	public void setup() {

        System.out.println("Initial Chromosome File: " + SimulationSettings.initialChromosomeFile);

        if (!SimulationSettings.initialChromosomeFile.equals("")) {
            loadPopulation(SimulationSettings.initialChromosomeFile);
            return;
        }

		try {
            stopSimulation = true;

            if (world.getGui().getEvolverThread() != null)
                while (world.getGui().getEvolverThread().isAlive());

			Configuration.reset();
            world.reset();

            world.regenerateWorld();
			gaGenotype = Genotype.randomInitialGenotype(new GAConfiguration(evolver,world));

            if (world.getGui().getSimulationSummaryPanel() != null)
                world.getGui().getSimulationSummaryPanel().refreshTable();

            stopSimulation = false;
            world.getGui().startWorld(false);
		} catch (InvalidConfigurationException e) {
			System.out.println("Genotype failed to create");
			System.exit(1);
		}
	}


    public void setup(List<Gene[]> genes) {

        System.out.println("Loading from set genes");
        stopSimulation = true;

        Configuration.reset();
        SimulationSettings.populationSize = genes.size();
        Configuration configuration = new GAConfiguration(evolver,world);
        evolver.setNumberOfLifeFormsSimulatedThisGeneration(0);


        try {

            gaGenotype = Genotype.initialGenotype(configuration, genes);

            /*System.out.println("Pre setup");
            for (int i = 0; i < SimulationSettings.populationSize; i++) {
                System.out.println("Setting up: " + i);
                gaGenotype.getPopulation().getChromosome(i).setGenes(genes.get(i));

                for (int j = 0; j < genes.get(i).length; j++) {
                    if (genes.get(i)[j] instanceof IntegerGene) {
                        Gene a = chromosome.getGene(j);
                        Gene b = genes.get(i)[j];
                        a.setAllele(b.getAllele());
                    } else {
                        ICompositeGene cg = (ICompositeGene)genes.get(i)[j];
                        ICompositeGene kg = (ICompositeGene)chromosome.getGene(j);
                        for (int k = 0; k < cg.size(); k++) {
                            kg.geneAt(k).setAllele(cg.geneAt(k).getAllele());
                        }
                    }
                }

                gaGenotype.getPopulation().setChromosome(i,chromosome);
            }*/

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (world.getGui().getSimulationSummaryPanel() != null)
            world.getGui().getSimulationSummaryPanel().refreshTable();

        stopSimulation = false;
    }
	
	/**
	 * Evolves the genotype: stores the current fittest life form, simulates all the chromosomes, adds the fittest life form and the new population is created
	 */
	public void evolve() {


        try {
            fittest = getFittestChromosome(gaGenotype.getPopulation());
            evolver.getWorld().getGui().setFittestGenes(fittest.getGenes());
            evolver.getWorld().getGui().setTree(GeneManager.createTreeModel(fittest.getGenes()));
        } catch (Exception ex) {

        }
		
		//Manually reset all population because I don't trust JGAP
		//resetPopulationFitness(gaGenotype.getConfiguration(), gaGenotype.getPopulation().getChromosomes());
       // for (IChromosome chromosome : gaGenotype.getPopulation().getChromosomes()) {
       //     if (chromosome != null) {
        //        chromosome.resetOperatedOn();
        //        chromosome.setFitnessValueDirectly(FitnessFunction.NO_FITNESS_VALUE);
        //    }
        //}

		//Deterministic without

        System.out.println("evolving");
        gaGenotype.evolve();

        try {
            if (!gaGenotype.getPopulation().contains(fittest) && fittest != null    ) {
                //gaGenotype.getPopulation().getChromosomes().add(fittest);
            }
        } catch (Exception e) {

        }
	}

    private IChromosome getFittestChromosome(Population p) {
        return GeneManager.getFittest(p.getChromosomes());
    }

    private Chromosome createFittestChromosome(Gene[] genes) {
        Chromosome fittest = null;
        try {
            fittest = new Chromosome(gaGenotype.getConfiguration());
            fittest.setGenes(genes);
            world.getGui().setFittestGenes(fittest.getGenes());
            world.getGui().setTree(GeneManager.createTreeModel(genes));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return fittest;
    }

    public static Chromosome resetChromosome(final IChromosome c) {
        try {
            Chromosome newChromosome = new Chromosome(c.getConfiguration());
            newChromosome.setGenes(c.getGenes().clone());
            return newChromosome;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void resetPopulationFitness(Configuration configuration, List<IChromosome> chromosomes) {
        synchronized (chromosomes) {
            for (int i = chromosomes.size() - 1; i >= 0; i--) {
                Chromosome c = resetChromosome(chromosomes.get(i));
                chromosomes.remove(i);
                chromosomes.add(c);
            }
        }
	}

	public Genotype getGenotype() {
		return gaGenotype;
	}

    public void stopSimulation() {
        stopSimulation = true;
    }

    public Configuration getConfiguration() {
        return gaGenotype.getConfiguration();
    }

    public void restart() {
        try {
            stopSimulation = true;

            if (world.getGui().getEvolverThread() != null)
                while (world.getGui().getEvolverThread().isAlive());

            Configuration.reset();

            //world.regenerateWorld();
            gaGenotype = Genotype.randomInitialGenotype(new GAConfiguration(evolver,world));

            if (world.getGui().getSimulationSummaryPanel() != null)
                world.getGui().getSimulationSummaryPanel().refreshTable();

            stopSimulation = false;
            world.getGui().startWorld(false);
        } catch (InvalidConfigurationException e) {
            System.out.println("Genotype failed to create");
            System.exit(1);
        }
    }

   public void loadPopulation(String absolutePath) {
        System.out.println("here");
        List<Gene[]> chromosomes = new ArrayList<Gene[]>();

        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;

        }


        System.out.println("Splitting cells");
        for (String line : lines) {
            String[] cells = line.split(",");

            chromosomes.add(GeneManager.readGenes(cells));

        }

        setup(chromosomes);
    }

    public void stopThreads() {
        ((ConcurrentBreeder)configuration.getBreeder()).stopThreads();
    }
}
