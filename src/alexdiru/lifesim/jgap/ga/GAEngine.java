package alexdiru.lifesim.jgap.ga;

import java.io.Serializable;
import java.util.List;

import alexdiru.lifesim.main.Evolver;
import org.jgap.*;

import alexdiru.lifesim.main.World;

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
            world.getGui().startWorld();
		} catch (InvalidConfigurationException e) {
			System.out.println("Genotype failed to create");
			System.exit(1);
		}
	}

    /**
     * Sets up the simulation applying a specified set of genes to each chromosome
     * @param genes
     */
    public void setup(Gene[] genes) {
        stopSimulation = true;

        if (world.getGui().getEvolverThread() != null)
            while (world.getGui().getEvolverThread().isAlive());

        Configuration.reset();
        world.reset();

        if (world.getGui().getSimulationSummaryPanel() != null)
            world.getGui().getSimulationSummaryPanel().refreshTable();

        world.regenerateWorld();
        try {
            gaGenotype = Genotype.randomInitialGenotype(new GAConfiguration(evolver,world));
            for (IChromosome c : gaGenotype.getPopulation().getChromosomes())
              c.setGenes(genes.clone());
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (world.getGui().getSimulationSummaryPanel() != null)
            world.getGui().getSimulationSummaryPanel().refreshTable();

        stopSimulation = false;
        world.getGui().startWorld();
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
        double fitness = -1;
        IChromosome fit = null;
        for (IChromosome c : p.getChromosomes())
            if (c.getFitnessValueDirectly() > fitness) {
                fitness = c.getFitnessValueDirectly();
                fit = c;
            }
        return fit;
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

    public void loadFittest(String filePath) {
        fittest = createFittestChromosome(GeneManager.loadFromFile(gaGenotype.getConfiguration(), filePath));
        setup(fittest.getGenes());
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
}
