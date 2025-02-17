package fifteenpuzzle;

import java.awt.*;

public class Vertex {

    int[][] board;
    private Vertex parent;
    private int h;
    private int g;
    private int f;
    private int Xpos;
    private int Ypos;
    private int hash;
    private final Point emptyPosition;


    // Constructor
    // Takes in a 2D integer array and coordinates of empty position on board
    public Vertex(int[][] board, int x, int y){
        this.board = board;
        this.emptyPosition = new Point(0,0);
        g = 0;
        h = 0;
        f = 0;
        this.Xpos = x;
        this.Ypos = y;
        hash = hashCode();

        // Finding empty position on board and setting its x and y coordinates
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] == 0){
                    emptyPosition.x = j;
                    emptyPosition.y = i;
                } else {
                    this.board[i][j] = board[i][j];
                }
            }
        }
    }

    // Returns empty position on board
    public Point getEmptyPosition(){
        return emptyPosition;
    }

    // Returns data/value of vertex, i.e. the board
    public int[][] getVal(){
        return board;
    }

    // Setter for the value of the cost to reach a vertex from the starting vertex
    public void setG(int g){
        this.g = g;
    }

    // Getter for the value of the cost to reach a vertex from the starting vertex
    public int getG(){
        return g;
    }

    // Setter for the value to reach the target vertex from the current vertex
    public void setH(int h){
        this.h = h;
        this.f = this.g + this.h;
    }

    // NOT IN USE
    // Getter for the value to reach the target vertex from the current vertex
    public int getH(){
        return h;
    }

    // Getter for the estimate cost of the shortest path to target
    // Used to load vertices into open list implemented using a priority queue
    public int getF(){
        return f;
    }

    // NOT IN USE
    // Setter for the estimate cost of the shortest path to target
    public void setF(int f){
        this.f = f;
    }

    // Getter to return x coordinate of empty position
    public int getXpos(){
        return Xpos;
    }

    // Setter for x coordinate of empty position
    public void setXpos(int x){
        this.Xpos = x;
    }

    // Getter to return y coordinate of empty position
    public int getYpos(){
        return Ypos;
    }

    // Setter for y coordinate of empty position
    public void setYpos(int y){
        this.Ypos = y;
    }

    // Setter for parent of vertex
    public void setParent(Vertex parent){
        this.parent = parent;
    }

    // Getter to return parent of vertex
    public Vertex getParent(){
        return parent;
    }

    // Compares board with the solution board, index by index
    public boolean equalsSols(int[][] solution){

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] != solution[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    // Function to compare object class and hash code for precise comparison
    @Override
    public boolean equals(Object other){
        if (this.getClass() != other.getClass() && this.hashCode() != other.hashCode()){
            return false;
        }

        int[][] otherName = ((Vertex) other).getVal();
        return equalsSols(otherName);
    }

    // Function to get hash code of a vertex
    // Used for comparison
    @Override
    public int hashCode(){
        int prime =  1610612741;

        // Boards of size 3 - use prime 2
        if (board.length == 3) {
            for(int i = 0; i < board.length; i++) {
                prime = board[i][2] +  prime;
            }
        }

        // Boards of size 4 and 5 - use prime 3
        if (board.length == 4 || board.length == 5) {
            for(int i = 0; i < board.length; i++) {
                prime = board[i][3] +  prime;
            }
        }

        // Boards of size 6 - use prime 5 and add prime/2
        if (board.length == 6){
            for (int i = 0; i < board.length; i++){
                prime = board[i][5] + prime/2;
            }
        }

        // Boards of size 7 - use prime 5
        if (board.length == 7){
            for (int i = 0; i < board.length; i++){
                prime = board[i][5] - prime;
            }
        }

        // Boards of size 8 and 9 - use prime 7
        if (board.length == 8 || board.length == 9) {
            for(int i = 0; i < board.length; i++) {
                prime = board[i][7] +  prime;
            }
        }

        return prime;
    }


}
