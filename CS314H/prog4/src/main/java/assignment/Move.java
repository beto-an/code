package assignment;

import java.awt.*;
import java.util.ArrayList;


//Helper class for processing all the move actions that TetrisBoard can receive
public class Move {


    //Helped function that attempts to shift the current piece by a certain dx, dy
    public static Board.Result executeShift(TetrisBoard board, int xShift, int yShift) {

        //Check that a piece exists
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        //Figure out the new position of the piece
        Point currentPos = board.getLowerLeftPos();
        int newX = currentPos.x + xShift;
        int newY = currentPos.y + yShift;

        //Check if the new position is valid or not
        if (board.isValidPosition(currPiece, newX, newY)) {
            board.setLowerLeftPos(new Point(newX, newY));
            return Board.Result.SUCCESS;
        }

        //If above fails, then the move is not valid
        return Board.Result.OUT_BOUNDS;

    }


    //Action.LEFT: shift piece by 1 column to the left
    public static Board.Result executeLeft(TetrisBoard board) {
        return executeShift(board, -1, 0);
    }


    //Action.RIGHT: shift piece by 1 column to the right
    public static Board.Result executeRight(TetrisBoard board) {
        return executeShift(board, 1, 0);
    }


    //Action.DOWN: shift piece by 1 row down, placing piece if it intersects with existing board pieces/boundary
    public static Board.Result executeDown(TetrisBoard board) {

        //Check if a current piece exists
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        //Try to move piece down; if successful, then we're done
        if (executeShift(board, 0, -1).equals(Board.Result.SUCCESS)) {
            return Board.Result.SUCCESS;
        }

        //If moving down wasn't successful, then place the current piece
        Point currentPos = board.getLowerLeftPos();
        Point[] body = currPiece.getBody();
        ArrayList<BoardRow> grid = board.getGrid();

        //Update the grid entries to reflect placing the current piece
        for (Point p : body) {
            int currY = currentPos.y + p.y, currX = currentPos.x + p.x;
            grid.get(currY).setEntry(currX, currPiece.getType());
        }

        //Clear any rows that now become full
        int rowsCleared = 0;
        for (int i = board.getHeight() - 1; i >= 0; i--) {

            //If a row is filled, remove it and add an empty row at the end
            if (grid.get(i).isFilled()) {
                grid.remove(i);
                grid.add(new BoardRow(board.getWidth()));
                rowsCleared++;
            }

        }

        //Update the height of each column, calculate the new max column height
        int[] colHeight = board.getColHeight();
        int maxHeight = 0;
        for (int i = 0; i < board.getWidth(); i++) {

            colHeight[i] = 0;

            //Check for the highest filled location in the current column
            for (int j = board.getHeight() - 1; j >= 0; j--) {

                //If a piece exists at this location, update the column height
                if (grid.get(j).getEntry(i) != null) {
                    colHeight[i] = j + 1;
                    break;
                }

            }

            //Update the max column height
            maxHeight = Math.max(maxHeight, colHeight[i]);

        }

        //Update the board's stored values, and reset the piece and location
        board.setMaxHeight(maxHeight);
        board.setRowsCleared(rowsCleared);
        board.setCurrentPiece(null);
        board.setLowerLeftPos(null);
        return Board.Result.PLACE;

    }


    //Action.DROP: move the piece all the way down until it gets placed
    public static Board.Result executeDrop(TetrisBoard board) {

        //Check if a current piece exists
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        //Keep moving the piece down until it gets placed eventually
        Board.Result result = executeDown(board);
        while (result == Board.Result.SUCCESS) {
            result = executeDown(board);
        }
        return result;

    }


    //Helper function that attempts to turn the piece either 90 degrees clockwise or counterclockwise
    public static Board.Result executeTurn(TetrisBoard board, boolean isClockwiseTurn) {

        //Check to see a current piece exists
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        //Determine what set of kick values to use based on the current PieceType and rotation index
        Point[] kicks = null;
        switch(currPiece.getType()){

            //A square does not kick, so the only rotation value to process is its own location
            case SQUARE:
                kicks = new Point[1];
                kicks[0] = new Point(0, 0);
                break;

            //Use the stick's values for kicking, choosing the correct clockwise or counterclockwise set
            case STICK:
                if (isClockwiseTurn) kicks = Piece.I_CLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                else kicks = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                break;

            //Use the default kick values, choosing the correct clockwise or counterclockwise set
            case T:
            case LEFT_L:
            case RIGHT_L:
            case LEFT_DOG:
            case RIGHT_DOG:
                if (isClockwiseTurn) kicks = Piece.NORMAL_CLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                else kicks = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                break;

        }

        //Get the rotated piece
        Point currentPos = board.getLowerLeftPos();
        Piece newPiece = null;
        if (isClockwiseTurn) newPiece = currPiece.clockwisePiece();
        else newPiece = currPiece.counterclockwisePiece();

        //Check the kicks sequentially to see first location that works for the rotated piece
        for (Point p : kicks) {

            //Calculate new location of the kicked piece
            int newX = currentPos.x + p.x;
            int newY = currentPos.y + p.y;

            //Check to see if this position is valid; if so, update the current piece and location
            if (board.isValidPosition(newPiece, newX, newY)) {
                board.setCurrentPiece(newPiece);
                board.setLowerLeftPos(new Point(newX, newY));
                return Board.Result.SUCCESS;
            }

        }

        //If all above kick checks fail, then there is no possible rotation
        return Board.Result.OUT_BOUNDS;

    }


    //Action.CLOCKWISE: Try to rotate the piece 90 degrees clockwise
    public static Board.Result executeClockwise(TetrisBoard board) {
        return executeTurn(board, true);
    }


    //Action.COUNTERCLOCKWISE: Try to rotate the piece 90 degrees counterclockwise
    public static Board.Result executeCounterclockwise(TetrisBoard board) {
        return executeTurn(board, false);
    }


    //Action.NOTHING: Do nothing
    public static Board.Result executeNothing(TetrisBoard board) {
        return Board.Result.SUCCESS;
    }


    //Action.HOLD: Put the current piece on hold, and swap out the held piece if one exists
    public static Board.Result executeHold(TetrisBoard board) {

        //Check if there is a current piece
        if (board.getCurrentPiece() == null) return Board.Result.NO_PIECE;

        //Swap the held piece with the current piece
        Piece tempPiece = board.getCurrentPiece();
        board.setCurrentPiece(board.getHeldPiece());
        board.setHeldPiece(tempPiece);

        //Check if we swapped in an actual piece; if not, return no piece
        if (board.getCurrentPiece() == null) return Board.Result.NO_PIECE;

        //If there is an actual piece, reset the location
        board.setLowerLeftPos(new Point(board.getWidth() / 2 - board.getCurrentPiece().getWidth() / 2, 20));

        //Action did not break beforehand, executed successfully
        return Board.Result.SUCCESS;

    }

}