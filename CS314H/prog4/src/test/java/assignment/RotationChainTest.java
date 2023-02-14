package assignment;

import org.junit.jupiter.api.Test;

import static assignment.PieceDictionaryItem.*;
import static org.junit.jupiter.api.Assertions.*;


//Test the RotationChain class
class RotationChainTest {


    //Helper array for storing the all values that the chains should contain
    private static final PieceDictionaryItem[][] ALL_CHAINS = {
            { PieceDictionaryItem.T_NORTH, T_EAST, T_SOUTH, PieceDictionaryItem.T_WEST },
            { PieceDictionaryItem.SQUARE_NORTH, PieceDictionaryItem.SQUARE_EAST, PieceDictionaryItem.SQUARE_SOUTH, PieceDictionaryItem.SQUARE_WEST },
            { PieceDictionaryItem.STICK_NORTH, PieceDictionaryItem.STICK_EAST, PieceDictionaryItem.STICK_SOUTH, PieceDictionaryItem.STICK_WEST },
            { PieceDictionaryItem.LEFT_L_NORTH, PieceDictionaryItem.LEFT_L_EAST, PieceDictionaryItem.LEFT_L_SOUTH, PieceDictionaryItem.LEFT_L_WEST },
            { PieceDictionaryItem.RIGHT_L_NORTH, PieceDictionaryItem.RIGHT_L_EAST, PieceDictionaryItem.RIGHT_L_SOUTH, PieceDictionaryItem.RIGHT_L_WEST },
            { PieceDictionaryItem.LEFT_DOG_NORTH, PieceDictionaryItem.LEFT_DOG_EAST, PieceDictionaryItem.LEFT_DOG_SOUTH, PieceDictionaryItem.LEFT_DOG_WEST },
            { PieceDictionaryItem.RIGHT_DOG_NORTH, PieceDictionaryItem.RIGHT_DOG_EAST, PieceDictionaryItem.RIGHT_DOG_SOUTH, PieceDictionaryItem.RIGHT_DOG_WEST },
    };


    //Helper array for storing all the corresponding PieceDictionaryItem enum types for a clockwise rotation
    private static final PieceDictionaryItem[] CLOCKWISE_ROTATION = {
            T_EAST,
            T_SOUTH,
            T_WEST,
            T_NORTH,
            SQUARE_EAST,
            SQUARE_SOUTH,
            SQUARE_WEST,
            SQUARE_NORTH,
            STICK_EAST,
            STICK_SOUTH,
            STICK_WEST,
            STICK_NORTH,
            LEFT_L_EAST,
            LEFT_L_SOUTH,
            LEFT_L_WEST,
            LEFT_L_NORTH,
            RIGHT_L_EAST,
            RIGHT_L_SOUTH,
            RIGHT_L_WEST,
            RIGHT_L_NORTH,
            LEFT_DOG_EAST,
            LEFT_DOG_SOUTH,
            LEFT_DOG_WEST,
            LEFT_DOG_NORTH,
            RIGHT_DOG_EAST,
            RIGHT_DOG_SOUTH,
            RIGHT_DOG_WEST,
            RIGHT_DOG_NORTH,
    };


    //Helper array for storing all the corresponding PieceDictionaryItem enum types for a counterclockwise rotation
    private static final PieceDictionaryItem[] COUNTERCLOCKWISE_ROTATION = {
            T_WEST,
            T_NORTH,
            T_EAST,
            T_SOUTH,
            SQUARE_WEST,
            SQUARE_NORTH,
            SQUARE_EAST,
            SQUARE_SOUTH,
            STICK_WEST,
            STICK_NORTH,
            STICK_EAST,
            STICK_SOUTH,
            LEFT_L_WEST,
            LEFT_L_NORTH,
            LEFT_L_EAST,
            LEFT_L_SOUTH,
            RIGHT_L_WEST,
            RIGHT_L_NORTH,
            RIGHT_L_EAST,
            RIGHT_L_SOUTH,
            LEFT_DOG_WEST,
            LEFT_DOG_NORTH,
            LEFT_DOG_EAST,
            LEFT_DOG_SOUTH,
            RIGHT_DOG_WEST,
            RIGHT_DOG_NORTH,
            RIGHT_DOG_EAST,
            RIGHT_DOG_SOUTH,
    };


    //Test that the correct chain is returned for a given PieceType
    @Test
    void getChain() {
        int i = 0;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            assertArrayEquals(RotationChain.getChain(type), ALL_CHAINS[i]);
            i++;
        }
    }


    //Test that the correct PieceDictionaryItem is returned when rotating clockwise
    @Test
    void rotateClockwise() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(RotationChain.rotateClockwise(piece), CLOCKWISE_ROTATION[i]);
            i++;
        }
    }


    //Test that the correct PieceDictionaryItem is returned when rotating counterclockwise
    @Test
    void rotateCounterClockwise() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(RotationChain.rotateCounterClockwise(piece), COUNTERCLOCKWISE_ROTATION[i]);
            i++;
        }
    }


}