package fifteenpuzzle;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameSolver {

    private Vertex root;    // Starting state
    static private Vertex targetVertex; // Target state
    public static int SIZE; // Size of board being solved
    static private int xVal;    // x coordinate of empty position on board
    static private int yVal;    // y coordinate of empty position on board

    // The four possible moves on the board
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    private static  int[][] gameBoard;  // 2D integer aray on to which board values are loaded and is used to create a Vertex object

    private static final List<String> outputSolution = new ArrayList<>();   // output string to hold steps to take on solution path

    // Constructor, takes in input file name as argument
    public GameSolver(File input) throws IOException {

        // New reader object to read file
        BufferedReader newReader = new BufferedReader(new FileReader(input));

        // Get size of board from one line
        GameSolver.SIZE = Integer.parseInt(newReader.readLine());

        // Create 2d array to hold board
        gameBoard = new int[SIZE][SIZE];

        // Temporary variables to check for where empty position on board is
        int val1;
        int val2;
        int s;  // To store space between tiles

        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                val1 = newReader.read();
                val2 = newReader.read();
                s = newReader.read();

                // If the temporary variables are spaces i.e. the empty spot
                // set them to be 0
                if (val1 == ' '){
                    val1 = '0';
                }
                if (val2 == ' '){
                    val2 = '0';
                }

                // Loading values on to 2d array
                gameBoard[i][j] = 10 * (val1 - '0') + (val2 - '0');

                // Finding and storing coordinates of empty spot
                if (gameBoard[i][j] == 0){
                    xVal = i;
                    yVal = j;
                }
            }
        }

        newReader.close();

        // Creating first vertex in graph, the starting state of game
        root = new Vertex(gameBoard, xVal, yVal);

        // Creating target vertex, the goal state of game
        targetVertex = new Vertex(setTarget(SIZE), SIZE-1, SIZE-1);
    }

    // Function to set the target state of board based on size
    // Returns a new board
    public int[][] setTarget(int size){

        int[][] target = new int[size][size];
        int count = 1;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                target[i][j] = count;
                count++;
            }
        }

        // Setting last position to be the empty position
        target[size-1][size-1] = 0;

        return target;
    }

    // Returns the starting state of game
    public Vertex getRoot(){
        return root;
    }

    // Returns the string list containing the solution of board as output
    // Writing to file purpose
    public static List<String> getOutputSolution(){
        return outputSolution;
    }

    // Creating a clone of board
    // For the purpose of making moves on copy of board which then becomes the neighbour
    public static Vertex copyVertex(Vertex v){

        int[][] vertex = new int[SIZE][];

        for (int i = 0; i < v.getVal().length; i++){
            vertex[i] = v.getVal()[i].clone();
        }

        return new Vertex(vertex, v.getXpos(), v.getYpos());
    }

    // Function to get the successors/neighbours of a vertex
    // Checks for which moves are possible on the current board
    public static Set<Vertex> getNeighbours(Vertex v){

        // Creating set to store neighbours of vertex
        Set<Vertex> neighbours = new HashSet<Vertex>();

        // Storing x and y coordinates of vertex
        int xPos = v.getXpos();
        int yPos = v.getYpos();

        // Checking if moving UP is possible
        // also checking that new position will be within the bounds of the board i.e. position is less than size of board
        if (xPos - 1 >= 0 && xPos - 1 < SIZE){
            neighbours.add(makeMove(copyVertex(v), UP)); // make the move with a copy of original vertex!!
        }

        // Checking if moving DOWN is possible
        if (xPos + 1 > 0 && xPos + 1 < SIZE){
            neighbours.add(makeMove(copyVertex(v), DOWN));
        }

        // Checking if moving LEFT is possible
        if (yPos - 1 >= 0 && yPos - 1 < SIZE){
            neighbours.add(makeMove(copyVertex(v), LEFT));
        }

        // Checking if moving RIGHT is possible
        if (yPos + 1 > 0 && yPos + 1 < SIZE){
            neighbours.add(makeMove(copyVertex(v), RIGHT));
        }

        return neighbours;
    }

    // Function to do the four possible moves on the board
    // Completes the swapping of values using a temporary variable
    public static Vertex makeMove(Vertex v, int direction){

        int temp;

        switch (direction) {
            case UP: // Moving UP, switching with upper tile
                if (v.getXpos() > 0){
                    temp = v.getVal()[v.getXpos()-1][v.getYpos()];  // storing position of upper tile in temporary variable
                    v.getVal()[v.getXpos()-1][v.getYpos()] = v.getVal()[v.getXpos()][v.getYpos()];  // transferring value of upper tile into original tile
                    v.getVal()[v.getXpos()][v.getYpos()] = temp;    // transferring value of original value into upper tile
                    v.setXpos(v.getXpos()-1);   // updating x coordinate of new vertex in graph
                    break;
                }

            case DOWN: // Moving DOWN, switching with lower tile
                if (v.getXpos() < SIZE-1){
                    temp = v.getVal()[v.getXpos()+1][v.getYpos()]; // storing position of lower tile in temporary variable
                    v.getVal()[v.getXpos()+1][v.getYpos()] = v.getVal()[v.getXpos()][v.getYpos()];  // transferring value of lower time into original tile
                    v.getVal()[v.getXpos()][v.getYpos()] = temp;    // transferring value of original tile into lower tile
                    v.setXpos(v.getXpos()+1);   // updating x coordinate of new vertex in graph
                    break;
                }

            case LEFT: // Moving LEFT, switching with left tile
                if (v.getYpos() > 0){
                    temp = v.getVal()[v.getXpos()][v.getYpos()-1];  // storing position of left tile in temporary variable
                    v.getVal()[v.getXpos()][v.getYpos()-1] = v.getVal()[v.getXpos()][v.getYpos()];  // transferring value of left tile into original tile
                    v.getVal()[v.getXpos()][v.getYpos()] = temp;    // transferring value of original tile into left tile
                    v.setYpos(v.getYpos()-1);   // updating y coordinate of new vertex in graph
                    break;
                }

            case RIGHT: // Moving RIGHT, switching with right tile
                if (v.getYpos() < SIZE-1){
                    temp = v.getVal()[v.getXpos()][v.getYpos()+1];  // storing position of right tile in temporary variable
                    v.getVal()[v.getXpos()][v.getYpos()+1] = v.getVal()[v.getXpos()][v.getYpos()];  // transferring value of right tile into original tile
                    v.getVal()[v.getXpos()][v.getYpos()] = temp;    // transferring value of original tile into right tile
                    v.setYpos(v.getYpos()+1);   // updating y coordinate of new vertex in graph
                    break;
                }

            default: {
                return v;
            }
        }

        return v;
    }

    // Function to choose correct algorithm based on board size
    public static void algorithmSwitch(Vertex start){
        if (start.getVal().length > 3){
            algorithm1(start); // For boards of size more than 3
        } else {
            algorithm2(start);  // For boards of size less than or equal to 3
        }
    }

    // A star algorithm used for boards of size greater than 3
    public static void algorithm1(Vertex start){

        // Creating open list using a priority queue since minimum value goes first
        // Holds that nodes that are being processed
        PriorityQueue<Vertex> openList = new PriorityQueue<>(
                new Comparator<Vertex>() {
                    // Comparison function to compare heuristic values of two vertices
                    @Override
                    public int compare(Vertex o1, Vertex o2) {
                        return o1.getF() - o2.getF();
                    }
                }
        );

        // Creating a closed list using HashSet
        // Holds the nodes that have been processed
        Set<Vertex> closedList = new HashSet<>();

        start.setG(0);   // Setting g(n) of starting vertex to be 0
        start.setH(calcManhattan_LineConflict(start));  // Calculate heuristic of starting vertex
        start.setParent(null);  // Set parent of starting vertex to null as it is the first in graph
        openList.add(start);    // Add starting vertex to open list

        // Checking if open list is empty or not
        while (!openList.isEmpty()){

            // Removing vertex with smallest heuristic value (g(n) + h(n))
            // Priority queue is used so first vertex popped out has minimum value
            Vertex v = openList.remove();

            // Expand to its successors/neighbours
            Set<Vertex> neighbours = getNeighbours(v);

            // Going through the set of vertex's neighbours
            for (Vertex x: neighbours){

                x.setH(calcManhattan_LineConflict(x)); // Calculate heuristic of current vertex

                // If the goal vertex is found, set its parent to be the original vertex and print the path to be taken
                if (x.equals(targetVertex)){
                    x.setParent(v);
                    List<Vertex> solution = getParents(x);
                    addToOutput(solution);
                    x.setG(v.getG()+1);
//                    printPath(x);
                    return;
                }

                // If the current neighbour vertex is not the goal vertex
                // if BOTH the open and closed lists do not have the neighbour vertex in them,
                // set its parent to be the original vertex in graph and add it to open queue so that its neighbours can be processed
                else if (!openList.contains(x) && !closedList.contains(x)){
                    x.setG(v.getG()+1);
                    x.setParent(v);
                    openList.add(x);
                }

            }

            // Add the processed vertex to the closed set.
            closedList.add(v);
        }
    }

    // A star algorithm used for boards of size less than or equal to 3
    public static void algorithm2(Vertex start){

        PriorityQueue<Vertex> openList = new PriorityQueue<>(
                new Comparator<Vertex>() {
                    @Override
                    public int compare(Vertex o1, Vertex o2) {
                        return o1.getF() - o2.getF();
                    }
                }
        );

        Map<Vertex, Integer> closedList = new HashMap<>();

        start.setG(0);
        start.setH(calcManhattan_LineConflict(start));
        start.setParent(null);
        openList.add(start);

        while (!openList.isEmpty()){

            Vertex v = openList.poll();

            if (v.equals(targetVertex)){
                List<Vertex> solution = getParents(v);
                addToOutput(solution);
//                printPath(v);
                return;
            }

            closedList.put(v, v.getF());

            Set<Vertex> neighbours = getNeighbours(v);
            for (Vertex x: neighbours){

                int temp = v.getG();

                if (!Arrays.deepEquals(v.getVal(), x.getVal())){
                    temp = v.getG() + 1;
                }

                if (closedList.containsKey(x) && temp >= x.getG()){
                    continue;
                }

                if (!openList.contains(x) || temp < x.getG()){
                    x.setParent(v);
                    x.setG(temp);
                    x.setH(calcManhattan_LineConflict(x));
                    openList.add(x);
                }
            }
        }
    }

    // Function to retrace path back to route by referencing the parents of each vertex
    public static ArrayList<Vertex> getParents(Vertex v){
        ArrayList<Vertex> parents = new ArrayList<>();
        LinkedList<Vertex> queue = new LinkedList<>();
        Vertex x;
        queue.add(v);
        while (!queue.isEmpty()){
            x = queue.remove();
            if (x.getParent() != null){
                queue.add(x.getParent());
                parents.add(x);
            }
        }

        return parents;
    }

    // NOT IN USE
    // Alternate implementation of function get parents using stack instead of a queue
