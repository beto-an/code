package assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//Test the BoardRow class
class BoardRowTest {


    //Test that the isFilled function correctly checks for filled boards
    @Test
    void isFilled() {

        //Test row length when adding pieces to row
        BoardRow row = new BoardRow(5);
        assertFalse(row.isFilled());

        row.setEntry(3, Piece.PieceType.STICK);
        assertFalse(row.isFilled());

        row.setEntry(2, Piece.PieceType.T);
        assertFalse(row.isFilled());

        row.setEntry(0, Piece.PieceType.LEFT_DOG);
        assertFalse(row.isFilled());

        row.setEntry(4, Piece.PieceType.RIGHT_L);
        assertFalse(row.isFilled());

        row.setEntry(1, Piece.PieceType.SQUARE);
        assertTrue(row.isFilled());

        //Test bad length rows
        row = new BoardRow(0);
        assertTrue(row.isFilled());

        row = new BoardRow(-2873);
        assertTrue(row.isFilled());

    }

    @Test
    void getNumFilled() {

        //Test row length when adding pieces to row
        BoardRow row = new BoardRow(5);
        assertEquals(0, row.getNumFilled());

        row.setEntry(3, Piece.PieceType.STICK);
        assertEquals(1, row.getNumFilled());

        row.setEntry(2, Piece.PieceType.T);
        assertEquals(2, row.getNumFilled());

        row.setEntry(0, Piece.PieceType.LEFT_DOG);
        assertEquals(3, row.getNumFilled());

        row.setEntry(4, Piece.PieceType.RIGHT_L);
        assertEquals(4, row.getNumFilled());

        row.setEntry(1, Piece.PieceType.SQUARE);
        assertEquals(5, row.getNumFilled());

        //Test bad length rows
        row = new BoardRow(0);
        assertEquals(0, row.getNumFilled());

        row = new BoardRow(-2873);
        assertEquals(0, row.getNumFilled());

    }

    @Test
    void testEquals() {

        BoardRow row1 = new BoardRow(5);
        BoardRow row2 = new BoardRow(5);
        BoardRow row3 = new BoardRow(6);

        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));
        assertFalse(row1.equals(row3));

        //Test for invalid inputs
        assertFalse(row1.equals("hello"));
        assertFalse(row1.equals(new TetrisPiece(Piece.PieceType.T)));
        assertFalse(row1.equals(null));

        //Test placed pieces
        row1.setEntry(0, Piece.PieceType.T);
        assertFalse(row1.equals(row2));
        assertFalse(row2.equals(row1));
        assertFalse(row1.equals(row3));

        row2.setEntry(0, Piece.PieceType.T);
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1.setEntry(3, Piece.PieceType.LEFT_DOG);
        assertFalse(row1.equals(row2));
        assertFalse(row2.equals(row1));

        row2.setEntry(3, Piece.PieceType.RIGHT_DOG);
        assertFalse(row1.equals(row2));
        assertFalse(row2.equals(row1));

        //Test bad length rows
        row1 = new BoardRow(0);
        row2 = new BoardRow(0);
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1 = new BoardRow(-1);
        row2 = new BoardRow(-1);
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1 = new BoardRow(-2);
        row2 = new BoardRow(-23);
        assertFalse(row1.equals(row2));
        assertFalse(row2.equals(row1));

    }

    @Test
    void testClone() {

        BoardRow row1 = new BoardRow(5);
        BoardRow row2 = row1.clone();

        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        //Test placed pieces
        row1.setEntry(0, Piece.PieceType.T);
        row2 = row1.clone();
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1.setEntry(3, Piece.PieceType.LEFT_DOG);
        row2 = row1.clone();
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1.setEntry(3, Piece.PieceType.RIGHT_DOG);
        row2 = row1.clone();
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        //Test bad length rows
        row1 = new BoardRow(0);
        row2 = row1.clone();
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

        row1 = new BoardRow(-23);
        row2 = row1.clone();
        assertTrue(row1.equals(row2));
        assertTrue(row2.equals(row1));

    }


}