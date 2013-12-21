package alexdiru.lifesim.jgap.ga;

import junit.framework.TestCase;
import org.jgap.Configuration;
import org.jgap.ICompositeGene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class CATBehaviourTreeTest extends TestCase {

    private void testAlleles(int[] alleles) throws InvalidConfigurationException {
        Configuration.reset();
        Configuration configuration = new DefaultConfiguration();
        ICompositeGene g = new CompositeGene(configuration);
        for (int i = 0; i < alleles.length; i++) {
            IntegerGene ig = new IntegerGene(configuration);
            ig.setAllele(alleles[i]);
            g.addGene(ig);
        }

        CATBehaviourTree tree = CATBehaviourTree.createFromSpecificGenes(null, CATBehaviourTree.convertCompositeGeneToList(g));
        List<DecisionNode> nodes = tree.convertToNodes();
        tree.toCompositeGene(configuration, nodes, g, new StockRandomGenerator());

        print(alleles);
        print(getAlleles(g));
        Assert.assertArrayEquals(alleles, getAlleles(g));
    }

    @Test
    public void testGeneToTreeToGeneConversion() throws InvalidConfigurationException {
        DecisionNode.ACTION_NUMBER = 3;
        DecisionNode.CONDITION_NUMBER = 4;
        DecisionNode.TERMINAL_NUMBER = 5;
        GeneManager.TREE_NODE_DEPTH = 2;
        GeneManager.TREE_NODE_SIZE = 7;
        testAlleles( new int[] { 0 , 1,2,3,4,5,6 });
        testAlleles( new int[] {6,5,4,3,2,1,0 });
        testAlleles( new int[] {6,5,4,4,0,3,0 });
        GeneManager.TREE_NODE_DEPTH = 3;
        GeneManager.TREE_NODE_SIZE = 15;
        testAlleles( new int[] {6,5,4,4,0,3,0,6,5,4,4,0,3,0,2 });
        GeneManager.TREE_NODE_DEPTH = 4;
        GeneManager.TREE_NODE_SIZE = 31;
        testAlleles( new int[] {6,5,4,4,0,3,0,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2 });
        GeneManager.TREE_NODE_DEPTH = 5;
        GeneManager.TREE_NODE_SIZE = 63;
        testAlleles( new int[] {6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2 });
        GeneManager.TREE_NODE_DEPTH = 6;
        GeneManager.TREE_NODE_SIZE = 63;
        testAlleles( new int[] {6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,0 });
        GeneManager.TREE_NODE_DEPTH = 7;
        GeneManager.TREE_NODE_SIZE = 127;
        testAlleles( new int[] {6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,0,5,6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,4,6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,1,4,4,0,3,0,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,6,5,4,4,0,3,0,2, 4, 6,5,4,4,0,3,0,6,5,4,4,0,3,0,2,0 });
    }

    private void print(int[] arr) {
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++)
            System.out.print(arr[i] + " ");
        System.out.print("]\n");
    }

    private int[] getAlleles(ICompositeGene gene) {
        int[] a = new int[gene.size()];
        for (int i = 0; i < gene.size(); i++)
            a[i] = (Integer)gene.geneAt(i).getAllele();
        return a;
    }
}