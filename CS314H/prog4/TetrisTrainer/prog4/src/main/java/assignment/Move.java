package assignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Move {

    public static Board.Result executeShift(TetrisBoard board, int xShift, int yShift) {
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        Point currentPos = board.getLowerLeftPos();
        int newX = currentPos.x + xShift;
        int newY = currentPos.y + yShift;
        if (board.isValidPosition(currPiece, newX, newY)) {
            board.setLowerLeftPos(new Point(newX, newY));
            return Board.Result.SUCCESS;
        }
        return Board.Result.OUT_BOUNDS;
    }

    public static Board.Result executeLeft(TetrisBoard board) {
        return executeShift(board, -1, 0);
    }

    public static Board.Result executeRight(TetrisBoard board) {
        return executeShift(board, 1, 0);
    }

    public static Board.Result executeDown(TetrisBoard board) {
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        if (executeShift(board, 0, -1).equals(Board.Result.SUCCESS)) {
            return Board.Result.SUCCESS;
        }

        Point currentPos = board.getLowerLeftPos();
        Point[] body = currPiece.getBody();
        ArrayList<BoardRow> grid = board.getGrid();
        for (Point p : body) {
            int currY = currentPos.y + p.y, currX = currentPos.x + p.x;
            grid.get(currY).setEntry(currX, currPiece.getType());
        }
        int rowsCleared = 0;
        for (int i = board.getHeight() - 1; i >= 0; i--) {
            if (grid.get(i).isFilled()) {
                grid.remove(i);
                grid.add(new BoardRow(board.getWidth()));
                rowsCleared++;
            }
        }
        int[] colHeight = board.getColHeight();
        int maxHeight = 0;
        for (int i = 0; i < board.getWidth(); i++) {
            colHeight[i] = 0;
            for (int j = board.getHeight() - 1; j >= 0; j--) {
                if (grid.get(j).getEntry(i) != null) {
                    colHeight[i] = j + 1;
                    break;
                }
            }
            maxHeight = Math.max(maxHeight, colHeight[i]);
        }
        board.setMaxHeight(maxHeight);
        board.setRowsCleared(rowsCleared);
        board.setCurrentPiece(null);
        board.setLowerLeftPos(null);
        return Board.Result.PLACE;
    }

    public static Board.Result executeDrop(TetrisBoard board) {
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;
        Board.Result result = executeDown(board);
        while (result == Board.Result.SUCCESS) {
            result = executeDown(board);
        }
        return result;
    }

    public static Board.Result executeTurn(TetrisBoard board, boolean isClockwiseTurn) {
        Piece currPiece = board.getCurrentPiece();
        if (currPiece == null) return Board.Result.NO_PIECE;

        Point[] kicks = null;

        switch(currPiece.getType()){
            case SQUARE:
                kicks = new Point[1];
                kicks[0] = new Point(0, 0);
                break;
            case STICK:
                if (isClockwiseTurn) kicks = Piece.I_CLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                else kicks = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                break;
            case T:
            case LEFT_L:
            case RIGHT_L:
            case LEFT_DOG:
            case RIGHT_DOG:
                if (isClockwiseTurn) kicks = Piece.NORMAL_CLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                else kicks = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currPiece.getRotationIndex()];
                break;
        }

        Point currentPos = board.getLowerLeftPos();

        Piece newPiece = null;
        if (isClockwiseTurn) newPiece = currPiece.clockwisePiece();
        else newPiece = currPiece.counterclockwisePiece();

        for (Point p : kicks) {
            int newX = currentPos.x + p.x;
            int newY = currentPos.y + p.y;

            if (board.isValidPosition(newPiece, newX, newY)) {
                board.setCurrentPiece(newPiece);
                board.setLowerLeftPos(new Point(newX, newY));
                return Board.Result.SUCCESS;
            }
        }

        return Board.Result.OUT_BOUNDS;
    }

    public static Board.Result executeClockwise(TetrisBoard board) {
        return executeTurn(board, true);
    }

    public static Board.Result executeCounterclockwise(TetrisBoard board) {
        return executeTurn(board, false);
    }

    public static Board.Result executeNothing(TetrisBoard board) {
        return Board.Result.SUCCESS;
    }

    public static Board.Result executeHold(TetrisBoard board) {

        if (board.getCurrentPiece() == null) return Board.Result.NO_PIECE;

        Piece tempPiece = board.getCurrentPiece();
        board.setCurrentPiece(board.getHeldPiece());
        board.setHeldPiece(tempPiece);

        if (board.getCurrentPiece() == null) return Board.Result.NO_PIECE;

        board.setLowerLeftPos(new Point(board.getWidth() / 2 - board.getCurrentPiece().getWidth() / 2, 20));

        return Board.Result.SUCCESS;

    }

}