package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


//Test the TetrisBoard class
class TetrisBoardTest {


    //Test that the equals method correctly tests semantic equality
    @Test
    void testEquals() {

        TetrisBoard tb = filledBoard(5, 1, 20);
        TetrisBoard tb2 = new TetrisBoard(5, 20);
        assertNotEquals(tb, tb2);

        tb2.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        assertNotEquals(tb, tb2);

        tb2.setLowerLeftPos(new Point(0, 16));
        assertNotEquals(tb, tb2);

        tb2.move(Board.Action.DROP);
        assertEquals(tb, tb2);

        //Test for invalid inputs
        assertFalse(tb.equals("hello"));
        assertFalse(tb.equals(new TetrisPiece(Piece.PieceType.T)));
        assertFalse(tb.equals(null));

    }


    //Helper method that creates a row which is almost full
    private static BoardRow almostFullRow(int width) {
        BoardRow br = new BoardRow(width);
        for (int i = 0; i < width - 1; i++) {
            br.setEntry(i, Piece.PieceType.STICK);
        }
        return br;
    }


    //Helper method that creates a board with a certain number of almost full rows
    private static TetrisBoard filledBoard(int width, int fillHeight, int height) {

        //Create almost full rows
        ArrayList<BoardRow> abr = new ArrayList<>();
        for (int i = 0; i < fillHeight; i++) {
            abr.add(almostFullRow(width));
        }

        //Let the rest of the rows be empty
        for (int i = fillHeight; i < height; i++) {
            abr.add(new BoardRow(width));
        }

        TetrisBoard tb = new TetrisBoard(width, height);
        tb.setGrid(abr);
        return tb;

    }


    //Test that the dropHeight method returns the correct row value
    @Test
    void dropHeight() {

        TetrisBoard tb = new TetrisBoard(10, 24);
        TetrisPiece r = new TetrisPiece(Piece.PieceType.STICK);
        assertEquals(-2, tb.dropHeight(r, 0));
        assertEquals(0, tb.dropHeight(r.clockwisePiece(), -2));

        TetrisPiece p = new TetrisPiece(PieceDictionaryItem.LEFT_L_EAST);
        tb.setCurrentPiece(p);
        tb.setLowerLeftPos( new Point(0, 17));
        Move.executeDrop(tb);

        TetrisPiece q = new TetrisPiece(Piece.PieceType.LEFT_L);
        assertEquals(3, tb.dropHeight(q.clockwisePiece(), 0));


        TetrisPiece s = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        assertEquals(2, tb.dropHeight(s, 0));

    }


    //Test that the isValidPosition method correctly determins position validity
    @Test
    void isValidPosition() {

        //Set up the grid
        TetrisBoard tb = new TetrisBoard(5, 20);
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);

