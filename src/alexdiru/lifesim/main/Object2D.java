package alexdiru.lifesim.main;
import java.awt.Point;
import java.io.Serializable;

import alexdiru.lifesim.interfaces.IClone;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import alexdiru.lifesim.interfaces.IRestartable;

public class Object2D implements IRestartable, Serializable, IClone {

	protected double xPosition;
	protected double yPosition;
	
	//The original positions used to reset the object to its original position
	protected double restartXPosition;
	protected double restartYPosition;
	
	public Object2D(int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		restartXPosition = xPosition;
		restartYPosition = yPosition;
	}

    public Object2D clone() {
        Object2D o = new Object2D(0,0);
        o.xPosition = xPosition;
        o.yPosition = yPosition;
        o.restartXPosition = restartXPosition;
        o.restartYPosition = restartYPosition;
        return o;
    }
	
	@Override
	public void restart() {
		xPosition = restartXPosition;
		yPosition = restartYPosition;
	}
	
	public double getXPosition(){
		return xPosition;
	}
	
	public double getYPosition(){
		return yPosition;
	}
	
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	public void setYPosition(int yPosition){
		this.yPosition = yPosition;
	}
	
	public void setPosition(Point position) {
		xPosition = position.x;
		yPosition = position.y;
	}
	
	public void render(SpriteBatch spriteBatch, Texture texture, int scrollOffsetX, int scrollOffsetY) {
		spriteBatch.draw(texture,(int)( xPosition - (texture.getWidth()/2) + scrollOffsetX),  (int)(yPosition - (texture.getHeight()/2) + scrollOffsetY), Renderer.TILE_SIZE,Renderer.TILE_SIZE);
	}
	
	public boolean isAtPosition(Point point) {
		return point.x == xPosition && point.y == yPosition;
	}
	
	public Point getPoint() {
		return new Point((int)xPosition, (int)yPosition);
	}
}
