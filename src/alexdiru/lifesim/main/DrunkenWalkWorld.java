package alexdiru.lifesim.main;
import java.awt.Point;
import java.util.ArrayList;


/**
 * Uses drunken walk algorithm to generate a world
 * @author Alex
 *
 */
public class DrunkenWalkWorld {
	
	/**
	 * Tile width of the generated map
	 */
	private int xSize;
	
	/**
	 * Tile height of the generated map
	 */
	private int ySize;
	
	/**
	 * The obstacles of the generated map
	 */
	private ArrayList<Obstacle> obstacles;
	
	/**
	 * Generates a map
	 * @param openSteps The number of walkable tiles on the map (the real number may be slightly lower than this)
	 */
	public void generate(int openSteps) {
		ArrayList<Point> temporaryMap = new ArrayList<Point>();
		obstacles = new ArrayList<Obstacle>();
		
		temporaryMap.add(new Point(0,0));
		Point currentPoint = temporaryMap.get(0);
		
		//This variable removes the very, very unlikely chance that this algorithm will end up in an infinite loop due to random chance
		int timeout = 0;
		int timeoutMax =  openSteps * openSteps;
		
		int filledSteps = 1;
		
		while (filledSteps != openSteps && timeout < timeoutMax) {
			Geometry.CardinalDirection direction = Geometry.getRandomCardinalDirection();
			Point destinationPoint = Geometry.getAdjacentPoint(currentPoint, direction);
			if (!temporaryMap.contains(destinationPoint)) {
				temporaryMap.add(destinationPoint);
				filledSteps++;
			}
			currentPoint = destinationPoint;
			timeout++;
		}
		
		//Get the min X and min Y points
		//Get the max X and max Y points
		Point minX = null;
		Point minY = null;
		Point maxX = null;
		Point maxY = null;
		
		for (Point point : temporaryMap) {
			if (minX == null || point.x < minX.x)
				minX = point;
			if (minY == null || point.y < minY.y)
				minY = point;
			if (maxX == null || point.x > maxX.x)
				maxX = point;
			if (maxY == null || point.y > maxY.y)
				maxY = point;
		}
		
		xSize = Math.abs(maxX.x - minX.x) + 1;
		ySize = Math.abs(maxY.y - minY.y) + 1;
		
		int xPositionOffset = -minX.x;
		int yPositionOffset = -minY.y;
		
		//Every point not in temporary map add an obstacle
		//Otherwise randomly add water
		//A way of getting round this is to add obstacles everywhere, then remove then
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				if (!temporaryMap.contains(new Point(x - xPositionOffset, y - yPositionOffset)))
					obstacles.add(new Obstacle(x,y));	
	}

	/**
	 * @return Get the width in tiles of the generated map
	 */
	public int getXSize() {
		return xSize;
	}
	
	/**
	 * @return Get the height in tiles of the generated map
	 */
	public int getYSize() {
		return ySize;
	}
	
	/**
	 * @return Get the obstacles of the geneerated map
	 */
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
}