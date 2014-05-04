package alexdiru.lifesim.datamining;

import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.DecisionNode;
import alexdiru.lifesim.jgap.ga.GeneManager;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.impl.CompositeGene;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19/02/14
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class CustomTreeComparison {
    /**
     * Computes the difference between two behaviour trees
     * 0 <= distance < 1
     * @param a
     * @param b
     * @return
     */
    public static double computeDistance(IChromosome a, IChromosome b) {
        return computeDistance((CompositeGene)a.getGenes()[GeneManager.BEHAVIOUR_TREE_INDEX], (CompositeGene)b.getGenes()[GeneManager.BEHAVIOUR_TREE_INDEX]);
    }


    public static double compute(Gene[] aGenes, Gene[] bGenes) {
        return computeDistance((CompositeGene)aGenes[GeneManager.BEHAVIOUR_TREE_INDEX], (CompositeGene)bGenes[GeneManager.BEHAVIOUR_TREE_INDEX]);
    }

    static double computeDistance(CompositeGene a, CompositeGene b) {
        double maxPenalty = GeneManager.TREE_NODE_DEPTH * 2 + 1;

        //Same node type but different function, penalty = 1
        //Different node type, penalty = 2
        double penalty = 0;

        //Variables to keep track of depth
        int currentDepth = 0;
        int nodesPerCurrentDepth = (int)Math.pow(2, currentDepth);
        int nodesCalculatedOnCurrentDepth = 0;

        //Since the nodes are in BFS order, they don't need to be converted to trees
        for (int currentIndex = GeneManager.BEHAVIOUR_TREE_INDEX; currentIndex < a.size(); currentIndex++) {
            DecisionNode nodeA = DecisionNode.create(a.geneAt(currentIndex));
            DecisionNode nodeB = DecisionNode.create(b.geneAt(currentIndex));

            if (nodeA.getType() != nodeB.getType())
                penalty += 2d/(double)nodesPerCurrentDepth;
            else if (nodeA.getFunction() != nodeB.getFunction())
                penalty += 1/(double)nodesPerCurrentDepth;

            //Swap the nodes in the current row if neccessary
            DecisionNode parentA = nodeA.getParent();
            DecisionNode parentB = nodeB.getParent();
            try {
                if (parentA != null && parentB != null)
                    if (parentA.getLeft().getLeft().isTheSameAs(parentB.getRight().getLeft()))
                        if (parentA.getLeft().getRight().isTheSameAs(parentB.getRight().getRight()))
                            if (parentA.getRight().getLeft().isTheSameAs(parentB.getLeft().getLeft()))
                                if (parentA.getRight().getRight().isTheSameAs(parentB.getLeft().getRight()))
                                    parentB.swapChildren();

            } catch (NullPointerException ex) {

            }


            //Update depth
            nodesCalculatedOnCurrentDepth++;
            if (nodesCalculatedOnCurrentDepth == nodesPerCurrentDepth) {
                currentDepth++;
                nodesPerCurrentDepth = (int)Math.pow(2, currentDepth);
                nodesCalculatedOnCurrentDepth = 0;
            }
        }

        return penalty/maxPenalty;
    }
}
