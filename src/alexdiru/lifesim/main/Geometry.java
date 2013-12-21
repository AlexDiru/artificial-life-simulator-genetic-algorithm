package alexdiru.lifesim.main;
import java.awt.Point;
import java.util.Random;

import alexdiru.lifesim.main.Geometry.CardinalDirection;


public class Geometry {
	
	private static Random random = new Random();
	
	public static boolean isPointInsideRectangle(int pointX, int pointY, int rectX, int rectY, int rectWidth, int rectHeight) {
		return (pointX > rectX && pointX < rectX + rectWidth && pointY > rectY && pointY < rectY + rectHeight);
	}
	
	public enum CardinalDirection {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
	
	public static CardinalDirection getRandomCardinalDirection() {
		switch (random.nextInt(4)) {
		case 0:
			return CardinalDirection.NORTH;
		case 1:
			return CardinalDirection.EAST;
		case 2:
			return CardinalDirection.SOUTH;
			default:
		case 3:
			return CardinalDirection.WEST;
		}
	}

	public static float getAngleOfCardinalDirection(CardinalDirection facingDirection) {
		switch (facingDirection) {
		case NORTH:
			return 0f;
		case EAST:
			return 90f;
		case SOUTH:
			return 180f;
			default:
		case WEST:
			return 270f;
		}
	}

	public static CardinalDirection cardinalCounterClockwise(CardinalDirection facingDirection) {
		switch (facingDirection) {
		case NORTH:
			return CardinalDirection.WEST;
		case EAST:
			return CardinalDirection.NORTH;
		case SOUTH:
			return CardinalDirection.EAST;
			default:
		case WEST:
			return CardinalDirection.SOUTH;
		}
	}
		
	public static boolean isPointWithinCircle(double pointX, double pointY, double circleX, double circleY, double circleRadius) {
		return ((circleX - pointX) * (circleX - pointX) + (circleY - pointY) * (circleY - pointY)) <= circleRadius * circleRadius;
	}
	
		public static CardinalDirection cardinalClockwise(CardinalDirection facingDirection) {
			switch (facingDirection) {
			case SOUTH:
				return CardinalDirection.WEST;
			case WEST:
				return CardinalDirection.NORTH;
			case NORTH:
				return CardinalDirection.EAST;
				default:
			case EAST:
				return CardinalDirection.SOUTH;
			}
		}

		public static Point getAdjacentPoint(Point point, CardinalDirection facingDirection) {

			switch (facingDirection) {
			case NORTH:
				return new Point(point.x, point.y + 1);
			case SOUTH:
				return new Point(point.x, point.y - 1);
			case WEST:
				return new Point(point.x + 1, point.y );
			case EAST:
				return new Point(point.x - 1, point.y );
			}
			return null;
		}

		public static CardinalDirection opposite(CardinalDirection facingDirection) {
			return cardinalClockwise(cardinalClockwise(facingDirection));
		}

		public static double getDistanceSquared(double x,
				double y, double x2, double y2) {
			return Math.pow(x2 - x, 2) + Math.pow(y2 - y,2);
		}
	
		public static boolean isAngleBetween(double angle, double upper, double lower) {
			angle = lockAngle(angle);
			upper = lockAngle(upper);
			lower = lockAngle(lower);
			if (upper > lower)
				return angle >= lower && angle <= upper;
			else
				return angle >= lower || angle <= upper;
		}
		
		public static double lockAngle(double angle){
			return (angle % 360) + (angle < 0 ? 360 : 0);
		}
}
