package alexdiru.lifesim.main;

import alexdiru.lifesim.interfaces.IClone;

import java.io.Serializable;

/**
 * Represents food in the simulation
 * @author Alex
 *
 */
public class Food extends Item implements Serializable, IClone {

    public Food clone() {
        Food f = new Food(super.cloneSuper());
        f.staminaValue = staminaValue;
        f.isPickedUp = isPickedUp;
        f.isExhausted = isExhausted;
        return f;
    }

	/**
	 * The stamina the food gives the life form that eats it
	 */
	private int staminaValue;

    public Food(Object2D object2D) {
        super(object2D);
        staminaValue = 50;
    }
	
	public Food(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		staminaValue = 50;
	}

	@Override
	public void apply(LifeForm lifeForm) {
		super.apply(lifeForm);
		lifeForm.increaseStamina(staminaValue);
	}

	@Override
	public String getInformation() {
		return "Food " + staminaValue;
	}

	@Override
	public void contactWithWater() {
		isExhausted = true;
	}
}