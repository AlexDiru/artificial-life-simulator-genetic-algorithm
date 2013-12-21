package alexdiru.lifesim.main;

import alexdiru.lifesim.interfaces.IClone;

import java.io.Serializable;

public class Poison extends Item implements Serializable, IClone {

	private int staminaReductionValue = 0;
	
	private int restartStaminaValue;

    public Poison(Object2D object2D) {
        super(object2D);
        staminaReductionValue = 50;
    }


    public Poison clone() {
        Poison f = new Poison(super.cloneSuper());
        f.staminaReductionValue = staminaReductionValue;
        f.restartStaminaValue = restartStaminaValue;
        f.isPickedUp = isPickedUp;
        f.isExhausted = isExhausted;
        return f;
    }


    public Poison(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		staminaReductionValue = 3000;
		restartStaminaValue = staminaReductionValue;
	}

	@Override
	public void apply(LifeForm lifeForm) {
		super.apply(lifeForm);
		lifeForm.increaseStamina(-staminaReductionValue);
	}

	@Override
	public String getInformation() {
		return "Poison " + staminaReductionValue;
	}
	
	@Override
	public void restart() {
		super.restart();
		staminaReductionValue = restartStaminaValue;
	}

	@Override
	public void contactWithWater() {
		isExhausted = true;
	}
}
