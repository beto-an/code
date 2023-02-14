package assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//Test the TetrisPiece class
class TetrisPieceTest {


    //Helper array that stores the dimensions of each piece
    public static final int[] DIMENSION = {3, 2, 4, 3, 3, 3, 3};


    //Test that the getType method returns the correct type
    @Test
    void getType() {
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getType(), type);
        }
    }


    //Test that the getRotationIndex method returns the correct rotation index
    @Test
    void getRotationIndex() {
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getRotationIndex(), 0);
        }
    }


    //Test that the getWidth method returns the correct width
    @Test
    void getWidth() {

        //Test the enum PieceDictionaryItem
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece().getWidth(), DIMENSION[i / 4]);
            i++;
        }

        //Test each PieceType
        i = 0;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getWidth(), DIMENSION[i]);
            i++;
        }

    }


    //Test that the getHeight method returns the correct height
    @Test
    void getHeight() {

        //Test the enum PieceDictionaryItem
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece().getHeight(), DIMENSION[i / 4]);
            i++;
        }

        //Test each PieceType
        i = 0;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getHeight(), DIMENSION[i]);
            i++;
        }

    }

    //Test that the equalsMethod correctly compares two TetrisPiece objects
    @Test
    void testEquals() {

        PieceDictionaryItem[] allItems = PieceDictionaryItem.values();
        TetrisPiece[] allPieces = new TetrisPiece[allItems.length];

        for (int i = 0; i < allItems.length; i++) {
            allPieces[i] = new TetrisPiece(allItems[i]);
        }

        for (int i = 0; i < allPieces.length; i++) {

            TetrisPiece piece1 = allPieces[i];

            //Test for invalid inputs
            assertFalse(piece1.equals("hello"));
            assertFalse(piece1.equals(new TetrisBoard(0, 0)));
            assertFalse(piece1.equals(null));

            for (int j = 0; j < allPieces.length; j++) {

                TetrisPiece piece2 = allPieces[j];

                //Check equality is correct for same and different pieces
                if (i == j) assertEquals(piece1, piece2);
                else assertNotEquals(piece1, piece2);

            }

        }

    }


}