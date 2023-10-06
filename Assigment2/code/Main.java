/**
 *Javadoc description part:
 * Program lets user to play Bubble Trouble game
 * Javadoc tags part:
 * @author Alperen Akyol, Student ID: 2021400078
 * @since Date: 15.04.2023
 */
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Environment.setStdDrawSettings(); // settings like canvas width etc.
        boolean win; // for checking the win condition
        boolean isKeyPressed = true; // for the first time playing

        while (true) { // replay loop
            Player coolGuy = new Player(); // creating a new player
            Arrow coolArrow = new Arrow(coolGuy.xPos); // creating a new arrow just for workaround to a problem
            ArrayList<Ball> ballsList = new ArrayList<>(); // list for balls
            // starting 3 balls
            ballsList.add(new Ball(0,4,0.6575));
            ballsList.add(new Ball(1,5.33,0.815));
            ballsList.add(new Ball(2,4,1.13));

            if(StdDraw.isKeyPressed(KeyEvent.VK_N)) // checks if the n key is pressed in the game screen
                System.exit(0); // exits the game

            if(!(StdDraw.isKeyPressed(KeyEvent.VK_Y))){ // checks if the y key is pressed in the game screen
                if (!isKeyPressed) // infinitely loops until y or n is pressed
                    continue;
            }
            Bar.takeStartTime(); // holds game's start time

            while (true) { // main game loop
                Environment.drawBackground(); //draws background

                if (Arrow.doesArrowExist){ // checks if arrow does exist
                    coolArrow.moveArrow(); // moves arrow upwards
                    Environment.drawArrow(coolArrow.xPos,coolArrow.yPos); // draws arrow to the screen
                }

                else { // arrow does not exist condition
                    if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) { // checks if the space bar is pressed to create new arrow
                        coolArrow.createArrow(coolGuy.xPos); // creates a new arrow from the bottom of the player
                        Arrow.doesArrowExist = true; // arrow exists condition
                    }
                }

                Environment.drawBarBackground(); // draws bar background
                coolGuy.movePlayer(); // moves the player according the user input
                Environment.drawPlayer(coolGuy.xPos,coolGuy.yPos,coolGuy.playerWidth,coolGuy.playerHeight); // draws the player

                if(Bar.remainingTime() < 0){ // checks if the time is up
                    win = false;
                    break; // ends the loop
                }

                Environment.drawBar(); // draws the time bar

                if(ballsList.size() == 0){ // checks if the all the balls are gone
                    win = true;
                    break; // ends the loop
                }

                boolean ballCollideCheck = false; // for holding ball-arrow collision
                boolean gameOverCheck = false; // for holding ball-player collision
                int ballIndex = -1; // for holding the collided ball's index
                Ball collidedBall = new Ball(); // for holding the collided balls info

                for (int i = 0; i < ballsList.size() ; i++){ // traverse through ball list
                    Ball coolBall = ballsList.get(i); // current ball
                    Ball.moveBall(coolBall); // moves the ball
                    if (Ball.doesBallCollideWithPlayer(coolBall,coolGuy)){ // checks if the ball is colliding with player
                        gameOverCheck = true;
                    }
                    if (Arrow.doesArrowExist) { // checks if the arrow exist
                        if (Ball.doesBallCollideWithArrow(coolBall, coolArrow)) { // checks if the ball is colliding with the arrow
                            ballIndex = i;
                            ballCollideCheck = true;
                            collidedBall = coolBall;
                            Arrow.doesArrowExist = false; // deletes the arrow
                        }
                    }
                    Environment.drawBall(coolBall.xPos,coolBall.yPos,coolBall.radius); // draws the ball
                }

                if (gameOverCheck){
                    win = false;
                    break; // ends the loop
                }

                if (ballCollideCheck){
                    ballsList.remove(ballIndex); // removes the ball from the ball list
                    if (!(collidedBall.ballLevel==0)){ // if ball level is not 0, splits it to two smaller level balls, facing opposite directions
                        ballsList.add(new Ball(collidedBall.ballLevel-1, collidedBall.xPos, collidedBall.yPos,0.032));
                        ballsList.add(new Ball(collidedBall.ballLevel-1, collidedBall.xPos, collidedBall.yPos,-0.032));
                    }
                }

                StdDraw.show(); // shows the canvas
                StdDraw.pause(Environment.PAUSE_DURATION); // waits a couple milliseconds
                StdDraw.clear(); // clears the canvas to draw next frame
            }

            if (!win) { // lose condition
                Environment.drawGameScreen(); // draws the game screen
                Environment.writeGameOver(); // writes game over
                StdDraw.show(); // shows the canvas
                isKeyPressed = false; // for checking if user pressed y or n
            }

            else if (win){
                Environment.drawGameScreen(); // draws the game screen
                Environment.writeYouWon(); // writes you won
                StdDraw.show(); // shows the canvas
                isKeyPressed = false; // for checking if user pressed y or n
            }
        }


    }

}