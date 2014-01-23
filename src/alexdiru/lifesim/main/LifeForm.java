package alexdiru.lifesim.main;

import alexdiru.lifesim.helpers.ItemsList;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import org.jgap.IChromosome;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LifeForm extends Object2D{
	
	/**
	 * The world the life form inhabits
	 */
    protected World world;
	
	/**
	 * The dataset to record the cycles the life form has survived and thus is used to calculate its fitness
	 */
	private LifeFormDataset dataset;
	
	/**
	 * The current stamina the life form has, when this hits zero the life form dies
	 */
	private float stamina = 0;
	
	/**
	 * Energy used from moving around
	 */
	private int usedEnergy = 0;
	
	/**
	 * The senses of the life form controlled by the chromosome/GA
	 */
	protected LifeFormSenses senses;
	
	/**
	 * The memory of the life form (stores the location of food the life form has recently seen)
	 * The number of items that the life form can remember is controller by its chromosome/GA
	 */
	protected LifeFormMemory memory;
	
	/**
fff	 * The angle the life form is facing
	 */
    protected double angle = 0;
	
	/**
	 * Used the store the items the life form can see on each update()
	 */
	private ItemsList itemsInSight = new ItemsList(this);
	
	/**
	 * The item the life form is currently moving/turning towards
	 */
	private Item targetItem = null;
	
	/**
	 * Used to store the fitness once it has been calculated - I'm sceptical of JGAP's method for it
	 */
	private double fitness = 0;
    private IChromosome chromosome;

    /**
	 * Creates the life form
	 * @param world The simulation world the life form belongs to
	 */
	public LifeForm(World world) {
		super(0,0);
		this.world = world;
		dataset = new LifeFormDataset();
		senses = new LifeFormSenses();
		stamina = 0;
		usedEnergy = 0;
	}
	
	/**
	 * Restarts the life form to how it was at the start of the round
	 */
	public void restart() {
		stamina = 0;
		usedEnergy = 0;
		senses.restart();
        if (memory != null)
		    memory.clear();
		targetItem = null;
		xPosition = 0f;
		yPosition = 0f;
		angle = 0;
		if (itemsInSight != null)
			itemsInSight.clear();
		targetItem = null;
	}
	
	/**
	 * Increases the stamina of the life form
	 * @param inc The amount to increase by
	 */
	public void increaseStamina(int inc) {
		stamina += inc;
	}
	
	/**
	 * Renders the life form and the item that it is holding (if it is holding an item)
	 * @param renderer The object the program uses to render
	 * @param spriteBatch The sprite batch which draws the images
	 * @param shapeRenderer The shape renderer to draw the shapes
	 * @param sprite The image used to render the life form
	 * @param scrollOffsetX The amount the map has been scrolled across the x axis
	 * @param scrollOffsetY The amount the map has been scrolled across the y axis
	 */
	public void renderFacing(Renderer renderer, SpriteBatch spriteBatch,ShapeRenderer shapeRenderer, Sprite sprite, int scrollOffsetX, int scrollOffsetY) {
		sprite.setPosition((int)(xPosition - (sprite.getWidth()/2)) + scrollOffsetX,(int)( yPosition - (sprite.getHeight()/2)) + scrollOffsetY);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setRotation((float)angle);
		sprite.draw(spriteBatch);
		
		spriteBatch.end();
		//Draw FOVs
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Circle);
		//Sight Radius
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.circle((float)(xPosition + scrollOffsetX), (float)(yPosition + scrollOffsetY), (float)senses.getSightDistance());
		//Reach
		shapeRenderer.setColor(0,1,1,1);
		shapeRenderer.circle((float)(xPosition + scrollOffsetX), (float)(yPosition + scrollOffsetY), (float)senses.getReachDistance());
		
		//Circle items that can be seen
		shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
		if (itemsInSight != null)
			synchronized (itemsInSight) {
				for (Item item : itemsInSight) 
					shapeRenderer.circle((float)item.getXPosition() + scrollOffsetX, (float)item.getYPosition() + scrollOffsetY, 16);
			}
		shapeRenderer.end();
		
		//Sight FOV
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.line((float)xPosition + scrollOffsetX, (float)yPosition + scrollOffsetY, (float)(xPosition  + scrollOffsetX+ senses.getSightDistance() * Math.cos(Math.toRadians(angle + senses.getSightFovDegrees()/2))),(float)( yPosition  + scrollOffsetY+ senses.getSightDistance() * Math.sin(Math.toRadians(angle + senses.getSightFovDegrees()/2))));
		shapeRenderer.line((float)xPosition + scrollOffsetX, (float)yPosition + scrollOffsetY, (float)(xPosition  + scrollOffsetX+ senses.getSightDistance() * Math.cos(Math.toRadians(angle + -1* senses.getSightFovDegrees()/2))),(float)( yPosition  + scrollOffsetY+ senses.getSightDistance() * Math.sin(Math.toRadians(angle + -1 * senses.getSightFovDegrees()/2))));
		shapeRenderer.end();
		
		//Items in memory
		shapeRenderer.begin(ShapeType.Rectangle);
		shapeRenderer.setColor(0.25f,0.25f,1,1);
		
		spriteBatch.begin();
	}
	
	/**
	 * Gets the angle of the item based on the life form's angle
	 * @param item The item to find the angle of
 	 * @return The angle to the items
	 */
	private double getAngle(Item item) {
		return Geometry.lockAngle(Math.toDegrees(Math.atan2((item.getYPosition() - yPosition),(item.getXPosition() - xPosition))));
	}
	
	
	/**
	 * Gets a list of all of the items within the life form's FOV
	 * @return The items the life form can see
	 */
	private ItemsList getItemsWithinSight() {
		ItemsList items = new ItemsList(this);
			
		for (Item item : world.getItems())
			if (item instanceof Food) 
				if (!item.isExhausted())
					//Check distance/within the whole sight circle
					if (Geometry.isPointWithinCircle(item.getXPosition(), item.getYPosition(), xPosition, yPosition, (double)senses.getSightDistance())) {
						//Check angle/within FOV arc
						double itemAngle = 90 - angle + getAngle(item);
						double fovUpper =  senses.getSightFovDegrees()/2 + 90;
						double fovLower =  - senses.getSightFovDegrees()/2 + 90;
						if (Geometry.isAngleBetween(itemAngle,fovUpper,fovLower))
							items.add(item);
					}
		return items;
	}
	
	/**
	 * Turns the life form right by their turn speed
	 */
	public void turnRight() {
		turnRight(senses.getTurnSpeed());
	}
	
	/**
	 * Turns the life form right by an angle
	 * @param angle The angle to turn by
	 */
	private void turnRight(double angle) {
		turnLeft(-angle);
	}

	/**
	 * Turns the life form left by their turn speed
	 */
	public void turnLeft() {
		turnLeft(senses.getTurnSpeed());
	}
	
	/**
	 * Turns the life form left by an angle
	 * @param angle The angle to turn by
	 */
	private void turnLeft(double angle) {
		this.angle += angle;
		//usedEnergy += Math.abs(angle) * SimulationSettings.turnEnergyDecreaseFactor;
	}
	
	/**
	 * Moves the life forward by their move speed
	 */
	public void moveForward() {
		moveForward(senses.getMoveSpeed());
	}
	
	/**
	 * Moves the life form forward by a certain amount
	 * @param moveSpeed The amount to move the life form forward by
	 */
	private void moveForward(int moveSpeed) {
		xPosition += moveSpeed * Math.cos(Math.toRadians(angle));
		yPosition += moveSpeed * Math.sin(Math.toRadians(angle));
		
		int maxX = world.getXSize()*Renderer.TILE_SIZE;
		int maxY = world.getYSize()*Renderer.TILE_SIZE;
		
		if (xPosition >= maxX)
			xPosition = maxX - 1;
		if (yPosition >= maxY)
			yPosition = maxY - 1;
		if (xPosition < 0)
			xPosition = 0;
		if (yPosition < 0)
			yPosition = 0;
		
		//Reduce energy according to speed
        //stamina -= (float)Math.abs(moveSpeed);
        //if (stamina < 0)
        //    stamina = 0;
		//usedEnergy += Math.abs(moveSpeed) * SimulationSettings.moveEnergyDecreaseFactor;
	}

    private void moveRight(int moveSpeed) {
        xPosition += moveSpeed * Math.sin(Math.toRadians(angle));
        yPosition += moveSpeed * Math.cos(Math.toRadians(angle));

        int maxX = world.getXSize()*Renderer.TILE_SIZE;
        int maxY = world.getYSize()*Renderer.TILE_SIZE;

        if (xPosition >= maxX)
            xPosition = maxX - 1;
        if (yPosition >= maxY)
            yPosition = maxY - 1;
        if (xPosition < 0)
            xPosition = 0;
        if (yPosition < 0)
            yPosition = 0;
    }

    public void moveLeft() {
        moveRight(-senses.getMoveSpeed());
    }

    public void moveRight() {
        moveRight(senses.getMoveSpeed());
    }

	/**
	 * Moves the life form backwards by their movement speed
	 */
	public void moveBackward() {
		moveForward(-senses.getMoveSpeed());
	}

	
	public void update() {
		//Update the life form depending on whether GA and/or GP is being used
		watch();
        updateGA();
        //reachForFood();
	}
	
	public void updateGP() {
		throw new UnsupportedOperationException("UpdateGP() not implemented");
	}
	
	/**
	 * Updates one cycle of the life form according to the GA
	 */
	public void updateGA() {
        angle = Geometry.lockAngle(angle);
		senses.evaluateBehaviourTree();
		
		/*
		watch();
		
		targetItem = getClosestItem(memory.getItems());
		
		//Check if the item in sight are reachable
		for (Item item : itemsInSight)
			if (Geometry.isPointWithinCircle(item.getXPosition(), item.getYPosition(), xPosition, yPosition, senses.getReachDistance())) {
				item.apply(this);
				memory.removeFromMemory(item);
				if (item == targetItem)
					targetItem = null;
			}
		
		if (targetItem == null) {
			itemsInSight = getItemsWithinSight();
			
			if (itemsInSight.size() == 0) {
				senses.performSearchAction();
			} 
		} else if (targetItem != null) {
			if (isFacing(targetItem)) {
				senses.performFacingAction();
			} else {
				if (isItemOnLeftSide(targetItem) && senses.canTurnLeft())
					senses.performLeftSideAction();
				else if (senses.canTurnRight())
					senses.performRightSideAction();
				else
					senses.performNoSideAction();
			}
		}
		*/
	}
	
	//Potential GP Functions
	public void watch() {
		//Look for all items
		itemsInSight = getItemsWithinSight();
		
		//Add items to memory, but we want to make sure the closest items end up stored in memory
		//As potentially the memory will not be big enough to store all the items 
		//So we need to sort by distance in descending order
		itemsInSight.sort();
		memory.store(itemsInSight);
	}

	
	/**
	 * Whether the life form is facing the item
	 * @param targetItem
	 * @return
	 */
    protected boolean isFacing(Item targetItem) {
        int threshold = 4;
        int upperBound = (int)angle + threshold;
        int lowerBound = (int)angle - threshold;
        int lockangle = (int)Geometry.lockAngle(getAngle(targetItem));
        return lockangle < upperBound && lockangle > lowerBound;
	}
	
	/**
	 * Whether the item is on the left side of the life (180 degrees area) and the life form is also not facing the item
	 * @param targetItem The item to check
	 * @return
	 */
	private boolean isItemOnLeftSide(Item targetItem) {
		double itemAngle = (int)Geometry.lockAngle(getAngle(targetItem));
		return !Geometry.isAngleBetween(itemAngle, angle, angle - 180) && !isFacing(targetItem);
	}

    /**
     * Whether the item is on the right side of the life (180 degrees area) and the life form is also not facing the item
     * @param item The item to check
     * @return
     */
    private boolean isItemOnRightSide(Item item) {
        return !isItemOnLeftSide(item) && !isFacing(item);
    }
	
	/**
	 * Gets the item in the list which is closest to the point x,y
	 * @param items The list of items
	 * @param x The x position
	 * @param y The y position
	 * @return The closest item
	 */
	public static Item getClosestItemInList(List<Item> items, double x, double y) {
		double minDistance = Integer.MAX_VALUE;
		double distance;
		Item closest = null;
        for (Item item : items)
            if (item != null)
                if ((distance = Geometry.getDistanceSquared(item.xPosition, item.yPosition,x, y)) < minDistance) {
                    minDistance = distance;
                    closest = item;
                }

		return closest;
	}

	/**
	 * Gets the item closest to the life form
	 * @param items The list of items the life form has in memory
	 * @return The closest item
	 */
	private Item getClosestItem(List<Item> items) {
		//Get the closest item in the life form's FOV
		Item closest = getClosestItemInList(items, xPosition, yPosition);
		if (closest == null)
			return null;
		
		//As long as this closest item is closer than the current target item
		//Otherwise there's a chance the life form will just end up spinning around
		if (targetItem != null)
			if (closest != targetItem)
				if (Geometry.getDistanceSquared(closest.xPosition, closest.yPosition,xPosition, yPosition) > Geometry.getDistanceSquared(targetItem.getXPosition(), targetItem.getYPosition(), xPosition, yPosition))
					return targetItem;
		
		//The closest item of the items the life form has in memory and the one it is targeting
		return closest;
	}

	public double evaluateFitness() {
		fitness = dataset.evaluateFitness();
		return fitness;
	}

	public void setPosition(Point point) {
		xPosition = Renderer.TILE_SIZE * point.x;
		yPosition = Renderer.TILE_SIZE * point.y;
	}

	public LifeFormSenses getSenses() {
		return senses;
	}
	
	public float getStamina() {
		return stamina;
	}

	public void moveTurnLeft() {
		turnLeft(senses.getTurnSpeed()/2);
		moveForward(senses.getMoveSpeed()/2);
	}
	
	public void moveTurnRight() {
		turnRight(senses.getTurnSpeed()/2);
		moveForward(senses.getMoveSpeed()/2);
	}
	
	public void backwardTurnLeft() {
		turnLeft(senses.getTurnSpeed()/2);
		moveForward(-senses.getMoveSpeed()/2);
	}
	
	public void backwardTurnRight() {
		turnRight(senses.getTurnSpeed()/2);
		moveForward(-senses.getMoveSpeed()/2);
	}

	public void onSimulateNextPosition(Point newPosition) {
		setPosition(newPosition);
	}

	public int getUsedEnergy() {
		return usedEnergy;
	}

	public void addData(float stamina, int usedEnergy) {
		dataset.addData(stamina,usedEnergy);
	}
	
	public double getFitness() {
		return fitness;
	}
	
	//Conditions for CATBehaviourTree

	public boolean canSeeFood() {
		if (itemsInSight == null)
			return false;
		return itemsInSight.size() > 0;
	}
	
	public boolean canRememberFood() {
		return memory.getItems().size() > 0;
	}
	
	//Actions for CATBehaviourTree
	public void setTargetItemToClosestFood() {
		targetItem = getClosestItem(memory.getItems());
	}
	
	//Terminals
	public void reachForFood() {
		if (itemsInSight == null)
			return;
		ArrayList<Item> itemsToRemove = new ArrayList<Item>();
		for (Item item : itemsInSight)
			if (canReach(item)) {
				item.apply(this);
				memory.removeFromMemory(item);
				itemsToRemove.add(item);
				if (item == targetItem)
					targetItem = null;
                break;
			}
		for (Item item : itemsToRemove)
			itemsInSight.remove(item);
	}

	public boolean isFoodOnLeft() {
		for (Item item : itemsInSight)
            if (item instanceof Food)
                if (isItemOnLeftSide(item))
                    return true;
        return false;
	}

    public boolean isPoisonOnLeft() {
        for (Item item : itemsInSight)
            if (item instanceof Poison)
                if (isItemOnLeftSide(item))
                    return true;
        return false;
    }

    public boolean isPoisonOnRight() {
        for (Item item : itemsInSight)
            if (item instanceof Poison)
                if (isItemOnRightSide(item))
                    return true;
        return false;
    }
    public boolean isFacingPoison() {
        for (Item item : itemsInSight)
            if (item instanceof Poison)
                if (isFacing(item))
                    return true;
        return false;
    }
	
	public boolean isFacingFood() {
        for (Item item : itemsInSight)
            if (item instanceof Food)
                if (isFacing(item))
                    return true;
		return false;
	}
	
	public boolean isFoodOnRight() {
        for (Item item : itemsInSight)
            if (item instanceof Food)
                if (isItemOnRightSide(item))
                    return true;
        return false;
	}

    public LifeFormDataset getDataset() {
        return dataset;
    }

    public void setChromosome(IChromosome chromosome) {
        this.chromosome = chromosome;
        senses.setProperties(chromosome, this);
        memory = new LifeFormMemory(senses.getMemoryLength());
    }

    public boolean canReachFood() {
        for (Item item : itemsInSight)
            if (item instanceof Food)
                if (canReach(item))
                    return true;
        return false;
    }

    private boolean canReach(Item item) {
        return (Geometry.isPointWithinCircle(item.getXPosition(), item.getYPosition(), xPosition, yPosition, senses.getReachDistance()));
    }
}