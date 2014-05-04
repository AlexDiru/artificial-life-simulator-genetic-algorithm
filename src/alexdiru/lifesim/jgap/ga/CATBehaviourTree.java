package alexdiru.lifesim.jgap.ga;

import alexdiru.lifesim.enums.DecisionNodeType;
import alexdiru.lifesim.main.LifeForm;
import org.jgap.*;
import org.jgap.impl.CompositeGene;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * The behaviour tree of a life form. It is created from the life form's gene and determines which actions the life form will take every cycle.
 * A way of replicated the tree-based structure of Genetic Programming using Genetic Algorithms
 */
public class CATBehaviourTree {
	
	/**
	 * The life form that this behaviour tree belongs to
	 */
	private LifeForm lifeForm;
	
	/**
	 * The root node of the behaviour tree (evaluated first)
	 */
	private DecisionNode root;


    public DecisionNode getRoot() {
        return root;
    }

	/**
	 * Creates a behaviour tree from a list of genes
	 * @param lifeForm The life form to assign the behaviour tree to
	 * @param genes The list of genes to create the behaviour tree from - note that only the genes related to the behaviour tree must be in this list
	 * @return The behaviour tree
	 */
	static CATBehaviourTree createFromSpecificGenes(final LifeForm lifeForm, final List<Gene> genes) {
		CATBehaviourTree tree = createFromNodes(convertGenesToDecisionNodes(genes));
		tree.setLifeForm(lifeForm);
		return tree;
	}
	
	/**
	 * Evaluate the behaviour tree starting from the root node
	 */
	public void evaluate() {
		evaluate(root);
	}
	
	/**
	 * Creates a tree from a set of decision nodes
	 * @param nodes The set of nodes to create the tree from
	 * @return A behaviour tree
	 */
	private static CATBehaviourTree createFromNodes(final List<DecisionNode> nodes) {
		//Must be a square number - 1 count of nodes
		//DebugHelper.assertCondition(MathHelper.isSquareNumber(nodes.size() + 1), "Behaviour tree can only be created with a node count that is a square number minus 1");

        /*
		int index = 1;
		for (int n = 0; n < Math.ceil(Math.sqrt(nodes.size())); n++) {
			nodes.get(n).setLeft(nodes.get(index++));
			nodes.get(n).setRight(nodes.get(index++));
		}*/



        int index = 1;
        nodes.get(0).setDepth(0);
        for (int n = 0; n < Math.pow(2, GeneManager.TREE_NODE_DEPTH - 1) - 1;  n++) {
            nodes.get(n).setLeft(nodes.get(index));
            nodes.get(index).setDepth(nodes.get(n).getDepth() + 1);
            index++;
            nodes.get(n).setRight(nodes.get(index));
            nodes.get(index).setDepth(nodes.get(n).getDepth() + 1);
            index++;
        }

        CATBehaviourTree tree = new CATBehaviourTree(nodes.get(0));
		return tree;
	}
	
	/**
	 * Creates a list of decision nodes from a list of genes
	 * @param genes The list of genes that the nodes will be based on
	 * @return The list of decision nodes
	 */
	private static List<DecisionNode> convertGenesToDecisionNodes(final List<Gene> genes) {
		List<DecisionNode> nodes = new ArrayList<DecisionNode>();
		for (Gene gene : genes)
			nodes.add(DecisionNode.create(gene));
		return nodes;
	}
	
	/**
	 * Create a behaviour tree from a root note (Note - the nodes must already be linked together)
	 * @param root The root node
	 */
	private CATBehaviourTree(DecisionNode root) {
		this.root = root;
	}

	/**
	 * Set the life form of the behaviour tree
	 * @param lifeForm The life form who the behaviour tree belongs to
	 */
	private void setLifeForm(final LifeForm lifeForm) {
		this.lifeForm = lifeForm;
	}
	
	/**
	 * Evaluates a node on the behaviour tree (also evaluates the children of the tree)
	 * @param node The (root) node to evaluate
	 */
	private void evaluate(final DecisionNode node) {
		if (node == null || node.isRedundant())
			return;
		
		if (node.getType() == DecisionNodeType.ACTION) 
			evaluateAsAction(node);
		else if (node.getType() == DecisionNodeType.CONDITION)
			evaluateAsCondition(node);
		else if (node.getType() == DecisionNodeType.TERMINAL)
			evaluateAsTerminal(node);
	}

