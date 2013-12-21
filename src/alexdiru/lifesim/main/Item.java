package alexdiru.lifesim.main;

import alexdiru.lifesim.interfaces.IClone;

import java.awt.Point;
import java.io.Serializable;

public abstract class Item extends Object2D implements Serializable, IClone {
	
	/**
	 * Whether the item is being carried by a life form
	 */
	protected boolean isPickedUp;
	
	/**
	 * Whether the item has been consumed
	 */
	protected boolean isExhausted;

    public Object2D cloneSuper() {
        return super.clone();
    }

    public Item clone() {
        if (this instanceof Food)
            return ((Food)this).clone();
        else {
            System.out.println("Clone method not implemented");
            return null;
        }
    }
	
	/**
	 * Creates the item
	 * @param xPosition The x tile position of the item
	 * @param yPosition The y tile position of the item
	 */
	public Item(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		isPickedUp = false;
	}

    public Item(Object2D object2D) {
        super(0,0);
        xPosition = object2D.xPosition;
        yPosition = object2D.yPosition;
        restartXPosition = object2D.restartXPosition;
        restartYPosition = object2D.restartYPosition;
    }
	
	@Override
	/**
	 * Restarts the item to its original properties when the world was created
	 */
	public void restart() {
		super.restart();
		isPickedUp = false;
		isExhausted = false;
	}

	/**
	 * @return Whether the item should be rendered
	 */
	public boolean shouldRender() {
		return !isExhausted() && !isPickedUp;
	}

	/**
	 * Applies the item to the life form (i.e. what happens when the life form consumes the item)
	 * @param lifeForm The life form which is using the item
	 */
	public void apply(LifeForm lifeForm) {
		isExhausted = true;
	}
	
	/**
	 * @return Whether the item has been used/eaten
	 */
	public boolean isExhausted() {
		return isExhausted;
	}

	/**
	 * @return Whether the item can be picked up from the world (Lilypads in water cannot be picked up)
	 */
	public boolean canPickUp() {
		return !isExhausted() && !isPickedUp;
	}
	
	/**
	 * @return Whether the item is being held by a life form
	 */
	public boolean isPickedUp() { 
		return isPickedUp;
	}
	
	/**
	 * @return A text representation about this item
	 */
	public String getInformation() {
		String[] split = getClass().getName().split(".");
		try{
			return split[split.length-1];
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * Set the item as being picked up
	 */
	public void setPickedUp() {
		isPickedUp = true;
	}

	/**
	 * Set the item as being on the floor of the world from beng previously carried
	 * @param world The world the life form belongs to
	 * @param xPosition The new x position the item has been placed down
	 * @param yPosition The new y position the item has been placed down
	 */
	public void setPlacedDown(World world,int xPosition, int yPosition) {
		//if (world.isWaterAt(new Point(xPosition, yPosition)))
		//	contactWithWater();
		
		isPickedUp = false;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	/**
	 * What happens when the item is placed on water
	 */
	public abstract void contactWithWater();
}