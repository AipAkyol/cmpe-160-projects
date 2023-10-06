/**
 *Javadoc description part:
 * Program is a game that modifies and finds all lakes that in a floating terrain after money/water accumulation
 * Javadoc tags part:
 * @author Alperen Akyol, Student ID: 2021400078
 * @since Date: 04.05.2023
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

 public class Alperen_Akyol {
    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<String> lakeNames = new ArrayList<>(); // creating an arraylist for lake naming
        for (int i = 0;i<26;i++) // loop for lake names from "A" to "Z"
            lakeNames.add(""+(char)(i+'A'));
        for (int i = 0;i<26;i++){ // loop for lake names from "AA" to "ZZ"
            for (int j = 0;j<26;j++)
                lakeNames.add(""+(char)(i+'A')+(char)(j+'A'));
        }

        String fileName = "src/input.txt"; // input file name
        File file = new File(fileName);

        // if input.txt not exist, program will write a prompt and exit
        if (!file.exists()) {
            System.out.printf("%s can not be found.", fileName);
            System.exit(1); // exit the program
        }

        Scanner inputScanner = new Scanner(file); // new scanner for input reading
        int columnNumber = inputScanner.nextInt(); // storing column number for further use
        int rowNumber = inputScanner.nextInt(); // storing row number for further use
        inputScanner.nextLine(); // moving to the next line
        int[][] board = new int[rowNumber][columnNumber]; // creating a 2D array for board

        for(int i = 0; i < rowNumber; i++){ // loop for filling the board with input integers
            for(int j = 0; j < columnNumber; j++){
                board[i][j] = inputScanner.nextInt();
            }
            if (inputScanner.hasNextLine())
                inputScanner.nextLine();
        }

        String[][] lakeBoard = new String[rowNumber][columnNumber]; // new 2D array for lake names

        printBoard(board, columnNumber,lakeBoard); // printing the board for the first time

        Scanner input = new Scanner(System.in); // new scanner for board modifying
        int inputCount = 1; // counting the input count for taking 10 inputs

        while (inputCount < 11){ // loop to ask for input until 10 right formatted inputs taken
            System.out.printf("Add stone %d / 10 to coordinate:", inputCount);
            String playerInput = input.nextLine();
            String[] coordinatesArray = turnCoordinates(playerInput,rowNumber,columnNumber); // changing ths string input to a 2-length array that stores coordinates
            if (coordinatesArray[0] == "fail") { // if input is wrong formatted, the array contains "fail"
                System.out.println("Not a valid step!");
                continue; // for asking the input again
            }
            String columnString = coordinatesArray[0]; // column coordinate
            int rowIndex = Integer.parseInt(coordinatesArray[1]); // row index
            int columnIndex = 0; // creating the column index integer
            for (int i = 0; i < columnString.length() ; i++) { // loop for finding the column index from column name string
                char c = columnString.charAt(columnString.length()-1-i); // iterating through chars in index
                if (i == 0)
                    columnIndex += c - 97;
                if (i == 1)
                    columnIndex += (c - 96) * 26;
            }
            board[rowIndex][columnIndex] += 1; // increasing the corresponding board value
            inputCount += 1; // increasing the successful input count
            printBoard(board,columnNumber,lakeBoard); // printing the updated board
            System.out.println("---------------");
        }

        int maxHeight = 0; //max height in 3D perspective

        for (int i = 0; i < board.length ; i++){ // finding the maximum value of "height" in all the board
            for (int j = 0; j<board[0].length; j++){
                 if (board[i][j]> maxHeight)
                     maxHeight = board[i][j];
            }
        }

        String[][][] terrain3D = new String[rowNumber][columnNumber][maxHeight]; // creating a 3D "terrain" to float the lakes

        for (int i = 0; i<rowNumber; i++){
            for(int j = 0; j< columnNumber; j++){
                for(int k = 0;k< maxHeight; k++)
                    if (k < board[i][j])
                        terrain3D[i][j][k] = "Soil"; // "Soil" is written in the elements that have a value height
                    else
                        terrain3D[i][j][k] = "Water"; // else "Water" is written in "empty" heights
            }
        }

        for (int i = 0; i < rowNumber; i++) { // loop for "leaking" the excess water from right and left ends (for further clearance visit "leakBoard" method)
                for (int k = 0; k < maxHeight; k++){
                    leakBoard(terrain3D,i,0,k);
                    leakBoard(terrain3D,i,columnNumber-1,k);
                }
            }

        for (int j = 0; j < rowNumber; j++) { // loop for "leaking" the excess water from up and bottom ends (for further clearance visit "leakBoard" method)
            for (int k = 0; k < maxHeight; k++){
                leakBoard(terrain3D,0,j,k);
                leakBoard(terrain3D,rowNumber-1,j,k);
            }
        }

        int lakeCount = 0; // integer for counting "lakes"
        ArrayList<Integer> lakeVolumes = new ArrayList<>(); // array list for storing lake volumes to calculate final score

        for (int i = 0; i<rowNumber;i++){ // loops that transverses all 3D terrain
            for ( int j = 0; j < columnNumber; j++){
                for (int k = maxHeight-1; k >=0 ; k--){
                    if (lakeBoard[i][j] != null) // if a "lake" is already found in that column-row combination skips that matrix element
                        break;
                    if (terrain3D[i][j][k].equals("Air")) // if "Air" is found checks the element one "below" element ("Air" is explained at "leakBoard" method)
                        continue;
                    if (terrain3D[i][j][k].equals("Soil")) // if "Soil" is found skips all remaining heights since lakes cannot form below "Soil"
                        break;
                    if ((terrain3D[i][j][k].equals("Water"))){ // if "Water" is found "labelLakes" method is called and creates a new lake in lakeVolumes and increases the lake count
                        lakeVolumes.add(0);
                        labelLakes(terrain3D,i,j,k,lakeNames.get(lakeCount),lakeBoard,lakeVolumes);
                        lakeCount += 1;
                    }
                }
            }
        }

        printBoard(board,columnNumber,lakeBoard); // prints the board with lakes
        double totalScore = 0; // double for storing score

        for (int i = 0; i< lakeVolumes.size();i++) // calculates the score
            totalScore += Math.pow((double)lakeVolumes.get(i),0.5);

        System.out.printf("Final Score: %.2f",totalScore); // prints the score with 2 significant scores

        input.close(); // closes the input scanner
    }

     /**
      * Javadoc description part:
      * prints the board with or without lakes
      *
      * Javadoc tags part:
      * @param board 2D Board
      * @param columnNumber Max Column Number (for checking the index range)
      * @param lakeBoard 2D Board with lake names
      */
    public static void printBoard(int[][] board, int columnNumber,String[][] lakeBoard) {

        int fullColumnNumber = columnNumber / 26; // a "full" iteration in columns is counted as "a" to "z" so it is 26
        int excessColumnNumber = columnNumber % 26; // excess columns

        for (int i = 0; i < board.length; i++){ // iterating through rows
            System.out.printf("%3.3s",i); // row number, starting from 0
            for (int j = 0; j < board[i].length; j++){ // iterating through columns
                int matrixNumber = board[i][j];
                if (lakeBoard[i][j] != null) // if a lake exists on corresponding element, lake name is printed
                    System.out.printf("%3.3s",lakeBoard[i][j]);
                else // else the element is printed
                    System.out.printf("%3.3s",matrixNumber);
            }
            System.out.println();
        }

        //from now on the column letters printed
        System.out.print("   ");

        if (fullColumnNumber==0){ // if there is not any full "alphabet iteration"
            for(int i = 0; i < excessColumnNumber; i ++){
                System.out.printf("  "+(char) (i+97));
            }
        }

        if (fullColumnNumber>0) { // if any full "alphabet iteration" exists
            for(int i = 0; i < 26; i ++){
                System.out.printf("  "+(char) (i+97));
            }
            for(int i = 0; i < fullColumnNumber-1; i++){
                for( int j = 0; j < 26; j++){
                    System.out.printf(" " + ((char)(i+97)) + (char)(j+97));
                }
            }
            for(int i = 0; i < excessColumnNumber; i ++){
                System.out.printf(" "+ (char)(fullColumnNumber-1+97) + (char) (i+97));
            }
        }

        System.out.println();
    }

     /**
      * Javadoc description part:
      * checks and turns the coordinates string to and 2-length array
      *
      * Javadoc tags part:
      * @param coordinatesString Input String
      * @param rowNumber Max Row Number for checking out of range inputs
      * @param columnNumber Max Column Number for checking out of range inputs
      * @return coordinates array
      */
    public static String[] turnCoordinates(String coordinatesString , int rowNumber, int columnNumber) {

        String[] outArray = new String[2]; // output array will be filled with either coordinates or "fail"s

        if (!(isAlNumLowered(coordinatesString))) { // checking if input only consists "a" to "z" and "0" to "9"
            outArray[0] = "fail";
            outArray[1] = "fail";
            return outArray;
        }

        if (coordinatesString.length()>5) { //checking if the input length is more than 5 (max input can be zz702)
            outArray[0] = "fail";
            outArray[1] = "fail";
            return outArray;
        }

        boolean checkFormat = false; // boolean for checking if letter part is found
        String column = "";
        String row = "";

        for (int i = 0; i < coordinatesString.length(); i++) { // iterating through input
            char c = coordinatesString.charAt(i);
            if (checkFormat && (c >= 'a' && c <= 'z')){ // if letter part is found and again a letter comes
                outArray[0] = "fail";
                outArray[1] = "fail";
                return outArray;
            }
            if(!checkFormat && (c >= '0' && c <= '9') && column.equals("")){ // if letter part is not found and a number comes
                outArray[0] = "fail";
                outArray[1] = "fail";
                return outArray;
            }
            if (!checkFormat && (c >= 'a' && c <= 'z')){ // if letter part is not found and a letter comes
                column += c;
            }
            if((c >= '0' && c <= '9')){ // taking the number part
                row += c;
                checkFormat = true;
            }
        }

        if(row.equals("") || column.equals("")){ // if one of the coordinates is empty
            outArray[0] = "fail";
            outArray[1] = "fail";
            return outArray;
        }

        if(Integer.parseInt(row) > rowNumber-1){ // if row is out of range
            outArray[0] = "fail";
            outArray[1] = "fail";
            return outArray;
        }

        if (column.length()>2){ // if column is too long
            outArray[0] = "fail";
            outArray[1] = "fail";
            return outArray;
        }

        if (column.length() == 1){
            char c = column.charAt(0);
            if (c-97 > columnNumber-1){ // if column is out of range
                outArray[0] = "fail";
                outArray[1] = "fail";
                return outArray;
            }
        }

        if (column.length() == 2){
            int columnValue = 0;
            columnValue += (column.charAt(0)-96) * 26;
            columnValue += (column.charAt(1)-96);
            if (columnValue > columnNumber){ // if column is out of range
                outArray[0] = "fail";
                outArray[1] = "fail";
                return outArray;
            }
        }

        outArray[0] = column;
        outArray[1] = row;
        return outArray;
    }

     /**
      * Javadoc description part:
      * checks thew input string only consists "a" to "z" and "0" to "9"
      *
      * Javadoc tags part:
      * @param s Input String
      * @return boolean value
      */
    public static boolean isAlNumLowered(String s) {
        for (int i = 0; i < s.length(); i++) { // iterate the string
            char c = s.charAt(i);
            if (!(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9')) { // if the char is not one of the given values
                return false;
            }
        }
        return true;
    }

     /**
      * Javadoc description part:
      * modifies the 3d terrain according to leaking rules
      *
      * Javadoc tags part:
      * @param terrain 3D Array "terrain"
      * @param row Row Index
      * @param col Column Index
      * @param height Height Index
      */
    public static void leakBoard(String[][][] terrain, int row, int col, int height){
        //taking the max ranges for avoiding out of range errors
        int rowNumber = terrain.length;
        int columnNumber = terrain[0].length;
        int heightNumber = terrain[0][0].length;

        if (row < 0 || row >= rowNumber || col < 0 || col >= columnNumber || height < 0 || height >= heightNumber)  // check if the current cell is within the bounds of the terrain
            return;

        if (terrain[row][col][height] == "Soil" || terrain[row][col][height] == "Air") // check if the current cell is not water to leak
            return;

        terrain[row][col][height] = "Air"; // leak the water by transforming it to air

        // recurse through all 8 directions
        leakBoard(terrain,row+1,col,height);
        leakBoard(terrain,row+1,col-1,height);
        leakBoard(terrain,row+1,col+1,height);
        leakBoard(terrain,row-1,col,height);
        leakBoard(terrain,row,col-1,height);
        leakBoard(terrain,row,col+1,height);
        leakBoard(terrain,row-1,col+1,height);
        leakBoard(terrain,row-1,col-1,height);
    }

     /**
      * Javadoc description part:
      * labels lakes with given label and spreads the label through 8 directions if lake continues
      *
      * Javadoc tags part:
      * @param terrain 3D Array "terrain"
      * @param row Row Index
      * @param col Column Index
      * @param height Height Index
      * @param lakeLabel Lake Label(Name)
      * @param lakeBoard 2D Array with lake names
      * @param lakeVolumes Array that stores lake volumes
      */
    public static void labelLakes(String[][][] terrain, int row, int col, int height, String lakeLabel, String[][] lakeBoard, ArrayList<Integer> lakeVolumes){
        //taking the max ranges for avoiding out of range errors
        int rowNumber = terrain.length;
        int columnNumber = terrain[0].length;

        if (row < 0 || row >= rowNumber || col < 0 || col >= columnNumber)  // check if the current cell is within the bounds of the terrain
            return;

        if (terrain[row][col][height] == "Soil" || lakeBoard[row][col] == lakeLabel) // check if the current cell is labeled or is "Soil"
            return;

        for (int k = height; k >=0;k--){ // iterate until bottom of "Lake"
            if (terrain[row][col][k] == "Water"){
                int lastLakeIndex = lakeVolumes.size()-1;
                lakeVolumes.set(lastLakeIndex,lakeVolumes.get(lastLakeIndex)+1); // increase current lake's volume
            }
        }

        lakeBoard[row][col] = lakeLabel; // label the lake

        // recurse through all 8 directions
        labelLakes(terrain,row+1,col,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row+1,col-1,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row+1,col+1,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row-1,col,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row,col-1,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row,col+1,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row-1,col+1,height,lakeLabel,lakeBoard,lakeVolumes);
        labelLakes(terrain,row-1,col-1,height,lakeLabel,lakeBoard,lakeVolumes);
    }

}