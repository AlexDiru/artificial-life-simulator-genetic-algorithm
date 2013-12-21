package alexdiru.lifesim.jgap.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.junit.Test;

public class GAEngineTest extends TestCase{
    @SuppressWarnings("deprecation")
	@Test
	public void testResetPopulationFitness() throws InvalidConfigurationException {
		List<IChromosome> chromosomes = new ArrayList<IChromosome>();
		Configuration configuration = new DefaultConfiguration();
		Random random = new Random();
		
		for (int i = 0; i < 100; i++) {
			chromosomes.add(new Chromosome(configuration));
			chromosomes.get(i).setFitnessValue((double)random.nextInt(10000));
		}

		GAEngine.resetPopulationFitness(configuration, chromosomes);
		
		for (IChromosome chromosome : chromosomes) 
			Assert.assertTrue(chromosome.getFitnessValue() == FitnessFunction.NO_FITNESS_VALUE);
	}
}
