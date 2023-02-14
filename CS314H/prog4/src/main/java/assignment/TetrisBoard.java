package assignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {


    //Store a variety of instance variables that contain information about the state of the baord
    private ArrayList<BoardRow> grid;
    private Piece currentPiece;
    private Point lowerLeftPos;
    private Result lastResult;
    private Action lastAction;
    private int rowsCleared;
    private int width;
    private int height;
    private int maxHeight;

    //Array storing the height of every column
    private int[] colHeight;

    //Queue storing the actions taken to get from the previous board to the current one (Only used for brain)
    private Queue<Action> actionsTaken;

    //Piece variable storing the currently held piece
    private Piece heldPiece;

    //Array that stores all seven TetrisPiece PieceTypes
    public static final Piece[] pieces = {
            new TetrisPiece(Piece.PieceType.T),
            new TetrisPiece(Piece.PieceType.SQUARE),
            new TetrisPiece(Piece.PieceType.STICK),
            new TetrisPiece(Piece.PieceType.LEFT_L),
            new TetrisPiece(Piece.PieceType.RIGHT_L),
            new TetrisPiece(Piece.PieceType.LEFT_DOG),
            new TetrisPiece(Piece.PieceType.RIGHT_DOG)
    };

    //Line separator to make toString return a more nicely formatted String to print out
    private static String separator = "\n-------------------------------\n";


    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {

        //Set the width and height
        this.width = width;
        this.height = height;

        //Initialize the grid with empty rows
        grid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            if (width < 0) grid.add(null);
            grid.add(new BoardRow(width));
        }

        //Initialize instance variables
        currentPiece = null;
        lowerLeftPos = null;
        lastResult = null;
        lastAction = null;
        rowsCleared = 0;
        maxHeight = 0;
        heldPiece = null;

        //Deal with improperly sized boards
        if (width < 0) colHeight = null;
        else colHeight = new int[width];
        if (colHeight != null) Arrays.fill(colHeight, 0);

    }


    //Perform the given Action and change the board state
    @Override
    public Result move(Action act) {

        //If no action, do nothing
        if (act == null) return null;

        //Reset the rows cleared and the last action
        rowsCleared = 0;
        lastAction = act;

        //Based on what action is given, call the corresponding Move function
        switch(act) {
            case LEFT:
                lastResult = Move.executeLeft(this);
                break;
            case RIGHT:
                lastResult = Move.executeRight(this);
                break;
            case DOWN:
                lastResult = Move.executeDown(this);
                break;
            case DROP:
                lastResult = Move.executeDrop(this);
                break;
            case CLOCKWISE:
                lastResult = Move.executeClockwise(this);
                break;
            case COUNTERCLOCKWISE:
                lastResult = Move.executeCounterclockwise(this);
                break;
            case NOTHING:
                lastResult = Move.executeNothing(this);
                break;
            case HOLD:
                lastResult = Move.executeHold(this);
                break;
        }

        return lastResult;

    }


    //Create a copy of the current board and perform a given action on the copy
    @Override
    public Board testMove(Action act) {
        Board clone = clone();
        clone.move(act);
        return clone;
    }


    //Return a deep copy of the current board
    public TetrisBoard clone() {

        //Create a new board and create a deep copy of the grid
        TetrisBoard clone = new TetrisBoard(getWidth(), getHeight());
        ArrayList<BoardRow> cloneList = new ArrayList<>();
        for (BoardRow row : getGrid()) {
            cloneList.add(row.clone());
        }
        clone.setGrid(cloneList);

        //Copy over all the instance variables
        clone.setCurrentPiece(getCurrentPiece());
        clone.setLowerLeftPos(getLowerLeftPos());
        clone.setLastResult(getLastResult());
        clone.setLastAction(getLastAction());
        clone.setRowsCleared(getRowsCleared());
        clone.setMaxHeight(getMaxHeight());
        clone.setHeldPiece(getHeldPiece());

        //Copy over the column heights, taking care if the array is null (due to invalid input)
        if (getColHeight() == null) clone.setColHeight(null);
        else clone.setColHeight(Arrays.copyOf(getColHeight(), getColHeight().length));

        return clone;

    }


    //Return a list of all possible states the board could end up in after placing the current piece
    public ArrayList<TetrisBoard> generateAllStates() {

        ArrayList<TetrisBoard> possibleBoards = new ArrayList<>();

        if (currentPiece == null) {

            //If the piece is null, we can't place a piece, so just return a clone of the same board
            possibleBoards.add(clone());

        } else {

            //Loop through both the current piece and the held piece
            for (int e = 0; e < 2; e++) {

                //Choose to use either the current piece or the held piece
                Piece currPiece = null;
                if (e == 0) currPiece = currentPiece;
                else {

                    //If there is no held piece, then break
                    if (heldPiece == null) break;
                    currPiece = heldPiece;

                }

                //Loop through all possible starting column locations for piece
                for (int i = -4; i <= width + 3; i++) {

                    //Loop through all possible orientations for the piece
                    for (int j = 0; j < 4; j++) {

                        //Get the next rotation of the piece
                        currPiece = currPiece.clockwisePiece();

                        //Check to see if the current piece + position is a valid location
                        if (isValidPosition(currPiece, i, height - 4)) {

                            //If valid, then create a hypothetical board resulting from dropping this piece at this location
                            TetrisBoard clone = clone();

                            //If we used the held piece, then update the held piece
                            if (e == 1) clone.setHeldPiece(currentPiece);

                            //Update current piece and location, then drop
                            clone.setCurrentPiece(currPiece);
                            clone.setLowerLeftPos(new Point(i, height - 4));
                            clone.move(Action.DROP);

                            if (clone.getLastResult() == Result.PLACE) {

                                //If the drop was successful, set a random next piece + corresponding location (helpful for karma brain)
                                clone.setCurrentPiece(getRandomPiece());
                                clone.setLowerLeftPos(new Point(getWidth() / 2 - getCurrentPiece().getWidth() / 2, height - 4));

                                //Create a queue of actions to take to get from current board to this new board
                                Queue<Action> actionsTaken = new LinkedList<>();
                                if (e == 1) actionsTaken.add(Action.HOLD);

                                //Add rotation moves
                                for (int k = 0; k < (j + 1) % 4; k++) {
                                    actionsTaken.add(Action.CLOCKWISE);
                                }

                                //Add left/right moves
                                int initX = width / 2 - currPiece.getWidth() / 2;
                                for (int k = 0; k < Math.abs(initX - i); k++) {
                                    if (i < initX) actionsTaken.add(Action.LEFT);
                                    if (i > initX) actionsTaken.add(Action.RIGHT);
                                }

                                actionsTaken.add(Action.DROP);
                                clone.setActionsTaken(actionsTaken);

                                //Add new board to possible resulting states
                                possibleBoards.add(clone);

                            }
                        }
                    }
                }

            }

        }

        return possibleBoards;

    }

    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
        return lowerLeftPos;
    }


    //Update the next piece and position
    @Override
    public void nextPiece(Piece p, Point spawnPosition) {

        //Check to see if the piece or position are null, in which case do nothing
        if (p == null || spawnPosition == null) return;

        //If location is valid, then update state; otherwise, throw an IllegalArgumentException
        if (isValidPosition(p, spawnPosition.x, spawnPosition.y)) {
            currentPiece = p;
            lowerLeftPos = spawnPosition;
        } else {
            throw new IllegalArgumentException("Invalid position for the new piece!");
        }

    }


    //Return the current board represented as a String, helpful for visualizing our karma brain
    public String toString() {

        //Turn each row of the grid into a String, and add them in reverse order
        StringBuilder res = new StringBuilder(separator);
        for (int i = height - 1; i >= 0; i--) {
            String rowString = grid.get(i).toString();
            res.append(rowString);
            res.append(separator);
        }

        return res.toString();

    }


    //An equality check that doesn't check if the current pieces are equal
    public boolean noPieceEquals(TetrisBoard otherBoard) {

        //Check to make sure dimensions are the same
        if (this.getWidth() != otherBoard.getWidth()) return false;
        if (this.getHeight() != otherBoard.getHeight()) return false;

        //Check to make sure each row is exactly the same
        for (int i = 0; i < grid.size(); i++) {
            if (!grid.get(i).equals(otherBoard.grid.get(i))) return false;
        }

        return true;

    }


    //A semantic equality check that makes sure the grids are equal and the current piece + location are equal
    @Override
    public boolean equals(Object other) {

        //Ensure the other object is a TetrisBoard
        if(!(other instanceof TetrisBoard)) return false;
        TetrisBoard otherBoard = (TetrisBoard) other;

        //Check for equality in the grid
        if (!noPieceEquals(otherBoard)) return false;

        //Check for equality of current piece and position
        if (this.getCurrentPiece() != otherBoard.getCurrentPiece()) return false;
        if (this.getLowerLeftPos() != otherBoard.getLowerLeftPos()) return false;

        return true;

    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }


    //Given a piece, figure out the row at which it comes to rest
    @Override
    public int dropHeight(Piece piece, int x) {

        //By default, return highest location for an invalid piece
        if (piece == null) return height;

        //Scan from lowest to highest row to figure out the lowest valid row
        for (int i = height - 4; i >= -2; i--) {
            if (isValidPosition(piece, x, i)) continue;
            return i + 1;
        }
        return -2;

    }


    //Helper method that checks, for a given piece and position, if it is valid (i.e. within grid, non-intersecting with grid pieces)
    public boolean isValidPosition(Piece piece, int x, int y) {

        //Null pieces are never valid
        if (piece == null) return false;

        //Check for each filled square of the piece body, if it is within the grid and in an empty square
        for (Point p : piece.getBody()) {

            int currY = y + p.y, currX = x + p.x;
            if (currX < 0 || currX >= width || currY < 0 || currY >= height) return false;
            if (getGrid(currX, currY) != null) return false;

        }

        return true;

    }

    @Override
    public int getColumnHeight(int x) {
        if (colHeight == null) return 0;
        if (x < 0 || x >= colHeight.length) return 0;
        return colHeight[x];
    }

    @Override
    public int getRowWidth(int y) {
        if (y < 0 || y >= grid.size()) return 0;
        BoardRow row = grid.get(y);
        if (row == null) return 0;
        return row.getNumFilled();
    }


    //Get the PieceType at a certain location, check to make sure it is a valid position first
    @Override
    public Piece.PieceType getGrid(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        BoardRow row = grid.get(y);
        if (row == null) return null;
        return row.getEntry(x);
    }


    //Helper method for filling in an entry, useful for testing
    public void fillEntry(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        BoardRow row = grid.get(y);
        if (row == null) return;
        row.setEntry(x, Piece.PieceType.T);
    }


    //Helper method for emptying an entry, useful for testing
    public void emptyEntry(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        BoardRow row = grid.get(y);
        if (row == null) return;
        row.setEntry(x, null);
    }


    //Helper method for deleting the majority of the data inside the TetrisBoard
    public void cleanup() {
        for (BoardRow row : grid) {
            row.cleanup();
        }
        grid = null;
        colHeight = null;
    }


    //Get a random TetrisPiece, used for karma brain
    public static Piece getRandomPiece() {
        return pieces[(int) (Math.random() * pieces.length)];
    }


    //Overloaded version of getGrid, returns the entire grid of pieces
    public ArrayList<BoardRow> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<BoardRow> list) {
        grid = list;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    public Point getLowerLeftPos() {
        return lowerLeftPos;
    }

    public void setLowerLeftPos(Point lowerLeftPos) {
        this.lowerLeftPos = lowerLeftPos;
    }

    public void setRowsCleared(int rowsCleared) {
        this.rowsCleared = rowsCleared;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int[] getColHeight() {
        return colHeight;
    }

    public void setLastResult(Result lastResult) {
        this.lastResult = lastResult;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    public void setColHeight(int[] colHeight) {
        this.colHeight = colHeight;
    }

    public Queue<Action> getActionsTaken() {
        return actionsTaken;
    }

    public void setActionsTaken(Queue<Action> actionsTaken) {
        this.actionsTaken = actionsTaken;
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    public void setHeldPiece(Piece heldPiece) {
        this.heldPiece = heldPiece;
    }


}
