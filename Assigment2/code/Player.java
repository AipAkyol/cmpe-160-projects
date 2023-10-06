import java.awt.event.KeyEvent;

/**
 * Javadoc description part:
 * Player class for creating and moving players
 */
public class Player {
    public double playerHeight; // player height in canvas
    public double playerWidth; // player width in canvas
    public double playerSpeed; // player speed to move in the canvas
    public double xPos; // x position of player
    public double yPos; // y position of player

    Player(){     // No-arg constructor (default constructor)
        playerHeight = 1.125;
        playerWidth = 0.821;
        playerSpeed = 0.082;
        xPos = 8.0;
        yPos = 0.5625;
    }

    /**
     * Javadoc description part:
     * moves the player according to player input
     */
    public void movePlayer(){
        double newXPos; // creating a potential x position
        // checking if the left arrow key is pressed
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            newXPos = xPos - playerSpeed; // setting new potential x position
            if (newXPos > 0.41) // checking if the player is at the left edge of the screen
                xPos = newXPos; // finally setting the new x position if all the conditions are met
        }
        // checking if the right arrow key is pressed
        else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
            newXPos = xPos + playerSpeed; // setting new potential x position
            if (newXPos < 15.59) // checking if the player is at the left edge of the screen
                xPos = newXPos; // finally setting the new x position if all the conditions are met
        }
    }
}
