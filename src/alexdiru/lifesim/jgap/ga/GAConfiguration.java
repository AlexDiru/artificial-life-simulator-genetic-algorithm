	package alexdiru.lifesim.jgap.ga;
import alexdiru.lifesim.main.Evolver;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.*;

import alexdiru.lifesim.interfaces.IXMLConverter;
import alexdiru.lifesim.main.LifeFormSenses;
import alexdiru.lifesim.main.SimulationSettings;
import alexdiru.lifesim.main.World;

    /**
 * Configuration to produce the chromosomes for the genetic algorithm
 * @author Alex
 *
 */
public class GAConfiguration extends DefaultConfiguration implements IXMLConverter{
	
	private static final long serialVersionUID = 1L;
    private World world;

    /**
	 * Sets the mutation rate
	 * @param rate The mutation rate
	 */
	private void setMutationRate(int rate) {
		for (int i = 0; i < getGeneticOperators().size(); i++) {
			Object go = getGeneticOperators().get(i);
			if (go instanceof MutationOperator)
				((MutationOperator)go).setMutationRate(rate);
		}
	}
	
	/**
	 * Sets the crossover rate
	 * @param rate The crossover rate
	 */
	private void setCrossoverRate(int rate){
		for (int i = 0; i < getGeneticOperators().size(); i++) {
			Object go = getGeneticOperators().get(i);
			if (go instanceof CrossoverOperator)
				try {
                    getGeneticOperators().set(i, new CrossoverOperator(this, rate));
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
		}
	}

    public GAConfiguration() {
        this(null,null);
    }
	
	/**
	 * Creates the configuration
	 * @param world The world where the simulation takes place
	 */
	public GAConfiguration(Evolver evolver, World world) {
		super();
		try {
            setBreeder(new ConcurrentBreeder(evolver, world, 4));
			setPopulationSize(SimulationSettings.populationSize);
            setFitnessFunction(new GAFitnessFunction(world));
            //setBulkFitnessFunction(new GABulkFitnessFunction(world));
            setPreservFittestIndividual(false);
            setKeepPopulationSizeConstant(true);


            if (SimulationSettings.RANDOM_SEED != -1)
                setRandomGenerator(new SeededRandomGenerator(SimulationSettings.RANDOM_SEED));



            //Replace operators with tree operators
            getGeneticOperators().set(0, new CompositeGeneTreeCrossoverOperator(this, SimulationSettings.crossoverRate));
            getGeneticOperators().set(1, new CompositeGeneTreeMutationOperator(this, -1));

			//if (crossoverRate != -1)
			//	setCrossoverRate(crossoverRate);
			//if (mutationRate != -1)
			//	setMutationRate(mutationRate);

			setSampleChromosome(new Chromosome(this, GeneManager.createSampleChromosome(this)));
		} catch (InvalidConfigurationException e) {
			System.out.println("GAConfiguration() failed");
		}
	}
	
	@Override
	public String toXML() {
		StringBuilder xml = new StringBuilder();
		xml.append("<GAEngine>\n");
		//xml.append("<CrossoverRate>" + crossoverRate + "</CrossoverRate>\n");
		//xml.append("<MutationRate>" + mutationRate + "</MutationRate>\n");
		xml.append("</GAEngine>\n");
		return xml.toString();
	}

    public static void reset() {
        Configuration.reset();
    }

    public World getWorld() {
        return world;
    }
}
