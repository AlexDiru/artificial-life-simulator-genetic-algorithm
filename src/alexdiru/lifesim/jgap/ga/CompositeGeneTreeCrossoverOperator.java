package alexdiru.lifesim.jgap.ga;

import alexdiru.lifesim.main.SimulationSettings;
import org.apache.commons.lang.NotImplementedException;
import org.jgap.*;
import org.jgap.impl.CrossoverOperator;

import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 18/12/13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class CompositeGeneTreeCrossoverOperator extends CrossoverOperator {
    public CompositeGeneTreeCrossoverOperator() throws InvalidConfigurationException {
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration) throws InvalidConfigurationException {
        super(a_configuration);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, IUniversalRateCalculator a_crossoverRateCalculator) throws InvalidConfigurationException {
        super(a_configuration, a_crossoverRateCalculator);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, IUniversalRateCalculator a_crossoverRateCalculator, boolean a_allowFullCrossOver) throws InvalidConfigurationException {
        super(a_configuration, a_crossoverRateCalculator, a_allowFullCrossOver);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, int a_desiredCrossoverRate) throws InvalidConfigurationException {
        super(a_configuration, a_desiredCrossoverRate);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, int a_desiredCrossoverRate, boolean a_allowFullCrossOver) throws InvalidConfigurationException {
        super(a_configuration, a_desiredCrossoverRate, a_allowFullCrossOver);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, int a_desiredCrossoverRate, boolean a_allowFullCrossOver, boolean a_xoverNewAge) throws InvalidConfigurationException {
        super(a_configuration, a_desiredCrossoverRate, a_allowFullCrossOver, a_xoverNewAge);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, double a_crossoverRatePercentage) throws InvalidConfigurationException {
        super(a_configuration, a_crossoverRatePercentage);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, double a_crossoverRatePercentage, boolean a_allowFullCrossOver) throws InvalidConfigurationException {
        super(a_configuration, a_crossoverRatePercentage, a_allowFullCrossOver);
    }

    public CompositeGeneTreeCrossoverOperator(Configuration a_configuration, double a_crossoverRatePercentage, boolean a_allowFullCrossOver, boolean a_xoverNewAge) throws InvalidConfigurationException {
        super(a_configuration, a_crossoverRatePercentage, a_allowFullCrossOver, a_xoverNewAge);
    }


    public void operate(final Population a_population,
                        final List a_candidateChromosomes) {

        int numCrossovers = SimulationSettings.crossoverRate/100;
        RandomGenerator generator = getConfiguration().getRandomGenerator();
        IGeneticOperatorConstraint constraint = getConfiguration().getJGAPFactory().getGeneticOperatorConstraint();

        for (int i = 0; i < numCrossovers; i++) {
            //Get two random chromosomes
            IChromosome chrom1;
            IChromosome chrom2;
            do {
                chrom1 = a_population.getChromosome(generator.nextInt(a_population.size()));
                chrom2 = a_population.getChromosome(generator.nextInt(a_population.size()));
            } while (chrom1 != chrom2);

            // Verify that crossover is allowed.
            if (!isXoverNewAge() && chrom1.getAge() < 1 && chrom2.getAge() < 1) {
                // Crossing over two newly created chromosomes is not seen as helpful
                // here.
                // ------------------------------------------------------------------
                continue;
            }

            if (constraint != null) {
                List v = new Vector();
                v.add(chrom1);
                v.add(chrom2);
                if (!constraint.isValid(a_population, v, this)) {
                    // Constraint forbids crossing over.
                    // ---------------------------------
                    continue;
                }
            }

            doCrossover(chrom1, chrom2, a_candidateChromosomes, generator);
        }
    }

    private void regularCrossover(Gene[] firstGenes, Gene[] secondGenes, RandomGenerator generator) {
        Gene gene1;
        Gene gene2;
        Object firstAllele;

        int locus = generator.nextInt(GeneManager.BEHAVIOUR_TREE_INDEX - 1);

        for (int j = locus; j <GeneManager.BEHAVIOUR_TREE_INDEX; j++) {

            // Make a distinction for ICompositeGene for the first gene.
            // ---------------------------------------------------------
            if (firstGenes[j] instanceof ICompositeGene)
                throw new NotImplementedException();
            else
                gene1 = firstGenes[j];

            // Make a distinction for the second gene if CompositeGene.
            // --------------------------------------------------------
            if (secondGenes[j] instanceof ICompositeGene)
                throw new NotImplementedException();
            else
                gene2 = secondGenes[j];

            if (m_monitorActive) {
                gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
                gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
            }
            firstAllele = gene1.getAllele();
            gene1.setAllele(gene2.getAllele());
            gene2.setAllele(firstAllele);
        }
    }

   protected void doCrossover(IChromosome firstMate, IChromosome secondMate,
                               List a_candidateChromosomes,
                               RandomGenerator generator) {

        Gene[] firstGenes = firstMate.getGenes();
        Gene[] secondGenes = secondMate.getGenes();


        //A 50% chance we will swap the behaviour tree or the other parameters
        if (generator.nextInt(2) == 1) {
            //Swap tree
            treeCrossover(firstGenes, secondGenes, generator);
        } else {
            //Swap senses
            regularCrossover(firstGenes, secondGenes, generator);
        }

        // Add the modified chromosomes to the candidate pool so that
        // they'll be considered for natural selection during the next
        // phase of evolution.
        // -----------------------------------------------------------
        a_candidateChromosomes.add(firstMate);
        a_candidateChromosomes.add(secondMate);
    }

    private void treeCrossover(Gene[] firstGenes, Gene[] secondGenes, RandomGenerator generator) {
        ICompositeGene a = (ICompositeGene)firstGenes[GeneManager.BEHAVIOUR_TREE_INDEX];
        ICompositeGene b = (ICompositeGene)secondGenes[GeneManager.BEHAVIOUR_TREE_INDEX];
        CATBehaviourTree.crossover(a,b,generator,false);
    }
}
