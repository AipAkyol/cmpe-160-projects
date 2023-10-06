import java.awt.*;

/**
 * Javadoc description part:
 * Drawings method depot class
 */
public class Environment {
    public static int canvasWidth = 800;
    public static int canvasHeight = 500;
    public static final int TOTAL_GAME_DURATION = 40000; // in milliseconds
    public static final int PAUSE_DURATION = 15; // in milliseconds

    Environment(){     // No-arg constructor (default constructor)
    }

    /**
     * Javadoc description part:
     * Sets the canvas sizes, scales, and enables double buffering
     */
    public static void setStdDrawSettings(){
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0.0,16.0); // x scale
        StdDraw.setYscale(-1.0,9.0); // y scale (-1 to 0 is for bar, 0 to 9 is for game screen)
        StdDraw.enableDoubleBuffering(); // for faster animations
    }

    /**
     * Javadoc description part:
     * Draws the background image
     */
    public static void drawBackground(){
        StdDraw.picture(8.0,4.5,"background.png",16.0,9.0); //drawing game background
    }

    /**
     * Javadoc description part:
     * Draws the bar background
     */
    public static void drawBarBackground(){
        StdDraw.picture(8.0,-0.5,"bar.png",16.0,1.0); //drawing bar background
    }

    /**
     * Javadoc description part:
     * Draws the time bar
     */
    public static void drawBar(){
        int barColorGreenValue = (int)((Bar.remainingTime() / TOTAL_GAME_DURATION) * 255); //green value of bar
        StdDraw.setPenColor(255,barColorGreenValue,0); // bar color according to the remaining time left
        StdDraw.filledRectangle(8.0*(Bar.remainingTime() / TOTAL_GAME_DURATION),-0.5,8.0*(Bar.remainingTime() / TOTAL_GAME_DURATION),0.25); // draws time bar
    }

    /**
     * Javadoc description part:
     * Draws the game screen
     */
    public static void drawGameScreen(){
        StdDraw.picture(8.0,4.12,"game_screen.png",4.21,2.25); // drawing game screen
        StdDraw.setPenColor(0,0,0); // black
        StdDraw.setFont( new Font("Helvetica", Font.ITALIC, 15)); // sets font for replay and quit sections
        StdDraw.text(8.0,3.91,"To Replay Click \"Y\""); // writes the text to the screen
        StdDraw.text(8.0,3.46,"To Quit Click \"N\""); // writes the text to the screen
    }

    /**
     * Javadoc description part:
     * Writes "GAME OVER!" to the screen
     */
    public static void writeGameOver(){
        StdDraw.setFont( new Font("Helvetica", Font.BOLD, 30)); // sets font for game over
        StdDraw.text(8.0,4.5,"GAME OVER!"); // writes the text to the screen
    }

    /**
     * Javadoc description part:
     * Writes "YOU WON!" to the screen
     */
    public static void writeYouWon() {
        StdDraw.setFont(new Font("Helvetica", Font.BOLD, 30)); // sets font for you won
        StdDraw.text(8.0, 4.5, "YOU WON!"); // writes the text to the screen
    }

    /**
     * Javadoc description part:
     * Draws the player to the screen
     */
    public static void drawPlayer(double xCoordinate,double yCoordinate,double xScale,double yScale){
        StdDraw.picture(xCoordinate,yCoordinate,"player_back.png",xScale,yScale); // draws player
    }

    /**
     * Javadoc description part:
     * Draws the arrow to the screen
     */
    public static void drawArrow(double xCoordinate, double yCoordinate){
        StdDraw.picture(xCoordinate,yCoordinate,"arrow.png",0.16,9.0); // draws arrow
    }

    /**
     * Javadoc description part:
     * Draws a ball to the screen
     */
    public static void drawBall(double xCoordinate, double yCoordinate, double radius){
        StdDraw.picture(xCoordinate,yCoordinate,"ball.png",radius*2,radius*2); // draws a ball
    }
}
