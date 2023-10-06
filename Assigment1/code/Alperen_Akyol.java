/**
 *Javadoc description part:
 * Program prints the unique route between two metro points and draws the route on StdDraw Screen
 * Javadoc tags part:
 * @author Alperen Akyol, Student ID: 2021400078
 * @since Date: 26.03.2023
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
public class Alperen_Akyol {
    public static void main(String[] args) throws FileNotFoundException{
        // reading info from input.txt
        String fileName = "input.txt";
        File file = new File(fileName);
        // if input.txt not exist, program will write a prompt and exit
        if (!file.exists()) {
            System.out.printf("%s can not be found.", fileName);
            System.exit(1); // exit the program
        }
        Scanner inputCoordinatesScanner = new Scanner(file); // creating a new scanner for file reading
        ArrayList<String> lineNames = new ArrayList<String>(); // arraylist that will contain line names according to indexes
        ArrayList<String> lineColors = new ArrayList<String>(); // arraylist that will contain line colors according to indexes
        ArrayList<ArrayList<String>> linePointsRaw = new ArrayList<>(); // arraylist that will contain line points (* no trimmed) according to indexes
        ArrayList<ArrayList<String>> linePoints = new ArrayList<>(); // arraylist that will contain line names (* trimmed) according to indexes
        ArrayList<String>[] lineCoordinates = new ArrayList[10]; // arraylist that will contain line coordinates according to indexes
        // adds empty array lists to linePointsRaw, linePoints, and lineCoordinates
        for (int j = 0; j <10; j++) {
            linePointsRaw.add(new ArrayList<String>());
            linePoints.add(new ArrayList<String>());
            lineCoordinates[j] = new ArrayList<String>();
        }
        // splits every two lines into lineNames, LineColors, linePointsRaw, and linePoints
        for (int i = 0; i < 10; i++) {
            lineNames.add(inputCoordinatesScanner.next());
            lineColors.add(inputCoordinatesScanner.nextLine().trim());
            String[] scannerLineArray = inputCoordinatesScanner.nextLine().split(" ");
            for (int u = 0; u < scannerLineArray.length; u=u+2) {
                linePointsRaw.get(i).add(scannerLineArray[u]);
                linePoints.get(i).add(scannerLineArray[u]);
                lineCoordinates[i].add(scannerLineArray[u+1]);
            }
        }
        // clears linePoints from *
        for (int i= 0;i<linePoints.size();i++){
            for (int j =0;j<linePoints.get(i).size();j++){
                if (linePoints.get(i).get(j).substring(0,1).equals("*"))
                    linePoints.get(i).set(j,linePoints.get(i).get(j).substring(1));
            }
        }
        String[] breakpointArray = new String[7]; // array that will contain breakpoint names according to indexes
        ArrayList<String> breakpointInfoArray = new ArrayList<>(7); // arraylist that will contain breakpoint lines according to indexes
        // splits breakpoints from their lines and adds them to according arrays
        for (int i = 0; i < 7; i++) {
            breakpointArray[i] = inputCoordinatesScanner.next();
            breakpointInfoArray.add(inputCoordinatesScanner.nextLine().trim());
        }
        inputCoordinatesScanner.close(); // closes the input scanner object
        Scanner input = new Scanner(System.in); // opens a new scanner for console inputs
        String startStation = input.nextLine();
        String endStation = input.nextLine();
        input.close(); // closes the input scanner
        ArrayList<String> routeList = new ArrayList<String>(); // array that will contain the unique rote line stations
        ArrayList<String> routeCoordinatesList = new ArrayList<String>(); // array that will contain the unique rote line station coordinates
        String startLine = new String(); // string that will contain the start line
        String endLine = new String(); // string that will contain the end line
        Boolean startCheck = true; // boolean that will be used for checking if the start station exists
        Boolean endCheck = true; // boolean that will be used for checking if the end station exists
        // finds the lines of start and end stations
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < linePoints.get(i).size(); j++) {
                if (linePoints.get(i).get(j).equals(startStation)) {
                    startLine = lineNames.get(i);
                    startCheck = false;
                }
                if (linePoints.get(i).get(j).equals(endStation)){
                    endLine = lineNames.get(i);
                    endCheck = false;
                }
            }
        }
        // gives and error message and closes the program if one of the inputs does not exist on the map
        if (startCheck||endCheck){
            System.out.println("The station names provided are not present in this map.");
            System.exit(1);
        }
        ArrayList<Integer> solutionList = pathFinder(startLine,endLine,breakpointInfoArray,new ArrayList<>()); // contains the breakpoint indexes between start and end stations
        ArrayList<String> solutionBP = new ArrayList<>(); // will contain the breakpoint names
        // fills the solutionBP with breakpoint names
        for (int i = 0;i< solutionList.size();i++) {
            solutionBP.add(breakpointArray[solutionList.get(i)]);
        }
        // fills up routeList and routeCoordinatesList if start and end lines are the same
        if ((startLine.equals(endLine))) {
            int indexBeginning = linePoints.get(lineNames.indexOf(startLine)).indexOf(startStation);
            int indexEnd = linePoints.get(lineNames.indexOf(startLine)).indexOf(endStation);
            if (indexBeginning < indexEnd) { // checks which one of the indexes does come first
                for (int i = indexBeginning; i < indexEnd; i++) {
                    routeList.add(linePoints.get(lineNames.indexOf(startLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(i));
                }
                routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(indexEnd));
            } else { // checks which one of the indexes does come first
                for (int i = indexBeginning; i > indexEnd; i--) {
                    routeList.add(linePoints.get(lineNames.indexOf(startLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(i));
                }
                routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(indexEnd));
            }
            routeList.add(endStation);
        }
        // adds the only end station if start and end stations are the same
        else if (startStation.equals(endStation)){
            routeList.add(endStation);
        }
        // gives error message and closes the program if stations are not connected
        else if (solutionList.size() == 0){
            System.out.println("These two stations are not connected");
            System.exit(1);
        }
        // if one of the conditions does not occur fills up the routeList and routeCoordinatesList in three steps
        else {
            // firstly adds the stations from first station to first breakpoint
            int indexBeginning = linePoints.get(lineNames.indexOf(startLine)).indexOf(startStation); // index of first station
            int indexEnd = linePoints.get(lineNames.indexOf(startLine)).indexOf(solutionBP.get(0)); // index of first breakpoint
            if (indexBeginning < indexEnd){ // checks which one of the indexes does come first
                for (int i=indexBeginning; i< indexEnd;i++){
                    routeList.add(linePoints.get(lineNames.indexOf(startLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(i));
                }
            }
            else{ // checks which one of the indexes does come first
                for (int i=indexBeginning; i> indexEnd;i--){
                    routeList.add(linePoints.get(lineNames.indexOf(startLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(startLine)].get(i));
                }
            }
            // secondly adds the stations from first breakpoint to last breakpoint (only if number of breakpoints is bigger than one) with iterating through breakpoints
            if (solutionList.size()>1){
                for (int i=1;i<solutionList.size();i++){
                    //finding the line that using breakpoint for
                    String currentLine = new String();
                    String[] currentBreakpointArray = breakpointInfoArray.get(solutionList.get(i-1)).split(" ");
                    for (int j=0;j< currentBreakpointArray.length;j++){
                        if (breakpointInfoArray.get(solutionList.get(i)).contains(currentBreakpointArray[j]))
                            currentLine = currentBreakpointArray[j];
                    }
                    indexBeginning = linePoints.get(lineNames.indexOf(currentLine)).indexOf(solutionBP.get(i-1));
                    indexEnd = linePoints.get(lineNames.indexOf(currentLine)).indexOf(solutionBP.get(i));
                    if (indexBeginning < indexEnd){ // checks which one of the indexes does come first
                        for (int j=indexBeginning; j< indexEnd;j++){
                            routeList.add(linePoints.get(lineNames.indexOf(currentLine)).get(j));
                            routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(currentLine)].get(j));
                        }
                    }
                    else{ // checks which one of the indexes does come first
                        for (int j=indexBeginning; j> indexEnd;j--){
                            routeList.add(linePoints.get(lineNames.indexOf(currentLine)).get(j));
                            routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(currentLine)].get(j));
                        }
                    }
                }
            }
            //lastly adds the stations from last breakpoint to end station
            indexBeginning = linePoints.get(lineNames.indexOf(endLine)).indexOf(solutionBP.get(solutionBP.size()-1));
            indexEnd = linePoints.get(lineNames.indexOf(endLine)).indexOf(endStation);
            if (indexBeginning < indexEnd){ // checks which one of the indexes does come first
                for (int i=indexBeginning; i< indexEnd;i++){
                    routeList.add(linePoints.get(lineNames.indexOf(endLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(endLine)].get(i));
                }
            }
            else{ // checks which one of the indexes does come first
                for (int i=indexBeginning; i> indexEnd;i--){
                    routeList.add(linePoints.get(lineNames.indexOf(endLine)).get(i));
                    routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(endLine)].get(i));
                }
            }
            routeList.add(endStation);
            routeCoordinatesList.add(lineCoordinates[lineNames.indexOf(endLine)].get(indexEnd));
        }
        // prints all the route to the console
        for (int i =0 ; i<routeList.size();i++)
            System.out.println(routeList.get(i));
        //drawing the map
        ArrayList<String> routeHistory = new ArrayList<>(); //array list that will contain the stations that are visited before
        Color PRINCETON_ORANGE = new Color(255, 143, 0); // defines princeton orange color
        StdDraw.setCanvasSize(1024, 482);
        StdDraw.setXscale(0,1024);
        StdDraw.setYscale(0,482);
        StdDraw.enableDoubleBuffering(); // for faster animations
        StdDraw.setFont( new Font("Helvetica", Font.BOLD, 8) ); // station name font
        // main animation loop
        for (int d = 0;d<routeList.size();d++){
            StdDraw.picture(512,241,"background.jpg");
            StdDraw.setPenRadius(0.012); // pen size for lines
            // draws all the lines with their colors
            for (int i = 0; i < 10; i++) {
                String lineColorAllTogether = lineColors.get(i); // the current line color as string, not split
                String[] tempColorArray = lineColorAllTogether.split(","); // array that contains RGB values, splitted
                int lineColorRed = Integer.parseInt(tempColorArray[0]);
                int lineColorGreen = Integer.parseInt(tempColorArray[1]);
                int lineColorBlue = Integer.parseInt(tempColorArray[2]);
                Color lineColor = new Color(lineColorRed, lineColorGreen, lineColorBlue); // line color
                StdDraw.setPenColor(lineColor);
                // draws the line
                for (int j = 0; j < lineCoordinates[i].size() - 1; j++) {
                    String[] tempCoordinateSplitArray = lineCoordinates[i].get(j).split(",");
                    int xCoordinateStart = Integer.parseInt(tempCoordinateSplitArray[0]);
                    int yCoordinateStart = Integer.parseInt(tempCoordinateSplitArray[1]);
                    tempCoordinateSplitArray = lineCoordinates[i].get(j + 1).split(",");
                    int xCoordinateEnd = Integer.parseInt(tempCoordinateSplitArray[0]);
                    int yCoordinateEnd = Integer.parseInt(tempCoordinateSplitArray[1]);
                    StdDraw.line(xCoordinateStart, yCoordinateStart, xCoordinateEnd, yCoordinateEnd);
                }
            }
            StdDraw.setPenRadius(0.01); // pen size for stations
            // draws all the stations and writes their names (if station name does contain *)
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < lineCoordinates[i].size(); j++) {
                    String[] tempCoordinateSplitArray = lineCoordinates[i].get(j).split(","); // array that contains station coordinates, split
                    int xCoordinate = Integer.parseInt(tempCoordinateSplitArray[0]);
                    int yCoordinate = Integer.parseInt(tempCoordinateSplitArray[1]);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.line(xCoordinate, yCoordinate, xCoordinate, yCoordinate); // draws the station points
                    StdDraw.setPenColor(Color.BLACK);
                    if (linePointsRaw.get(i).get(j).substring(0,1).equals("*"))
                        StdDraw.text(xCoordinate,yCoordinate+5,linePointsRaw.get(i).get(j).substring(1)); // writes the station names
                }
            }
            //navigation part
            String[] tempCoordinateSplitArray = routeCoordinatesList.get(d).split(","); // array that contains the current stations coordinates, split
            //current station
            int xCoordinate = Integer.parseInt(tempCoordinateSplitArray[0]);
            int yCoordinate = Integer.parseInt(tempCoordinateSplitArray[1]);
            StdDraw.setPenColor(PRINCETON_ORANGE);
            StdDraw.setPenRadius(0.02);
            StdDraw.line(xCoordinate, yCoordinate, xCoordinate, yCoordinate); // draws the current station point
            // stations that visited beforehand
            routeHistory.add(routeList.get(d));
            StdDraw.setPenRadius(0.01);
            // loop for drawing all the station points that visited beforehand
            for (int h = 0; h < routeHistory.size();h++){
                tempCoordinateSplitArray = routeCoordinatesList.get(h).split(",");
                xCoordinate = Integer.parseInt(tempCoordinateSplitArray[0]);
                yCoordinate = Integer.parseInt(tempCoordinateSplitArray[1]);
                StdDraw.line(xCoordinate, yCoordinate, xCoordinate, yCoordinate);
            }
            StdDraw.show();
            StdDraw.pause(300); // pause for user beneficence
            StdDraw.clear();
        }
    }
    /**
     * Javadoc description part:
     * Finds the unique route between initial line and final line
     *
     * Javadoc tags part:
     * @param currentLine Initial Metro Line
     * @param destinationLine Final Metro Line
     * @param bpIndexArray Breakpoint lines array
     * @param blacklist lines visited beforehand
     * @return unique path array
     */
    public static ArrayList pathFinder(String currentLine, String destinationLine, ArrayList<String> bpIndexArray, ArrayList<Integer> blacklist){
        // loop for iterating through breakpoints
        for (int i=0; i < 7; i++) {
            if (blacklist.contains(i)) // if current breakpoint has been visited beforehand skips the current breakpoint
                continue;
            if (bpIndexArray.get(i).contains(currentLine)) { // if current breakpoint contains currentLine
                // if current breakpoint also contains endLine it exits recursion and returns blacklist as the breakpoint indexes
                if (bpIndexArray.get(i).contains(destinationLine)) {
                    blacklist.add(i);
                    return blacklist;
                }
                // if current breakpoint does not contain endLine recursion is called for other lines in the breakpoint
                String[] bpStringArray = bpIndexArray.get(i).split(" "); // array that contains all lines in the breakpoint
                for (int j=0; j < bpStringArray.length;j++) { // iterating through all the lines in the breakpoint
                    if (bpStringArray[j].equals(currentLine)) // recursion is not called again for currentLine
                        continue;
                    ArrayList<Integer> tempBlacklist = new ArrayList<Integer>(blacklist); // creates a temporary blacklist for recursions
                    tempBlacklist.add(i);
                    ArrayList<Integer> checkList= pathFinder(bpStringArray[j],destinationLine,bpIndexArray,tempBlacklist); // calls the recursion for other lines in breakpoint
                    if (checkList.size()>0) // if recursion is successful for finding a valid route it returns that list of breakpoint indexes(old name = blacklist)
                        return checkList;
                }
            }
        }
        // if method fails to find a valid route for two lines it returns an empty array list
        return new ArrayList<>();
    }
}