	/**
	 * Evaluates a node as an action (evaluates it's left child afterwards)
	 * @param node The (root) node to evaluate
	 */
	private void evaluateAsAction(final DecisionNode node) {
		node.runFunction(lifeForm);
		evaluate(node.getLeft());
	}

	/**
	 * Evaluates a node as a condition (evaluates it's left or right child afterwards depending on the return value of the condition)
	 * @param node The (root) node to evaluate
	 */
	private void evaluateAsCondition(final DecisionNode node) {
		if (node.runFunction(lifeForm))
			evaluate(node.getLeft());
		else
			evaluate(node.getRight());
	}
	
	/**
	 * Evaluates a node as a terminal (evaluates the node and doesn't evaluate any children afterwards)
	 * @param node The (root) node to evaluate
	 */
	private void evaluateAsTerminal(final DecisionNode node) { 
		node.runFunction(lifeForm);
	}

    /**
     * Creates a behaviour tree from a chromosome
     * @param lifeForm The life form to assign the tree to
     * @param chromosome The chromosome to create the tree from
     * @return The behaviour tree
     */
	public static CATBehaviourTree createFromChromosome(LifeForm lifeForm, IChromosome chromosome) {
		int index = GeneManager.BEHAVIOUR_TREE_INDEX;
        List<Gene> treeGenes = ((CompositeGene)chromosome.getGenes()[index]).getGenes();
		return createFromSpecificGenes(lifeForm,treeGenes);// genes);
	}

