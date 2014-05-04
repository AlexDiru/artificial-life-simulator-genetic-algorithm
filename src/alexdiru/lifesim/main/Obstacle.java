package alexdiru.lifesim.main;

import alexdiru.lifesim.interfaces.IClone;

import java.io.Serializable;

public class Obstacle extends Object2D implements Serializable, IClone {

	public Obstacle(int xPosition, int yPosition) {
		super(xPosition, yPosition);
	}

    public Obstacle clone() {
        return (Obstacle)super.clone();
    }
}
