package alexdiru.lifesim.main;

import alexdiru.lifesim.interfaces.IClone;

import java.io.Serializable;

public class Water extends Object2D{
	//Creates water on the map at X Position, Y Position
	public Water(int xPosition, int yPosition) {
		super(xPosition, yPosition);
	}

    public Water clone() {
        return new Water((int)xPosition, (int)yPosition);
    }
}
