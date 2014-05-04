package alexdiru.lifesim.helpers;

/**
 * A helper class to provide functionality to process threads
 */
public class ThreadHelper {

    /**
     * Wait for an array of threads to finish their processes
     * @param threads The array of threads to wait for
     */
    public static void waitForThreads(Thread[] threads) {
        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
