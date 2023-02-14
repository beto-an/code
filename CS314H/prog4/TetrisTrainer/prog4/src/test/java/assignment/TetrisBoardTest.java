package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

class TetrisBoardTest {

    @Test
    void testEquals() {
        {
            TetrisBoard tb = filledBoard(5, 1, 20);
            TetrisBoard tb2 = new TetrisBoard(5, 20);
            assertNotEquals(tb, tb2);
            tb2.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
            assertNotEquals(tb, tb2);
            tb2.setLowerLeftPos(new Point(0, 16));
            assertNotEquals(tb, tb2);
            tb2.move(Board.Action.DROP);
            assertEquals(tb, tb2);
        }
    }

    private static BoardRow almostFullRow(int width) {
        BoardRow br = new BoardRow(width);
        for (int i = 0; i < width - 1; i++) {
            br.setEntry(i, Piece.PieceType.STICK);
        }
        return br;
    }

    private static TetrisBoard filledBoard(int width, int fillHeight, int height) {
        ArrayList<BoardRow> abr = new ArrayList<>();
        for (int i = 0; i < fillHeight; i++) {
            abr.add(almostFullRow(width));
        }
        for (int i = fillHeight; i < height; i++) {
            abr.add(new BoardRow(width));
        }
        TetrisBoard tb = new TetrisBoard(width, height);
        tb.setGrid(abr);
        return tb;
    }

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

    @Test
    void isValidPosition() {
        {
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

    }
}