package alexdiru.lifesim.jgap.ga;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alexdiru.lifesim.datamining.DistanceMatrix;
import alexdiru.lifesim.main.SimulationSettings;
import org.apache.commons.io.FileUtils;
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

    public static int TREE_NODE_DEPTH = 4;
	public static int TREE_NODE_SIZE = (int)Math.pow(2,4) - 1;
    public static final int BEHAVIOUR_TREE_INDEX = 6;

    private static List<IChromosome> initialChromosomes;

    public static void storeInitialChromosomes(List<IChromosome> chromosomes) {
        initialChromosomes = new ArrayList<IChromosome>();
        for (IChromosome chromosome : chromosomes)
            initialChromosomes.add((Chromosome)chromosome.clone());
    }

    public static void saveInitialChromosomes(String filePath) {
        saveChromosomes(filePath, initialChromosomes);
    }

    public static void saveChromosomes(String filePath, List<IChromosome> initialChromosomes) {
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (IChromosome chromosome : initialChromosomes) {
            sb.append("C").append(c++).append(",");
            for (int i = 0; i < chromosome.getGenes().length; i++){
                if (chromosome.getGenes()[i] instanceof IntegerGene) {
                    sb.append(((IntegerGene)chromosome.getGenes()[i]).getAllele());
                    if (i != chromosome.getGenes().length - 1)
                        sb.append(",");
                } else if (chromosome.getGenes()[i] instanceof CompositeGene) {
                    sb.append("composite,");
                    int compositeSize = ((CompositeGene)chromosome.getGenes()[i]).size();
                    for (int j = 0; j < compositeSize; j++) {
                        sb.append(((IntegerGene)((CompositeGene)chromosome.getGenes()[i]).geneAt(j)).getAllele());
                        if (j != compositeSize - 1)
                            sb.append(",");
                    }
                }
            }
            sb.append("\n");
        }

        try {
            FileUtils.write(new File(filePath), sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void saveFittestChromosomeAsGeneration(String filePath, List<IChromosome> chromosomes) {
        try {
        List<IChromosome> fittest = new ArrayList<IChromosome>();
        IChromosome fit = GeneManager.getFittest(chromosomes);
        for (int i = 0; i < SimulationSettings.populationSize; i++)
            fittest.add((IChromosome)fit.clone());
        saveChromosomes(filePath, fittest);
        } catch (NullPointerException e) {

        }
    }

    public static IChromosome getFittest(List<IChromosome> chromosomes) {
        double fitness = -1;
        IChromosome fit = null;
        for (IChromosome c : chromosomes)
            if (c.getFitnessValueDirectly() > fitness) {
                fitness = c.getFitnessValueDirectly();
                fit = c;
            }
        return fit;
    }

    public static Gene[] readGenes(String[] cells) {
        Configuration.reset();

        //cells[0] is the name so can be discarded

        int i = 1;
        List<Gene> genes = new ArrayList<Gene>();
        Configuration config = new GAConfiguration();

        try{
            while (!cells[i].equals("composite")) {
                IntegerGene ig = null;
                ig = new IntegerGene(config);
                ig.setAllele(Integer.parseInt(cells[i]));

                genes.add(ig);
                i++;
            }

            i++;

            CompositeGene cg = new CompositeGene(config);

            while (i != cells.length) {
                IntegerGene ig = null;
                try {
                    ig = new IntegerGene(config);
                    ig.setAllele(Integer.parseInt(cells[i]));
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                cg.addGene(ig);
                i++;
            }

            genes.add(cg);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return genes.toArray(new Gene[genes.size()]);
    }

    public static void loadInitialChromosomes(Configuration configuration, String filePath, boolean namePrefix) throws InvalidConfigurationException {
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Gene[]> geneList = new ArrayList<Gene[]>();
        int i =0;

        for (String line : lines) {
            if (line.equals(""))
                continue;

            String[] cells= line.split(",");


            geneList.add(createSampleChromosome(configuration));

            int j = 0;
            int c = namePrefix ? 1 : 0;

            while (!cells[c].equals("composite")) {
                geneList.get(i)[j].setAllele(Integer.parseInt(cells[c]));
                j++;
                c++;
            }
            int k = 0;
            c++;
            while (c < cells.length) {
                ((CompositeGene)geneList.get(i)[j]).geneAt(k).setAllele(Integer.parseInt(cells[c]));
                k++;
                c++;
            }

            i++;
        }


        initialChromosomes = new ArrayList<IChromosome>();
        for (Gene[] genes : geneList) {
            Chromosome chromosome = new Chromosome(configuration);
            chromosome.setGenes(genes);
            initialChromosomes.add(chromosome);
        }
    }

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

    public static DefaultMutableTreeNode createTreeModel(CATBehaviourTree behaviourTree, Gene[] genes) {
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

    public static List<IChromosome> getInitialPopulation() {
        return initialChromosomes;
    }

    /**
     * Sets the alleles of a to the alleles of b
     * @param a
     * @param b
     */
    public static void setGenes(Gene[] a, Gene[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] instanceof IntegerGene) {
                a[i].setAllele(b[i].getAllele());
            } else if (a[i] instanceof CompositeGene) {
                CompositeGene ca = (CompositeGene)a[i];
                CompositeGene cb = (CompositeGene)b[i];
                for (int j = 0; j < ca.getGenes().size(); j++) {
                    ca.geneAt(j).setAllele(cb.geneAt(j).getAllele());
                }
            }
        }
    }
}