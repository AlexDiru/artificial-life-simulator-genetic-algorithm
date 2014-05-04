package alexdiru.lifesim.jgap.ga;

import alexdiru.lifesim.main.Evolver;
import alexdiru.lifesim.main.World;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 29/11/13
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class GAConcurrentFitnessFunction implements Runnable {

    private List<IChromosome> population;
    private int startIndex;
    private int endIndex;
    private World world;
    private Evolver evolver;
    private boolean render;

    public GAConcurrentFitnessFunction(Evolver evolver, World world, List<IChromosome> population, boolean render) {
        this.world = world;
        this.evolver = evolver;
        this.population = population;
        this.render = render;
    }

    public void setPopulation(List<IChromosome> population) {
        this.population = population;
    }

    @Override
    public void run() {
        for (IChromosome c : population) {
            c.getFitnessValue();
        }

    }

    public Population getPopulation() {
        try {
            Population p = new Population(population.get(0).getConfiguration());
            for (IChromosome c : population)
                p.addChromosome(c);
            return p;
        } catch (InvalidConfigurationException e) {
            return null;
        }
    }
}
