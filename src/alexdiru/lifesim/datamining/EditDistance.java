package alexdiru.lifesim.datamining;

import alexdiru.lifesim.enums.DecisionNodeType;
import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.DecisionNode;
import alexdiru.lifesim.jgap.ga.GeneManager;
import convenience.RTED;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19/02/14
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class EditDistance {

    public static double compute(CATBehaviourTree a, CATBehaviourTree b) {
        double editDistance = RTED.computeDistance(convertToRTED(a.getRoot()), convertToRTED(b.getRoot()));
        System.out.println(convertToRTED(a.getRoot()));
        System.out.println(convertToRTED(b.getRoot()));
        System.out.println("ED: " + editDistance);
        System.out.println("MED: " + getMaxEditDistance());
        return editDistance/getMaxEditDistance();
    }

    public static double getMaxEditDistance() {
        return GeneManager.TREE_NODE_DEPTH * 2;
    }

    public static String convertToRTED(DecisionNode a) {
        if (a == null || a.isRedundant())
            return "";

        String data = "{";
        int value = a.getFunction();

        if (a.getType() == DecisionNodeType.ACTION)
            value += DecisionNode.CONDITION_NUMBER;
        else if (a.getType() == DecisionNodeType.TERMINAL)
            value += DecisionNode.CONDITION_NUMBER + DecisionNode.TERMINAL_NUMBER;

        data += value;
        data += convertToRTED(a.getLeft());
        data += convertToRTED(a.getRight());
        data += "}";

        return data;
    }

}