//    public static List<Vertex> getParents(Vertex v){
//        ArrayList<Vertex> parents = new ArrayList<>();
//        Stack<Vertex> stack = new Stack<>();
//        Vertex current;
//
//        stack.push(v);
//        while (!stack.isEmpty()){
//            current = stack.pop();
//            if (current.getParent() != null){
//                stack.push(current.getParent());
//                parents.add(current);
//            }
//        }
//
//        return parents;
//    }

    // Function to check the last move made on the solution path to reach the target vertex
    public static void checkTheFirstMove(List<Vertex> solution){
        // * Alternate method commented out

        Point target = targetVertex.getEmptyPosition(); // Storing empty position on target board
        Point current = solution.get(0).getEmptyPosition(); // Storing empty position on current board i.e. the board right before target board is reached
        String move = null; // String variable to hold which move was made
        String tile = null; // String variable to hold which tile was moved
//        int value = 0;

        if (target.y - 1 == current.y){
            move = "U";
//            value = solution.get(0).board[current.y][current.x];
            tile = String.valueOf(solution.get(0).getVal()[current.y][current.x]);
        } else if (target.y + 1 == current.y){
            move = "D";
//            value = solution.get(0).board[current.y][current.x];
            tile = String.valueOf(solution.get(0).getVal()[current.y][current.x]);
        } else if (target.x - 1 == current.x){
            move = "L";
//            value = solution.get(0).board[current.y][current.x];
            tile = String.valueOf(solution.get(0).getVal()[current.y][current.x]);
        } else if (target.x + 1 == current.x){
            move = "R";
//            value = solution.get(0).board[current.y][current.x];
            tile = String.valueOf(solution.get(0).getVal()[current.y][current.x]);
        }

//        String tile = Integer.toString(value);
        outputSolution.add(tile + " " + move);  // Adding to output string
    }

    // Function to check for the rest of the moves made to get to target
    public static void addToOutput(List<Vertex> solution){
        // * Alternate method commented out

        int firstX = solution.get(0).getEmptyPosition().x;
        int firstY = solution.get(0).getEmptyPosition().y;
        int currentX = 0;
        int currentY = 0;
        String move = null;
        String tile = null;
        int value = 0;

        checkTheFirstMove(solution);
        // Checking rest of the moves
        for (int i = 1; i < solution.size(); i++){
            currentX = solution.get(i).getEmptyPosition().x;
            currentY = solution.get(i).getEmptyPosition().y;
            if (firstY - 1 == currentY){
                move = "U";
                tile = String.valueOf(solution.get(i).board[currentY][currentX]);
//                value = solution.get(i).board[currentY][currentX];
//                tile = Integer.toString(value);
                outputSolution.add(tile + " " + move);
                firstX = currentX;
                firstY = currentY;
            } else if (firstY + 1 == currentY){
                move = "D";
                tile = String.valueOf(solution.get(i).board[currentY][currentX]);
//                value = solution.get(i).board[currentY][currentX];
//                tile = Integer.toString(value);
                outputSolution.add(tile + " " + move);
                firstX = currentX;
                firstY = currentY;
            } else if (firstX - 1 == currentX){
                move = "L";
                tile = String.valueOf(solution.get(i).board[currentY][currentX]);
//                value = solution.get(i).board[currentY][currentX];
//                tile = Integer.toString(value);
                outputSolution.add(tile + " " + move);
                firstX = currentX;
                firstY = currentY;
            } else if (firstX + 1 == currentX){
                move = "R";
                tile = String.valueOf(solution.get(i).board[currentY][currentX]);
//                value = solution.get(i).board[currentY][currentX];
//                tile = Integer.toString(value);
                outputSolution.add(tile + " " + move);
                firstX = currentX;
                firstY = currentY;
            }
        }

        // Reversing order of output string to start with first move
        Collections.reverse(outputSolution);
    }

    // Function to calculate Manhattan distance
    public static int calcManhattan(int[][] array){
        int distManhattan = 0;
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array[i].length; j++){
                int position = array[i][j]; // storing position of tile of which manhattan distance is being calculated
                if (position != 0){
                    int targetX = (position - 1) / array.length;    // Getting x coordinate of target position
                    int targetY = (position - 1) % array.length;    // Getting y coordinate of target position
                    int distX = i - targetX;
                    int distY = j - targetY;
                    distManhattan += Math.abs(distX) + Math.abs(distY); // Calculating Manhattan distance and summing it for the entire board
                }
            }
        }
        return distManhattan;
    }

    // Function search in target board and find coordinates of element in its target position.
    // Returns the coordinates as a point
    public static Point searchTargetBoardP(int c, int[][] target, int x, int y){
        Point result = null;
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                if (c == target[i][j]) {
                    result = new Point(i, j);
                }
            }
        }
        return result;
    }

    // Heuristic function that combines Manhattan distance with linear conflict
    // Uses a POINT to compare tiles
    public static int calcManhattan_LineConflict(Vertex v){

        int[][] board = v.getVal(); // Storing vertex to calculate heuristic value of in 2D integer array
        int[][] target = targetVertex.getVal(); // Storing target vertex in 2D integer array

        // Creating point to hold coordinates of elements on the target board
        Point[] targetPos = new Point[board.length*board.length];

        // Finding every element on board in the target board and storing in the point
        for (int[] row: board){
            for (int j = 0; j < board.length; j++){
                if (row[j] >= 0){
                    targetPos[row[j]] = searchTargetBoardP(row[j], target, board.length, board.length);
                }
            }
        }

        int conflicts = 0;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] == 0 || board[i][j] < 0){
                    continue;
                } else {
                    // Searching for conflicts in rows
                    Point p1 = targetPos[board[i][j]];
                    for (int k = 0; k < board.length; k++){
                        if (board[i][k] == 0 || board[i][k] < 0){
                            continue;
                        }
                        Point p2 = targetPos[board[i][k]];

                        // If both tiles are on the same row and need to be swapped, there is a linear conflict
                        if (i == p1.x && p1.x == p2.x && j < k && p1.y > p2.y){
                            conflicts++;
                        }
                    }

                    // Searching for conflicts in columns
                    for (int k = 0; k < board.length; k++){
                        if (board[k][j] == 0 || board[k][j] < 0){
                            continue;
                        }
                        Point p2 = targetPos[board[k][j]];

                        // If both tiles are on the same column and need to be swapped, there is a linear conflict
                        if (j == p1.y && p1.y == p2.y && i < k && p1.x > p2.x){
                            conflicts++;
                        }
                    }
                }
            }
        }

        int manhattan = calcManhattan(board);
        return manhattan + 2*conflicts; // every linear conflict adds 2 additional steps to the Manhattan distance
    }

    // NOT IN USE
    // Heuristic function that combines Manhattan distance with linear conflict
    // Uses an INTEGER ARRAY to compare tiles
