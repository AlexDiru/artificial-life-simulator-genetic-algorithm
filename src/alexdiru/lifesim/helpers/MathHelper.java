package alexdiru.lifesim.helpers;

import alexdiru.lifesim.main.Object2D;

/**
 * A helper class to provide maths functionality which isn't provided in the standard Java math library
 */
public class MathHelper {

	/**
	 * Checks if a number is a square number i.e. 1, 4, 9, 16... etc
	 * @param n The number to check
	 * @return Whether the number of square
	 */
	public static boolean isSquareNumber(final int n) {
		if (n < 0)
			return false;
		
		int sqrt = (int)(Math.sqrt(n) + 0.5);
		return sqrt * sqrt == n;
	}
	
	/**
	 * Gets the distance squared between two objects
	 * @param a Object A
	 * @param b Object B
	 * @return The distance squared
	 */
	public static double getDistanceSquared(Object2D a, Object2D b) {
		return Math.sqrt(Math.pow(a.getXPosition() - b.getXPosition(), 2) + Math.pow(a.getYPosition() - b.getYPosition(), 2));
	}
}
