package alexdiru.lifesim.helpers;

import org.apache.commons.lang.NotImplementedException;

/**
 * Provides helper functions to aid in debugging the application
 */
public class DebugHelper {

	/**
	 * Replaces Java's assert keyword which requires certain JVM flags to be set
	 * @param condition The condition which must be true
	 */
	public static void assertCondition(boolean condition, String message) {
		if (!condition)
			throw new AssertConditionIsFalse(message);
	}

    /**
     * A custom exception used when a failed assertCondition happens
     */
	private static class AssertConditionIsFalse extends NotImplementedException {

		private static final long serialVersionUID = 1L;

        /**
         *
         * @param message The message to display when the exception is thrown
         */
		public AssertConditionIsFalse(String message) {
			super(message);
		}
		
	}
}