//    public static int calcManhattanLC(Vertex v){
//        int[][] board = v.getVal();
//        int[][] target = targetVertex.getVal();
//        int conflicts = 0;
//
//        for (int i = 0; i < board.length; i++){
//            for (int j = 0; j < board.length; j++){
//                if (board[i][j] == 0 || board[i][j] < 0){
//                    continue;
//                }
//
//                int[] targetPos = searchInTarget(target, board[i][j]);
//                if (targetPos[0] == i && targetPos[1] == j){
//                    continue;
//                }
//
//                if (targetPos[0] == i){
//                    for (int k = 0; k < board[i].length; k++){
//                        if (k != j && board[i][k] != 0){
//                            int[] targetPos2 = searchInTarget(target, board[i][k]);
//                            if (targetPos2[0] == i && targetPos[1] < targetPos2[1] && j < k){
//                                conflicts++;
//                            }
//                        }
//                    }
//                }
//
//                if (targetPos[1] == j){
//                    for (int k = 0; k < board.length; k++){
//                        if (k != i && board[k][j] != 0){
//                            int[] targetPos2 = searchInTarget(target, board[k][j]);
//                            if (targetPos2[1] == j && targetPos[0] > targetPos2[0] && i < k){
//                                conflicts++;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return calcManhattan(v.getVal()) + 2*conflicts;
//    }

    // NOT IN USE
    // Function search in target board and find coordinates of element in its target position.
    // Returns the coordinates in a integer array.
    // Used in heuristic function implemented using an integer array.
//    public static int[] searchInTarget(int[][] targetBoard, int c){
//        int[] result = new int[2];
//        for (int i = 0; i < targetBoard.length; i++){
//            for (int j = 0; j < targetBoard.length; j++){
//                if (targetBoard[i][j] == c){
//                    result[0] = i;
//                    result[1] = j;
//                }
//            }
//        }
//        return result;
//    }

//    // ~~~~~~~ Print functions implemented to check if board is being solved ~~~~~~~
//    public static void printBoard(int[][] board){
//        for(int i = 0; i < SIZE; i++){
//            for(int j = 0; j < SIZE; j++){
//                System.out.print(board[i][j]+" ");
//            }
//            System.out.println("");
//        }
//    }
//
//    public static <T> void printPathRec(Vertex target) {
//        if(target.getParent()== null) {
//            printBoard(target.getVal());
//        }
//        else {
//            printPathRec(target.getParent());
//            System.out.println("->");
//            printBoard(target.getVal());
//        }
//    }
//
//    public static<T> void printPath(Vertex target) {
//        printPathRec(target);
//        System.out.println();
//    }

}
