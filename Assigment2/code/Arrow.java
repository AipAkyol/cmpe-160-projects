/**
 * Javadoc description part:
 * Arrow class for creating and moving arrows
 */
public class Arrow {
    public static boolean doesArrowExist = false; // for checking if an arrow already exists
    public double xPos; // arrow x position
    public double yPos;// arrow y position
    public double speed; // arrow speed in canvas

    Arrow(){     // No-arg constructor (default constructor)
    }
    Arrow(double startingXPos){  //constructor if one param (starting x position) is given
        xPos = startingXPos; // setting x position
        yPos = -4.5; // setting y position to -4.5 because the arrow is exactly screen height long, and it will start from the bottom and will travel upwards
        speed = 0.16; // setting arrow speed
    }
    /**
     * Javadoc description part:
     * moves the arrow upwards and deletes it if it did reach the upper edge of the screen
     */
    public void moveArrow(){
        double newYPos = yPos + speed; // potential new y position
        if (newYPos < 4.5) // checking if the middle of the arrow has reached middle of the screen (because the arrows height is equal to screen height)
            yPos = newYPos;
        else
            doesArrowExist = false; // deleting the arrow
    }
    /**
     * Javadoc description part:
     * creates a real arrow to draw on the screen
     *
     * Javadoc tags part:
     * @param startingXPos Player's starting x position to create arrow from bottom of the player
     */
    public void createArrow(double startingXPos){
        xPos = startingXPos;
        yPos = -4.5;
    }
}