    /**
     * Creates a behaviour tree from a set of genes
     * @param lifeForm The life form to assign the tree to
     * @param genes The set of genes
     * @return The behaviour tree
     */
    public static CATBehaviourTree createFromGenes(LifeForm lifeForm, Gene[] genes) {
        try {
            GAConfiguration.reset();
            IChromosome chromosome = new Chromosome(new GAConfiguration());
            chromosome.setGenes(genes);
            return createFromChromosome(lifeForm, chromosome);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the behaviour tree to a DefaultMutableTreeNode so it can be displayed in the GUI using a JTree
     * Returns the root node of the tree
     * @return The root node
     */
	public DefaultMutableTreeNode getBehaviourTreeRoot() {
		return getTreeNodes(root);
	}

    /**
     * Converts a decision node to a DefaultMutableTreeNode and recursively converts the children
     * @param node The node to convert
     * @return The converted node
     */
	private DefaultMutableTreeNode getTreeNodes(DecisionNode node) {
		DefaultMutableTreeNode rootNode = node.getDefaultMutableTreeNode();
		if (node.getType() != DecisionNodeType.TERMINAL) {
			if (node.getLeft() != null)
				rootNode.add(getTreeNodes(node.getLeft()));
			if (node.getRight() != null && node.getType() == DecisionNodeType.CONDITION)
				rootNode.add(getTreeNodes(node.getRight()));
		}
		return rootNode;
	}

    public static void crossover(ICompositeGene a, ICompositeGene b, RandomGenerator generator, boolean mutation) {
        CATBehaviourTree treeA = CATBehaviourTree.createFromSpecificGenes(null, convertCompositeGeneToList(a));
        CATBehaviourTree treeB = CATBehaviourTree.createFromSpecificGenes(null, convertCompositeGeneToList(b));
        treeA.markRedundantNodes();
        treeB.markRedundantNodes();

        //List<DecisionNode> nodesA = treeA.convertToNodes();
        //List<DecisionNode> nodesB = treeB.convertToNodes();

        DecisionNode crossoverA;
        DecisionNode crossoverB;

        crossoverA = treeA.getRandomNonRedundantNode(generator);//nodesA.get(generator.nextInt(nodesA.size()));
        if (mutation)
            crossoverB = treeB.root;
        else
            crossoverB = treeB.getRandomNonRedundantNode(generator);//nodesB.get(generator.nextInt(nodesB.size()));


        DecisionNode parentA = crossoverA.getParent();
        DecisionNode parentB = crossoverB.getParent();

        if (parentA == null)
            treeA.setRoot(crossoverB);
        else if (parentA.getLeft() == crossoverA)
            parentA.setLeft(crossoverB);
        else
            parentA.setRight(crossoverB);

        if (parentB == null)
            treeB.setRoot(crossoverA);
        else if (parentB.getLeft() == crossoverB)
            parentB.setLeft(crossoverA);
        else
            parentB.setRight(crossoverA);


        treeA.removeRedundantNodes();
        treeA.toCompositeGene(a.getConfiguration(), treeA.convertToNodes(), a, generator);

        //If a mutation operation, then tree b is just a temporary created tree to swap with tree a and it will be
        //discarded after the mutation operation thus it's pointless to do any processing
        if (!mutation) {
            treeB.removeRedundantNodes();
            treeB.toCompositeGene(b.getConfiguration(), treeB.convertToNodes(), b, generator);
        }
    }

    void toCompositeGene(Configuration configuration, List<DecisionNode> nodes, ICompositeGene original, RandomGenerator generator) {
        if (nodes == null)
            nodes = convertToNodes();

        for (int i = 0; i < original.size(); i++) {
            if (i < nodes.size())
                original.setGene(i,nodes.get(i).toIntegerGene());

        }
    }

    private void markRedundantNodes() {
        markRedundantNodes(root);
    }

    private void removeRedundantNodes() {
        removeRedundantNodes(root, new ConditionStack());
    }

    private void removeRedundantNodes(DecisionNode currentRoot, ConditionStack conditionStack) {

        //If this node is a condition, if it exists, it's a duplicate so we replace this with the left or right child
        if (currentRoot.getType() == DecisionNodeType.CONDITION) {
            ConditionEvaluation conditionEvaluation = conditionStack.getCondition(currentRoot.getFunction());


            //Doesn't exist so evaluate left and right children with the according value
            if (conditionEvaluation == null) {
                //Left (true branch)
                conditionStack.addCondition(currentRoot.getFunction(), true);
                if (currentRoot.getLeft() != null)
                    removeRedundantNodes(currentRoot.getLeft(), conditionStack);

                conditionStack.inverseLatestCondition();
                if (currentRoot.getRight() != null)
                    removeRedundantNodes(currentRoot.getRight(), conditionStack);

                conditionStack.pop();
            }
            //The condition exists and a result has already been calculated so one branch of the current condition is pointless (as is this condition - no need to evaluate twice)
            else {
                if (conditionEvaluation.getConditionResult()) {
                    //Right branch is never going to be used
                    //So replace this node with the left child

                    //If no parent, then root node, so just mark as redundant
                    if (currentRoot.getParent() == null)
                        currentRoot.markAsRedundant();
                        //If the left child doesn't exist, then we just need to remove this node
                    else if (currentRoot.getLeft() == null) {
                        //Mark the node as redundant (from the parent node)
                        if (currentRoot.getParent().getLeft() == currentRoot)
                            currentRoot.getParent().getLeft().markAsRedundant();
                        else //if (currentRoot.getParent().getRight() == currentRoot)
                            currentRoot.getParent().getRight().markAsRedundant();
                    }
                    //Otherwise replace this node with the left child
                    else {
                        if (currentRoot.getParent().getLeft() == currentRoot) {
                            DecisionNode newNode = currentRoot.getLeft();
                            currentRoot.getParent().setLeft(newNode);
                            currentRoot = newNode;
                        } else {
                            DecisionNode newNode = currentRoot.getLeft();
                            currentRoot.getParent().setRight(newNode);
                            currentRoot = newNode;
                        }
                    }

                } else {
                    //Left branch is never going to be used
                    //So replace this node with the right child

                        //If no parent, then root node, so just mark as redundant
                    if (currentRoot.getParent() == null)
                        currentRoot.markAsRedundant();
                    //If the left child doesn't exist, then we just need to remove this node
                    else if (currentRoot.getLeft() == null) {
                        //Mark the node as redundant (from the parent node)
                        if (currentRoot.getParent().getLeft() == currentRoot)
                            currentRoot.getParent().getLeft().markAsRedundant();
                        else //if (currentRoot.getParent().getRight() == currentRoot)
                            currentRoot.getParent().getRight().markAsRedundant();
                    }
                    //Otherwise replace this node with the right child
                    else {
                        if (currentRoot.getParent().getLeft() == currentRoot) {
                            DecisionNode newNode = currentRoot.getRight();
                            currentRoot.getParent().setLeft(newNode);
                            currentRoot = newNode;
                        } else {
                            DecisionNode newNode = currentRoot.getRight();
                            currentRoot.getParent().setRight(newNode);
                            currentRoot = newNode;
                        }
                    }

                }


                //Evaluate child nodes
                if (currentRoot.getLeft() != null && !currentRoot.getLeft().isRedundant())
                    removeRedundantNodes(currentRoot.getLeft(), conditionStack);
                if (currentRoot.getRight() != null && !currentRoot.getRight().isRedundant())
                    removeRedundantNodes(currentRoot.getRight(), conditionStack);

            }
        } else {
            //Evaluate child nodes
            if (currentRoot.getLeft() != null && !currentRoot.getLeft().isRedundant())
                removeRedundantNodes(currentRoot.getLeft(), conditionStack);
            if (currentRoot.getRight() != null && !currentRoot.getRight().isRedundant())
                removeRedundantNodes(currentRoot.getRight(), conditionStack);
        }
    }

    /**
     * Marks nodes which will never be reached (i.e. both children of a terminal node, or the right child of an action node
     * @param root
     */
    private void markRedundantNodes(DecisionNode root) {
        if (root.getType() == DecisionNodeType.TERMINAL) {
            if (root.getLeft() != null)
                root.getLeft().markAsRedundant();
            if (root.getRight() != null)
                root.getRight().markAsRedundant();
        } else if (root.getType() == DecisionNodeType.ACTION) {
            if (root.getRight() != null)
                root.getRight().markAsRedundant();
        }

        if (root.getLeft() != null)
            markRedundantNodes(root.getLeft());
        if (root.getRight() != null)
            markRedundantNodes(root.getRight());
    }

    static List<Gene> convertCompositeGeneToList(ICompositeGene a) {
        List<Gene> g = new ArrayList<Gene>();
        for (int i = 0; i < a.size(); i++)
            g.add(a.geneAt(i));
        return g;
    }

    /**
     * Converts the decision tree into a list of nodes
     * @return The list of nodes
     */
    public List<DecisionNode> convertToNodes() {
        updateNodeDepths(root, 0);
        List<DecisionNode> nodes = new ArrayList<DecisionNode>();
        nodes.add(root);
        nodes.addAll(CATBehaviourTree.convertToNodes(root));
        return nodes;
    }

    /**
     * Recalculates the depth values of the nodes in the tree
     */
    private void updateNodeDepths(DecisionNode root, int depth) {
        root.setDepth(depth);
        if (root.getLeft() != null)
            updateNodeDepths(root.getLeft(), depth+1);
        if (root.getRight() != null)
            updateNodeDepths(root.getRight(), depth+1);
    }

    /**
     * Returns a list of all the non-redundant nodes in the tree, the tree is traversed in the level traversal
     * @param root
     * @return
     */
    private static List<DecisionNode> convertToNodes(DecisionNode root) {
        if (root == null || root.isRedundant())
            return Arrays.asList(new DecisionNode[]{});

        List<DecisionNode> nodes = new ArrayList<DecisionNode>();
        if (root.getLeft() != null)
            nodes.add(root.getLeft());
        if (root.getRight() != null)
            nodes.add(root.getRight());
        if (root.getLeft() != null)
            nodes.addAll(convertToNodes(root.getLeft()));
        if (root.getRight() != null)
            nodes.addAll(convertToNodes(root.getRight()));

        //Remove all nodes over a certain depth

        //

        //Sort nodes by depth to get Level traversal
        Collections.sort(nodes, new Comparator<DecisionNode>() {
            @Override
            public int compare(DecisionNode o1, DecisionNode o2) {
                return Integer.compare(o1.getDepth(), o2.getDepth());
            }
        });

        return nodes;
    }


    /**
     * Sets the root node of the tree
     * @param root
     */
    public void setRoot(DecisionNode root) {
        this.root = root;
        root.setParent(null);
    }

    public DecisionNode getRandomNonRedundantNode(RandomGenerator generator) {
        List<DecisionNode> nodes = convertToNodes();
        int length = nodes.size();

        while (length > 1)
        {
            DecisionNode node = nodes.get(generator.nextInt(length));
            if (!node.isRedundant())
                return node;
            length /= 2;
        }

        return nodes.get(0);
    }
}
