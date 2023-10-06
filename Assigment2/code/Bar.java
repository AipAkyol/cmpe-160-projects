/**
 * Javadoc description part:
 * Bar class for calculating games remaining time
 */
public class Bar {
    public static long startTime; // for calculating game time
    Bar(){     // No-arg constructor (default constructor)
    }
    public static void takeStartTime(){
        startTime = System.currentTimeMillis(); // for calculating games remaining time in milliseconds
    }
    /**
     * Javadoc description part:
     * Calculates game's remaining time
     */
    public static Double remainingTime(){
        long currentTime = System.currentTimeMillis();
        long totalGameDuration = (long) Environment.TOTAL_GAME_DURATION;
        double remaining = (double) (totalGameDuration-(currentTime - startTime)); // subtracts (current time - start time) from the game duration to find remaining time
        return remaining;
    }
}
