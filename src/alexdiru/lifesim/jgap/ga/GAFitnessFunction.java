package alexdiru.lifesim.jgap.ga;

import org.jgap .FitnessFunction;
import org.jgap.IChromosome;

import alexdiru.lifesim.main.World;

/**
 * This is the fitness function required for the artificial life engine to evaluate the fitness of a generation
 * @author Alex
 *
 */
public class GAFitnessFunction extends FitnessFunction{

	private static final long serialVersionUID = 1L;
	
	/**
	 * The world that the simulation takes place in
	 */
	private World world;
	
	public GAFitnessFunction(World world) {
		this.world = world;
	}

	@Override
	protected double evaluate(IChromosome a_subject) {
        //synchronized (world) {
            return world.simulate(a_subject);
        //}
	}
}
