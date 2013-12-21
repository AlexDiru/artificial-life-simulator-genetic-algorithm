package alexdiru.lifesim.jgap.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgap.*;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.IntegerGene;

import alexdiru.lifesim.helpers.FileHelper;

import javax.swing.tree.DefaultMutableTreeNode;

public class GeneManager {
    private static final int MINIMUM_MEMORY_LENGTH = 1;
    private static final int MAXIMUM_MEMORY_LENGTH = 40;
    private static final int MINIMUM_SIGHT_DISTANCE = 50;
    private static final int MAXIMUM_SIGHT_DISTANCE = 800;
    private static final int MINIMUM_SPEED = 4;
    private static final int MAXIMUM_SPEED = 30;
    private static final int MINIMUM_TURN_SPEED = 10;
    private static final int MAXIMUM_TURN_SPEED = 90;
    private static final int MINIMUM_SIGHT_FOV = 45;
    private static final int MAXIMUM_SIGHT_FOV = 270;
    private static final int MINIMUM_REACH_DISTANCE = 1;
    private static final int MAXIMUM_REACH_DISTANCE = 32;

    private static boolean IS_TILE_BASED = true;

    public static int TREE_NODE_DEPTH = 7;
	public static int TREE_NODE_SIZE = (int)Math.pow(2,7) - 1;
    public static final int BEHAVIOUR_TREE_INDEX = 6;

	public static void saveToFile(String filePath, Gene[] genes) {
		FileHelper.writeAllLines(filePath, getTextList(genes));
	}
	
	private static List<String> getTextList(Gene[] genes) {
		List<String> list = new ArrayList<String>();
		for (Gene gene : genes)
			if (gene instanceof IntegerGene)
				list.add(""+(Integer)gene.getAllele());
		return list;
	}
	
	public static Gene[] loadFromFile(Configuration configuration, String filePath) {
		List<String> lines = FileHelper.readAllLines(filePath);
		Gene[] genes = createSampleChromosome(configuration);

		try {
			for (int i = 0; i < lines.size(); i++) {
				genes[i].setAllele(Integer.parseInt(lines.get(i)));
			}
		} catch (NumberFormatException ex) {
			System.out.println("Cannot load corrupt file");
			ex.printStackTrace();
		}
		
		return genes;
	}

    public static DefaultMutableTreeNode createTreeModel(Gene[] genes) {
        CATBehaviourTree behaviourTree = CATBehaviourTree.createFromGenes(null, genes);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        node.setAllowsChildren(true);

        for (int i = 0; i < BEHAVIOUR_TREE_INDEX; i++) {
            DefaultMutableTreeNode geneNode = new DefaultMutableTreeNode(mapGeneIndexToName(i) + ": " + genes[i].getAllele());
            node.add(geneNode);
        }

        DefaultMutableTreeNode behaviourRoot = behaviourTree.getBehaviourTreeRoot();

        node.add(behaviourRoot);
        return node;
    }

    private static String mapGeneIndexToName(int i) {
        if (i == 0)
            return "Movement Speed";
        else if (i == 1)
            return "Turn Speed";
        else if (i == 2)
            return "Sight FOV";
        else if (i == 3)
            return "Sight Distance";
        else if (i == 4)
            return "Reach Distance";
        else if (i == 5)
            return "Memory Length";
        return "";
    }

    public static Gene[] createSampleChromosome(Configuration configuration) {
		ArrayList<Gene> genes = new ArrayList<Gene>();
		try {
			genes.add(new IntegerGene(configuration, MINIMUM_SPEED, MAXIMUM_SPEED));
			genes.add(new IntegerGene(configuration, MINIMUM_TURN_SPEED, MAXIMUM_TURN_SPEED));
			genes.add(new IntegerGene(configuration, MINIMUM_SIGHT_FOV, MAXIMUM_SIGHT_FOV));
			genes.add(new IntegerGene(configuration, MINIMUM_SIGHT_DISTANCE, MAXIMUM_SIGHT_DISTANCE));
			genes.add(new IntegerGene(configuration, MINIMUM_REACH_DISTANCE, MAXIMUM_REACH_DISTANCE));
			genes.add(new IntegerGene(configuration, MINIMUM_MEMORY_LENGTH, MAXIMUM_MEMORY_LENGTH));

            CompositeGene cg = new CompositeGene(configuration);
			for (int i = 0; i < TREE_NODE_SIZE; i++) {
                IntegerGene gene = new IntegerGene(configuration, 0, DecisionNode.getUniqueNodeCount() - 1);
				gene.setAllele(1);
                cg.addGene(gene);
            }
            genes.add(cg);

		} catch (InvalidConfigurationException e) {
			System.out.println("Error creating genes");
		}
		
		return genes.toArray(new Gene[0]);
	}

    static boolean geneEquality(Gene[] a, Gene[] b) {
        if (a.length != b.length)
            return false;

        for (int i = 0; i < a.length; i++)
            if (a[i].getAllele() != b[i].getAllele())
                return false;

        return true;
    }

    /**
     * Counts the unique chromosomes in a population
     * @param population
     * @return
     */
    public static int countUniqueGenes(final Population population) {
        List<IChromosome> uniqueChromosomes = new ArrayList<IChromosome>();
        uniqueChromosomes.addAll(Arrays.asList(population.toChromosomes()));

        for (int i = 0; i < uniqueChromosomes.size(); i++) {
            for (int j = i + 1; j < uniqueChromosomes.size(); j++) {
                IChromosome a = uniqueChromosomes.get(i);
                IChromosome b = uniqueChromosomes.get(j);

                if (a == null || b == null)
                    break;

                if (geneEquality(a.getGenes(), b.getGenes())) {
                    uniqueChromosomes.remove(j);
                    j--;
                }
            }
        }

        return uniqueChromosomes.size();
    }

    public static IntegerGene createRandomBehaviourTreeGene(Configuration configuration, RandomGenerator generator) {
        IntegerGene integerGene = null;
        try {
            integerGene = new IntegerGene(configuration);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        integerGene.setAllele(generator.nextInt( DecisionNode.getUniqueNodeCount()));
        return integerGene;
    }

    public static ICompositeGene generateRandomBehaviourTree(Configuration configuration, RandomGenerator generator) {
        ICompositeGene gene = null;
        try {
            gene = new CompositeGene(configuration);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < TREE_NODE_SIZE; i++)
            gene.addGene(createRandomBehaviourTreeGene(configuration, generator));
        return gene;

    }
}
