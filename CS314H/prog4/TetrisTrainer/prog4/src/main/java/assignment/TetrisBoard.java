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

    ArrayList<BoardRow> grid;
    Piece currentPiece;
    Point lowerLeftPos;
    Result lastResult;
    Action lastAction;
    int rowsCleared;
    int width;
    int height;
    int maxHeight;
    int[] colHeight;
    Queue<Action> actionsTaken;
    Piece heldPiece;

    static Piece[] pieces = {
            new TetrisPiece(Piece.PieceType.T),
            new TetrisPiece(Piece.PieceType.SQUARE),
            new TetrisPiece(Piece.PieceType.STICK),
            new TetrisPiece(Piece.PieceType.LEFT_L),
            new TetrisPiece(Piece.PieceType.RIGHT_L),
            new TetrisPiece(Piece.PieceType.LEFT_DOG),
            new TetrisPiece(Piece.PieceType.RIGHT_DOG)
    };

    static String separator = "\n-------------------------------\n";

    public static Piece getRandomPiece() {
        return pieces[(int) (Math.random() * pieces.length)];
    }

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

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {

        this.width = width;
        this.height = height;

        grid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            grid.add(new BoardRow(width));
        }

        currentPiece = null;
        lowerLeftPos = null;
        lastResult = null;
        lastAction = null;
        rowsCleared = 0;
        maxHeight = 0;
        heldPiece = null;

        if (width < 0) colHeight = null;
        else colHeight = new int[width];
        if (colHeight != null) Arrays.fill(colHeight, 0);

    }

    @Override
    public Result move(Action act) {

        if (act == null) return null;

        rowsCleared = 0;
        lastAction = act;

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

    @Override
    public Board testMove(Action act) {
        Board clone = clone();
        clone.move(act);
        return clone;
    }

    public TetrisBoard clone() {
        TetrisBoard clone = new TetrisBoard(getWidth(), getHeight());
        ArrayList<BoardRow> cloneList = new ArrayList<>();
        for (BoardRow row : getGrid()) {
            cloneList.add(row.clone());
        }
        clone.setGrid(cloneList);
        clone.setCurrentPiece(getCurrentPiece());
        clone.setLowerLeftPos(getLowerLeftPos());
        clone.setLastResult(getLastResult());
        clone.setLastAction(getLastAction());
        clone.setRowsCleared(getRowsCleared());
        clone.setMaxHeight(getMaxHeight());
        clone.setHeldPiece(getHeldPiece());
        if (getColHeight() == null) clone.setColHeight(null);
        else clone.setColHeight(Arrays.copyOf(getColHeight(), getColHeight().length));
        return clone;
    }

    public ArrayList<TetrisBoard> generateAllStates() {

        ArrayList<TetrisBoard> possibleBoards = new ArrayList<>();

        if (currentPiece == null) {
            possibleBoards.add(clone());
        } else {

            for (int e = 0; e < 2; e++) {
                Piece currPiece = null;
                if (e == 0) currPiece = currentPiece;
                else {
                    if (heldPiece == null) break;
                    currPiece = heldPiece;
                }

                for (int i = -4; i <= width + 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        currPiece = currPiece.clockwisePiece();
                        if (isValidPosition(currPiece, i, height - 4)) {
                            TetrisBoard clone = clone();
                            if (e == 1) clone.setHeldPiece(currentPiece);
                            clone.setCurrentPiece(currPiece);
                            clone.setLowerLeftPos(new Point(i, height - 4));
                            clone.move(Action.DROP);
                            if (clone.getLastResult() == Result.PLACE) {
                                clone.setCurrentPiece(getRandomPiece());
                                clone.setLowerLeftPos(new Point(getWidth() / 2 - getCurrentPiece().getWidth() / 2, 20));

                                Queue<Action> actionsTaken = new LinkedList<>();
                                if (e == 1) actionsTaken.add(Action.HOLD);
                                for (int k = 0; k < (j + 1) % 4; k++) {
                                    actionsTaken.add(Action.CLOCKWISE);
                                }
                                int initX = width / 2 - currPiece.getWidth() / 2;
                                for (int k = 0; k < Math.abs(initX - i); k++) {
                                    if (i < initX) actionsTaken.add(Action.LEFT);
                                    if (i > initX) actionsTaken.add(Action.RIGHT);
                                }
                                actionsTaken.add(Action.DROP);
                                clone.setActionsTaken(actionsTaken);

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

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        if (p == null || spawnPosition == null) return;
        if (isValidPosition(p, spawnPosition.x, spawnPosition.y)) {
            currentPiece = p;
            lowerLeftPos = spawnPosition;
        } else {
            throw new IllegalArgumentException("Invalid position for the new piece!");
        }
    }

    public String toString() {
        StringBuilder res = new StringBuilder(separator);
        for (int i = height - 1; i >= 0; i--) {
            String rowString = grid.get(i).toString();
            res.append(rowString);
            res.append(separator);
        }
        return res.toString();
    }

    public boolean noPieceEquals(TetrisBoard otherBoard) {

        if (this.getWidth() != otherBoard.getWidth()) return false;
        if (this.getHeight() != otherBoard.getHeight()) return false;

        for (int i = 0; i < grid.size(); i++) {
            if (!grid.get(i).equals(otherBoard.grid.get(i))) return false;
        }

        return true;

    }

    @Override
    public boolean equals(Object other) {

        if(!(other instanceof TetrisBoard)) return false;
        TetrisBoard otherBoard = (TetrisBoard) other;

        if (!noPieceEquals(otherBoard)) return false;

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

    @Override
    public int dropHeight(Piece piece, int x) {
        if (piece == null) return height;
        for (int i = -2; i < height; i++) {
            if (isValidPosition(piece, x, i)) return i;
        }
        return height;
    }

    public boolean isValidPosition(Piece piece, int x, int y) {
        if (piece == null) return false;
        for (Point p : piece.getBody()) {
            int currY = y + p.y, currX = x + p.x;
            if (currX < 0 || currX >= width || currY < 0 || currY >= height) return false;
            if (getGrid(currX, currY) != null) return false;
        }
        return true;
    }

    @Override
    public int getColumnHeight(int x) {
        return colHeight[x];
    }

    @Override
    public int getRowWidth(int y) {
        return grid.get(y).getNumFilled();
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        return grid.get(y).getEntry(x);
    }

    public void cleanup() {
        for (BoardRow row : grid) {
            row.cleanup();
        }
        grid = null;
        colHeight = null;
    }

}
