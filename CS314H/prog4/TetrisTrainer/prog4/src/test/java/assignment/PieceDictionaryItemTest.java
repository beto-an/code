package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PieceDictionaryItemTest {

    private static final Piece.PieceType[] TYPES = {
            Piece.PieceType.T,
            Piece.PieceType.SQUARE,
            Piece.PieceType.STICK,
            Piece.PieceType.LEFT_L,
            Piece.PieceType.RIGHT_L,
            Piece.PieceType.LEFT_DOG,
            Piece.PieceType.RIGHT_DOG,
    };

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

    private static final int[] ROTATION_INDICES = {0, 1, 2, 3};

    @Test
    void getType() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getType(), TYPES[i / 4]);
            i++;
        }
    }

    @Test
    void getBody() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertArrayEquals(piece.getBody(), BODIES[i]);
            i++;
        }
    }

    @Test
    void getSkirt() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertArrayEquals(piece.getSkirt(), SKIRTS[i]);
            i++;
        }
    }

    @Test
    void getRotationIndex() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getRotationIndex(), ROTATION_INDICES[i % 4]);
            i++;
        }
    }

    @Test
    void getCurrentPiece() {
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece(), new TetrisPiece(piece));
        }
    }
}