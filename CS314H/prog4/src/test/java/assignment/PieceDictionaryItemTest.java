package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


//Test the PieceDictionaryItem enum
class PieceDictionaryItemTest {


    //Helper array storing all the PieceTypes
    private static final Piece.PieceType[] TYPES = {
            Piece.PieceType.T,
            Piece.PieceType.SQUARE,
            Piece.PieceType.STICK,
            Piece.PieceType.LEFT_L,
            Piece.PieceType.RIGHT_L,
            Piece.PieceType.LEFT_DOG,
            Piece.PieceType.RIGHT_DOG,
    };


    //Helper array storing all the body's point information
    private static final Point[][] BODIES = {
            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
            {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},

            {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},

            {new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2)},
            {new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3)},
            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},

            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2)},
            {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
            {new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)},

            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
            {new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2)},

            {new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
            {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
            {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},

            {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
            {new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)},
    };


    //Helper array storing all the skirt values
    private static final int[][] SKIRTS = {

            {1, 1, 1},
            {Integer.MAX_VALUE, 0, 1},
            {1, 0, 1},
            {1, 0, Integer.MAX_VALUE},

            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},

            {2, 2, 2, 2},
            {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE},
            {1, 1, 1, 1},
            {Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE},

            {1, 1, 1},
            {Integer.MAX_VALUE, 0, 2},
            {1, 1, 0},
            {0, 0, Integer.MAX_VALUE},

            {1, 1, 1},
            {Integer.MAX_VALUE, 0, 0},
            {0, 1, 1},
            {2, 0, Integer.MAX_VALUE},

            {2, 1, 1},
            {Integer.MAX_VALUE, 0, 1},
            {1, 0, 0},
            {0, 1, Integer.MAX_VALUE},

            {1, 1, 2},
            {Integer.MAX_VALUE, 1, 0},
            {0, 0, 1},
            {1, 0, Integer.MAX_VALUE},
    };


    //Heper array storing the rotation indices
    private static final int[] ROTATION_INDICES = {0, 1, 2, 3};


    //Test that the PieceDictionaryItems have the correct type
    @Test
    void getType() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getType(), TYPES[i / 4]);
            i++;
        }
    }


    //Test that the PieceDictionaryItems have the correct bodies
    @Test
    void getBody() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertArrayEquals(piece.getBody(), BODIES[i]);
            i++;
        }
    }


    //Test that the PieceDictionaryItems have the correct skirts
    @Test
    void getSkirt() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertArrayEquals(piece.getSkirt(), SKIRTS[i]);
            i++;
        }
    }


    //Test that the PieceDictionaryItems have the correct rotation indices
    @Test
    void getRotationIndex() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getRotationIndex(), ROTATION_INDICES[i % 4]);
            i++;
        }
    }


    //Test that the PieceDictionaryItems store the correct Piece objects
    @Test
    void getCurrentPiece() {
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece(), new TetrisPiece(piece));
        }
    }


}