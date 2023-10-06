/**
 * Javadoc description part:
 * Ball class for creating and moving balls and checking collisions
 */
public class Ball {
    public double xPos; // x position of ball
    public double yPos; // y position of ball
    public double xSpeed; // x speed of a ball at canvas
    public double ySpeed; // y speed of a ball at canvas
    public final double GRAVITY = -0.0022; // gravity constant for free falls
    public int ballLevel; // ball level indicator (from 0 to 2)
    public double radius; // ball radius

    Ball() {     // No-arg constructor (default constructor)
    }

    Ball(int level, double startingXPos, double startingYPos){ // 3-arg constructor that will only be used at the start of the game
        xPos = startingXPos;
        yPos = startingYPos;
        ballLevel = level;
        if (level == 1) // initial x speed of level 1
            xSpeed = -0.032;
        else // initial x speed of levels 0 and 2
            xSpeed = 0.032;
        ySpeed = 0.066 * (Math.pow(1.32,level)); // initial y speed according to balls level
        radius = 0.1575 * (Math.pow(2,level)); // setting radius according to balls level
    }

    Ball(int level, double startingXPos, double startingYPos,double startingXSpeed){ // 4-arg constructor that will be used when a ball is split into two
        xPos = startingXPos;
        yPos = startingYPos;
        ballLevel = level;
        xSpeed = startingXSpeed; // setting s speed manually this time because the ball will split into two and two balls will move at the opposite direction
        ySpeed = 0.066 * (Math.pow(1.32,level)); // initial y speed of split balls
        radius = 0.1575 * (Math.pow(2,level)); // setting radius
    }

    /**
     * Javadoc description part:
     * moves a ball or collides it elastically
     *
     * Javadoc tags part:
     * @param coolBall Ball to move
     */
    public static void moveBall(Ball coolBall){
        coolBall.ySpeed = coolBall.ySpeed + coolBall.GRAVITY; // setting new y speed affected by gravity
        double newXPos = coolBall.xPos + coolBall.xSpeed; // potential x position
        double newYPos = coolBall.yPos + coolBall.ySpeed; // potential y position
        if (newXPos - coolBall.radius < 0.0) // checking if the ball is collided with the left edge of canvas
            coolBall.xSpeed= -1 * coolBall.xSpeed; // colliding it elastically
        else if (newXPos + coolBall.radius > 16.0) // checking if the ball is collided with the right edge of canvas
            coolBall.xSpeed = -1 * coolBall.xSpeed; // colliding it elastically
        else
            coolBall.xPos = newXPos; // or moving the ball if conditions are not met
        if (newYPos - coolBall.radius < 0.0) // checking if the ball is collided with the bottom edge of canvas
            coolBall.ySpeed = -1 * coolBall.ySpeed;  // colliding it elastically
        else
            coolBall.yPos = newYPos; // or moving the ball if conditions are not met
    }

    /**
     * Javadoc description part:
     * checks if the ball is collided with arrow
     *
     * Javadoc tags part:
     * @param coolBall Ball to collide
     * @param coolArrow Arrow to collide
     */
    public static boolean doesBallCollideWithArrow(Ball coolBall, Arrow coolArrow) {
        double rightEnd = coolBall.xPos + coolBall.radius; // ball's right end
        double leftEnd = coolBall.xPos - coolBall.radius; // ball's left end
        double downEnd = coolBall.yPos - coolBall.radius; // ball's bottom end
        if (downEnd < coolArrow.yPos + 4.5){ // checking if the balls bottom end is lower than arrows upper end
            if ((leftEnd < coolArrow.xPos) & (rightEnd > coolArrow.xPos)) // checking if the arrows x position is between balls right and left ends
                return true; // collision is met
            else
                return false; // collision is not met
        }
        else
            return false; // collision is not met
    }

    /**
     * Javadoc description part:
     * checks if the ball is collided with player
     *
     * Javadoc tags part:
     * @param coolBall Ball to collide
     * @param coolGuy Player to collide
     */
    public static boolean doesBallCollideWithPlayer(Ball coolBall, Player coolGuy){
        double rightEnd = coolBall.xPos + coolBall.radius; // ball's right end
        double leftEnd = coolBall.xPos - coolBall.radius; // ball's left end
        double downEnd = coolBall.yPos - coolBall.radius; // ball's bottom end
        if (downEnd < coolGuy.yPos + 0.56){ // checking if the balls bottom end is lower than players upper end
            if ((leftEnd < coolGuy.xPos + 0.40) & (rightEnd > coolGuy.xPos - 0.40)) // checking if the balls is in touch with the players right or left ends
                return true; // collision is met
            else
                return false; // collision is not met
        }
        else
            return false; // collision is not met
    }
}