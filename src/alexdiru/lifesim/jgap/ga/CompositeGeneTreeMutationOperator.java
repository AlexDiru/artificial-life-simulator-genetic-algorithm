package alexdiru.lifesim.jgap.ga;

import org.jgap.*;
import org.jgap.data.config.Configurable;
import org.jgap.impl.MutationOperator;

import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 18/12/13
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class CompositeGeneTreeMutationOperator extends MutationOperator implements Configurable {
    public CompositeGeneTreeMutationOperator(Configuration a_conf) throws InvalidConfigurationException {
        super(a_conf);
    }

    public CompositeGeneTreeMutationOperator(Configuration a_config, IUniversalRateCalculator a_mutationRateCalculator) throws InvalidConfigurationException {
        super(a_config, a_mutationRateCalculator);
    }

    public CompositeGeneTreeMutationOperator(Configuration a_config, int a_desiredMutationRate) throws InvalidConfigurationException {
        super(a_config, a_desiredMutationRate);
    }

    public ICompositeGene mutateCompositeGene(ICompositeGene a_gene, final RandomGenerator a_generator) {
        ICompositeGene randomMutation = GeneManager.generateRandomBehaviourTree(a_gene.getConfiguration(), a_generator);
        CATBehaviourTree.crossover(a_gene, randomMutation, a_generator,true);
        return a_gene;
    }

    public void mutateChromosome(Gene[] genes, final RandomGenerator generator) {
        //Pick a gene by random
        Gene gene = genes[generator.nextInt(genes.length)];
        if (gene instanceof ICompositeGene)
            gene = mutateCompositeGene((ICompositeGene)gene, generator);
        else
            mutateGene(gene, generator);
    }
}