        //Test a variety of valid and invalid locations
        Piece Stick = new TetrisPiece(Piece.PieceType.STICK);
        assertFalse(tb.isValidPosition(Stick, 0, -2));
        assertFalse(tb.isValidPosition(Stick, 0, -1));
        assertFalse(tb.isValidPosition(Stick, 0,  0));
        assertFalse(tb.isValidPosition(Stick, 0, 1));
        assertTrue(tb.isValidPosition(Stick, 0, 2));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);

        assertFalse(tb.isValidPosition(Stick, 0, 2));

        Piece L = new TetrisPiece(Piece.PieceType.RIGHT_L).counterclockwisePiece();
        assertFalse(tb.isValidPosition(L, 0, -70));
        assertFalse(tb.isValidPosition(L, 0, -3));
        assertFalse(tb.isValidPosition(L, 0, -2));
        assertFalse(tb.isValidPosition(L, 0,  -1));
        assertFalse(tb.isValidPosition(L, 0, 0));
        assertFalse(tb.isValidPosition(L, 0, 1));
        assertFalse(tb.isValidPosition(L, 0, 2));
        assertFalse(tb.isValidPosition(L, 0, 3));
        assertFalse(tb.isValidPosition(L, 0, 4));
        assertTrue(tb.isValidPosition(L, 0, 5));
        assertTrue(tb.isValidPosition(L, 0, 6));

    }


    //Test that the correct column height is returned
    @Test
    void getColumnHeight() {

        //Test zero column height
        TetrisBoard board = new TetrisBoard(10, 24);
        for (int i = 0; i < 10; i++) {
            assertEquals(0, board.getColumnHeight(i));
        }

        //Test placed pieces
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(1, board.getColumnHeight(0));
        assertEquals(2, board.getColumnHeight(1));
        assertEquals(1, board.getColumnHeight(2));
        for (int i = 3; i < 10; i++) {
            assertEquals(0, board.getColumnHeight(i));
        }

        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(3, board.getColumnHeight(0));
        assertEquals(3, board.getColumnHeight(1));
        assertEquals(3, board.getColumnHeight(2));
        assertEquals(3, board.getColumnHeight(3));
        for (int i = 4; i < 10; i++) {
            assertEquals(0, board.getColumnHeight(i));
        }

        //Test bad boards
        board = new TetrisBoard(1, 0);
        assertEquals(0, board.getColumnHeight(0));
        assertEquals(0, board.getColumnHeight(1));
        assertEquals(0, board.getColumnHeight(-12983));

        board = new TetrisBoard(0, 1);
        assertEquals(0, board.getColumnHeight(0));
        assertEquals(0, board.getColumnHeight(1));
        assertEquals(0, board.getColumnHeight(-12983));

        board = new TetrisBoard(0, 0);
        assertEquals(0, board.getColumnHeight(0));
        assertEquals(0, board.getColumnHeight(1));
        assertEquals(0, board.getColumnHeight(-12983));

        board = new TetrisBoard(-1928, 0);
        assertEquals(0, board.getColumnHeight(0));
        assertEquals(0, board.getColumnHeight(1));
        assertEquals(0, board.getColumnHeight(-12983));

        board = new TetrisBoard(-1293, -91823);
        assertEquals(0, board.getColumnHeight(0));
        assertEquals(0, board.getColumnHeight(1));
        assertEquals(0, board.getColumnHeight(-12983));

    }


    //Test that the correct row width is returned
    @Test
    void getRowWidth() {

        //Test zero row width
        TetrisBoard board = new TetrisBoard(10, 24);
        for (int i = 0; i < 24; i++) {
            assertEquals(0, board.getRowWidth(i));
        }

        //Test placed pieces
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(3, board.getRowWidth(0));
        assertEquals(1, board.getRowWidth(1));
        for (int i = 2; i < 24; i++) {
            assertEquals(0, board.getRowWidth(i));
        }

        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(3, board.getRowWidth(0));
        assertEquals(1, board.getRowWidth(1));
        assertEquals(4, board.getRowWidth(2));
        for (int i = 3; i < 24; i++) {
            assertEquals(0, board.getRowWidth(i));
        }

        //Test bad boards
        board = new TetrisBoard(1, 0);
        assertEquals(0, board.getRowWidth(0));
        assertEquals(0, board.getRowWidth(1));
        assertEquals(0, board.getRowWidth(-12983));

        board = new TetrisBoard(0, 1);
        assertEquals(0, board.getRowWidth(0));
        assertEquals(0, board.getRowWidth(1));
        assertEquals(0, board.getRowWidth(-12983));

        board = new TetrisBoard(0, 0);
        assertEquals(0, board.getRowWidth(0));
        assertEquals(0, board.getRowWidth(1));
        assertEquals(0, board.getRowWidth(-12983));

        board = new TetrisBoard(-1928, 0);
        assertEquals(0, board.getRowWidth(0));
        assertEquals(0, board.getRowWidth(1));
        assertEquals(0, board.getRowWidth(-12983));

        board = new TetrisBoard(-1293, -91823);
        assertEquals(0, board.getRowWidth(0));
        assertEquals(0, board.getRowWidth(1));
        assertEquals(0, board.getRowWidth(-12983));

    }


    //Test that the correct entry in the grid is returned
    @Test
    void getGrid() {

        //Test empty squares
        TetrisBoard board = new TetrisBoard(10, 24);
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(null, board.getGrid(j, i));
            }
        }

        //Test placed pieces
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(Piece.PieceType.T, board.getGrid(0, 0));
        assertEquals(Piece.PieceType.T, board.getGrid(1, 1));
        assertEquals(Piece.PieceType.T, board.getGrid(1, 0));
        assertEquals(Piece.PieceType.T, board.getGrid(2, 0));

        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(0, 15));
        Move.executeDrop(board);
        assertEquals(Piece.PieceType.T, board.getGrid(0, 0));
        assertEquals(Piece.PieceType.T, board.getGrid(1, 1));
        assertEquals(Piece.PieceType.T, board.getGrid(1, 0));
        assertEquals(Piece.PieceType.T, board.getGrid(2, 0));
        assertEquals(Piece.PieceType.STICK, board.getGrid(0, 2));
        assertEquals(Piece.PieceType.STICK, board.getGrid(1, 2));
        assertEquals(Piece.PieceType.STICK, board.getGrid(2, 2));
        assertEquals(Piece.PieceType.STICK, board.getGrid(3, 2));

        //Test bad boards
        board = new TetrisBoard(1, 0);
        assertEquals(null, board.getGrid(2, 0));
        assertEquals(null, board.getGrid(0, 324));
        assertEquals(null, board.getGrid(-3294, -48));

        board = new TetrisBoard(0, 1);
        assertEquals(null, board.getGrid(2, 0));
        assertEquals(null, board.getGrid(0, 324));
        assertEquals(null, board.getGrid(-3294, -48));

        board = new TetrisBoard(0, 0);
        assertEquals(null, board.getGrid(2, 0));
        assertEquals(null, board.getGrid(0, 324));
        assertEquals(null, board.getGrid(-3294, -48));

        board = new TetrisBoard(-1928, 0);
        assertEquals(null, board.getGrid(2, 0));
        assertEquals(null, board.getGrid(0, 324));
        assertEquals(null, board.getGrid(-3294, -48));

        board = new TetrisBoard(-1293, -91823);
        assertEquals(null, board.getGrid(2, 0));
        assertEquals(null, board.getGrid(0, 324));
        assertEquals(null, board.getGrid(-3294, -48));

    }


}