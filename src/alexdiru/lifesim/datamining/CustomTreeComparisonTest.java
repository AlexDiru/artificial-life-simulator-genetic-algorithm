package alexdiru.lifesim.datamining;

import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.DecisionNode;
import alexdiru.lifesim.jgap.ga.GeneManager;

import junit.framework.TestCase;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19/02/14
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public class CustomTreeComparisonTest extends TestCase {

    @Test
    public void testComputeDistance() throws InvalidConfigurationException {
        DecisionNode.ACTION_NUMBER = 3;
        DecisionNode.CONDITION_NUMBER = 4;
        DecisionNode.TERMINAL_NUMBER = 5;
        GeneManager.TREE_NODE_DEPTH = 2;
        GeneManager.TREE_NODE_SIZE = 7;
        testDistance(new int[]{0, 1, 2, 3, 4, 5, 6}, new int[]{0, 1, 2, 3, 4, 5, 6}, 0);
    }

    private void testDistance(int[] a, int[] b, double distance) throws InvalidConfigurationException {
        Configuration.reset();
        Configuration configuration = new DefaultConfiguration();
        CompositeGene ga = new CompositeGene(configuration);
        CompositeGene gb = new CompositeGene(configuration);

        for (int i = 0; i < GeneManager.BEHAVIOUR_TREE_INDEX; i++) {
            IntegerGene iga = new IntegerGene(configuration);
            iga.setAllele(0);
            ga.addGene(iga);

            IntegerGene igb = new IntegerGene(configuration);
            igb.setAllele(0);
            gb.addGene(igb);
        }

        for (int i = 0; i < a.length; i++) {
            IntegerGene iga = new IntegerGene(configuration);
            iga.setAllele(a[i]);
            ga.addGene(iga);

            IntegerGene igb = new IntegerGene(configuration);
            igb.setAllele(b[i]);
            gb.addGene(igb);
        }

        Assert.assertEquals(distance, CustomTreeComparison.computeDistance(ga, gb), 0.005);
    }
}
