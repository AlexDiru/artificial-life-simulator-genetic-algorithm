package alexdiru.lifesim.datamining;

import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.GeneManager;
import org.apache.commons.io.FileUtils;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DistanceMatrix {

    /**
     * Computes a distance matrix of the similarity of each chromosome
     * The matrix produced is lower triangle and includes the diagonal
     * Also includes row header and column header
     * @param chromosomes The chromosomes to calculate the distance of
     * @return The distance matrix, columns delimited by commas, rows delimited by newlines
     */
    public static String computeDistanceMatrix(final List<IChromosome> chromosomes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cluster,");
        for (int i = 0; i < chromosomes.size(); i++) {
            sb.append("C").append(i);
            if (i != chromosomes.size() - 1)
                sb.append(",");
        }
        sb.append("\n");

        for (int i = 0; i < chromosomes.size(); i++) {
            sb.append("C").append(i).append(",");
            for (int j = 0; j <= i; j++) {
                sb.append(computeDistance(chromosomes.get(i), chromosomes.get(j)));
                if (j != i)
                    sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void saveDistanceMatrix(String filePath, final List<IChromosome> chromosomes) {
        try {
            FileUtils.write(new File(filePath), computeDistanceMatrix(chromosomes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes the similarity between two chromosomes where 0 <= distance < 1
     * @param a
     * @param b
     * @return
     */
    private static double computeDistance(final IChromosome a, final IChromosome b) {
        //Compute sense distance
        double senseDistance = 0;
        for (int i = 0; i < GeneManager.BEHAVIOUR_TREE_INDEX; i++)
            senseDistance += computeDistance((IntegerGene)(a.getGenes()[i]),(IntegerGene)(b.getGenes()[i]));

        senseDistance /= GeneManager.BEHAVIOUR_TREE_INDEX;

        double behaviourDistance = 0;
        behaviourDistance = CustomTreeComparison.computeDistance(a, b);

        return (senseDistance + behaviourDistance)/2d;
    }

    private static double computeDistance(final IntegerGene a, final IntegerGene b) {
        int possibleValues = a.getUpperBounds() - a.getLowerBounds();
        return (double)Math.abs((Integer)a.getAllele() - (Integer)b.getAllele())/possibleValues;
    }

}
