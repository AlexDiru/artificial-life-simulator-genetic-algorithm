package alexdiru.lifesim.jgap.ga;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jgap.Configuration;
import org.jgap.Gene;

import alexdiru.lifesim.enums.DecisionNodeType;
import alexdiru.lifesim.helpers.DebugHelper;
import alexdiru.lifesim.main.LifeForm;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.IntegerGene;

public class DecisionNode {

    private static Configuration configuration;

	/**
	 * Number of unique conditions the life form can execute
	 */
	public static int CONDITION_NUMBER = 7;

	/**
	 * Number of unique actions the life form can execute
	 */
	public static int ACTION_NUMBER = 1;

	/**
	 * Number of unique terminal functions the life form can execute
	 */
	public static int TERMINAL_NUMBER = 4;

	/**
	 * The left child node of this node
	 */
	private DecisionNode left;

	/**
	 * The right child node of this node
	 */
	private DecisionNode right;

    /**
     * The parent of this node
     */
    private DecisionNode parent;

	/**
	 * The type of this node (condition, action or terminal)
	 */
	private DecisionNodeType type;

    private int depth;

	/**
	 * The function this node performs (i.e. 1 = move forward, 2 = move backward etc)
	 */
	private int function;

    private boolean isRedunant = false;

    /**
	 * Creates a decision node based on the value of the gene given
	 * @param g The gene
	 * @return The decision node
	 */
	static DecisionNode create(final Gene g) {
        configuration = g.getConfiguration();

		int val = (Integer)g.getAllele();

		if (val < CONDITION_NUMBER)
			return new DecisionNode(DecisionNodeType.CONDITION, val);
		if (val < ACTION_NUMBER + CONDITION_NUMBER)
			return new DecisionNode(DecisionNodeType.ACTION, val - CONDITION_NUMBER);
		if (val < TERMINAL_NUMBER + ACTION_NUMBER + CONDITION_NUMBER)
			return new DecisionNode(DecisionNodeType.TERMINAL, val - CONDITION_NUMBER - ACTION_NUMBER);

		DebugHelper.assertCondition(false, "Gene value is too high");
		return null;
	}

    public IntegerGene toIntegerGene() {
        try {
            IntegerGene integerGene = new IntegerGene(configuration);
            if (type == DecisionNodeType.CONDITION)
                integerGene.setAllele(function);
            else if (type == DecisionNodeType.ACTION)
                integerGene.setAllele(function + CONDITION_NUMBER);
            else
                integerGene.setAllele(function + CONDITION_NUMBER + ACTION_NUMBER);

            return integerGene;
        } catch (InvalidConfigurationException ex) {
            System.out.println("oops");
            ex.printStackTrace();
            return null;
        }
    }

	/**
	 * Gets the number of unique functions that the life form can execute
	 * @return Unique function count
	 */
	public static int getUniqueNodeCount() {
		return CONDITION_NUMBER + ACTION_NUMBER + TERMINAL_NUMBER;
	}

	private DecisionNode(DecisionNodeType type, int function) {
		this.type = type;
		this.function = function;
	}

	public DecisionNodeType getType() {
		return type;
	}

	public DecisionNode getLeft() {
		return left;
	}

	public DecisionNode getRight() {
		return right;
	}

	public boolean runFunction(LifeForm lifeForm) {

            if (type == DecisionNodeType.CONDITION) {
                if (function == 0)
                    return lifeForm.isFacingFood();
                else if (function == 1)
                    return lifeForm.isFoodOnLeft();
                else if (function == 2)
                    return lifeForm.isFoodOnRight();
                else if (function == 3)
                    return lifeForm.isFacingPoison();
                else if (function == 4)
                    return lifeForm.isPoisonOnLeft();
                else if (function == 5)
                    return lifeForm.isPoisonOnRight();
                else if (function == 6)
                    return lifeForm.canReachFood();
            } else if (type == DecisionNodeType.ACTION) {
                if (function == 0)
                    lifeForm.watch();/*
                else if (function == 1)
                    lifeForm.setTargetItemToClosestFood();*/
            } else if (type == DecisionNodeType.TERMINAL) {
                if (function == 0)
                    lifeForm.moveForward();
                else if (function == 1)
                    lifeForm.turnLeft();
                else if (function == 2)
                    lifeForm.turnRight();
                else if (function == 3)
                    lifeForm.reachForFood();
                    /*
                else if (function == 1)
                    lifeForm.moveBackward();
                else if (function == 2)
                    lifeForm.turnLeft();
                else if (function == 3)
                    lifeForm.turnRight();
                else if (function == 4)
                    lifeForm.moveLeft();
                else if (function == 5)
                    lifeForm.moveRight();*/
            }

		return true;
	}

	public DefaultMutableTreeNode getDefaultMutableTreeNode() {
		return new DefaultMutableTreeNode(getFunctionName());
	}

	private String getFunctionName() {
        if (isRedundant())
            return "REDUNDANT";

		if (type == DecisionNodeType.CONDITION) {
            if (function == 0)
                return "isFacingFood?";
            else if (function == 1)
                return "isFoodOnLeft?";
            else if (function == 2)
                return "isFoodOnRight?";
            else if (function == 3)
                return "isFacingPoison?";
            else if (function == 4)
                return "isPoisonOnLeft?";
            else if (function == 5)
                return "isPoisonOnRight?";
            else if (function == 6)
                return "canReachFood?";
		} else if (type == DecisionNodeType.ACTION) {
            if (function == 0)
                return "watch()";
		} else if (type == DecisionNodeType.TERMINAL) {
			if (function == 0)
				return "moveForward()";
            else if (function == 1)
                return "turnLeft()";
            else if (function == 2)
                return "turnRight()";
            else if (function == 3)
                return "reachForFood()";
            /*
			else if (function == 4)
				return "backwardTurnLeft()";
			else if (function == 5)
				return "backwardTurnRight()";
			else if (function == 6)
				return "reachForFood()";*/
		}

		return "REDUNDANT";
	}

    int getFunction() {
        return function;
    }

	public void setLeft(DecisionNode left) {
		this.left = left;
        left.parent = this;
	}

	public void setRight(DecisionNode right) {
		this.right = right;
        right.parent = this;
	}

    public DecisionNode getParent() {
        return parent;
    }

	@Override
	public String toString() {
		return type.toString();
	}

    public void markAsRedundant() {
        isRedunant = true;
        if (left != null)
            left.markAsRedundant();
        if (right != null)
            right.markAsRedundant();
    }

    public void mutate(RandomGenerator generator) {
        System.out.println("hi");
        int r = generator.nextInt(3);
        if (r == 0) {
            type = DecisionNodeType.CONDITION;
            function = generator.nextInt(CONDITION_NUMBER);
        } else if (r == 1) {
            type = DecisionNodeType.ACTION;
            function = generator.nextInt(ACTION_NUMBER);
        } else if (r == 2) {
            type = DecisionNodeType.TERMINAL;
            function = generator.nextInt(TERMINAL_NUMBER);
        }

        //Redundant nodes have a 50% chance of mutating
        if (left != null)
            if (!left.isRedunant || generator.nextInt(2) == 1)
                left.mutate(generator);

        if (right != null)
            if (!right.isRedunant || generator.nextInt(2) == 1)
                right.mutate(generator);
    }

    public void setParent(DecisionNode parent) {
        this.parent = parent;
    }

    public boolean isRedundant() {
        return isRedunant;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
