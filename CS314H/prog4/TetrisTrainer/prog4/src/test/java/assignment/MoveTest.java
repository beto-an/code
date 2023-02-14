package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    TetrisBoard createFilledBoard(Piece piece, Point lowerLeftPos, Point[] empty, boolean keepBoundingBoxEmpty) {
        TetrisBoard board = new TetrisBoard(10, 24);
        HashSet<Point> hs = new HashSet<>();
        for (Point p : empty) {
            hs.add(p);
        }
        ArrayList<BoardRow> grid = board.getGrid();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 10; j++) {
                if (hs.contains(new Point(j, i))) continue;
                grid.get(i).setEntry(j, TetrisBoard.getRandomPiece().getType());
            }
        }
        if (keepBoundingBoxEmpty) {
            for (int i = lowerLeftPos.y; i < lowerLeftPos.y + piece.getHeight(); i++) {
                for (int j = lowerLeftPos.x; j < lowerLeftPos.x + piece.getWidth(); j++) {
                    grid.get(i).setEntry(j, null);
                }
            }
        }
        board.nextPiece(piece, lowerLeftPos);
        return board;
    }

    TetrisBoard createEmptyBoard(Piece piece, Point lowerLeftPos, Point[] filled) {
        TetrisBoard board = new TetrisBoard(10, 24);
        ArrayList<BoardRow> grid = board.getGrid();
        for (Point p : filled) {
            grid.get(p.y).setEntry(p.x, TetrisBoard.getRandomPiece().getType());
        }
        board.nextPiece(piece, lowerLeftPos);
        return board;
    }

    static int[][] leftdx = {
            { 0, 1, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 2, 0, 1 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 }
    };

    static int[][] rightdx = {
            { 0, 0, 0, 1 },
            { 0, 0, 0, 0 },
            { 0, 1, 0, 2 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 }
    };

    //Tests both left and right
    @Test
    void testExecuteShift() {

        //Test null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeShift(emptyBoard, 0, 0));

        //Test moving left
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                //Should fail
                int col = 4 + leftdx[j][i];
                TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)});
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeShift(board, -1, 0));
                assertEquals(new Point(5, 5), board.getLowerLeftPos());

                //Should succeed
                board = createFilledBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)}, true);
                assertEquals(Board.Result.SUCCESS, Move.executeShift(board, -1, 0));
                assertEquals(new Point(4, 5), board.getLowerLeftPos());

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test moving right
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                //Should fail
                int col = currPiece.getWidth() + 5 - rightdx[j][i];
                TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)});
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeShift(board, 1, 0));
                assertEquals(new Point(5, 5), board.getLowerLeftPos());

                //Should succeed
                board = createFilledBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)}, true);
                assertEquals(Board.Result.SUCCESS, Move.executeShift(board, 1, 0));
                assertEquals(new Point(6, 5), board.getLowerLeftPos());

                currPiece = currPiece.clockwisePiece();

            }
        }

    }

    void assertArrayEquality(int[] expected, int[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    void testExecuteDown() {

        //Test null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeDown(emptyBoard));

        //Test successfully moving down
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                TetrisBoard board = createFilledBoard(currPiece, new Point(5, 5), new Point[]{new Point(5, 4), new Point(6, 4), new Point(7, 4), new Point(8, 4)}, true);
                assertEquals(Board.Result.SUCCESS, Move.executeDown(board));
                assertEquals(new Point(5, 4), board.getLowerLeftPos());

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test piece being placed
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                int[] skirt = currPiece.getSkirt();
                Point[] body = currPiece.getBody();

                for (int k = 0; k < skirt.length; k++) {

                    int skirtVal = skirt[k];

                    if (skirtVal == Integer.MAX_VALUE) continue;

                    TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(5 + k, 4 + skirtVal)});
                    assertEquals(Board.Result.PLACE, Move.executeDown(board));

                    for (Point p : body) {
                        assertEquals(currPiece.getType(), board.getGrid(5 + p.x, 5 + p.y));
                    }

                }

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test correctness of column height and rows cleared updates
        TetrisBoard board = createEmptyBoard(null, null, new Point[]{});

        checkDownPlace(board, new TetrisPiece(Piece.PieceType.T), new Point(0, -1), 0, new int[]{ 1, 2, 1, 0, 0, 0, 0, 0, 0, 0 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_DOG), new Point(2, 0), 0, new int[]{ 1, 2, 2, 3, 3, 0, 0, 0, 0, 0 }, 3, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.STICK), new Point(3, -2), 0, new int[]{ 1, 2, 2, 3, 3, 1, 1, 0, 0, 0 }, 3, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_L), new Point(7, -1), 1, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 0, 0 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.SQUARE), new Point(8, 0), 0, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 2, 2 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, -1), 0, new int[]{ 0, 1, 1, 2, 2, 1, 2, 1, 2, 2 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_DOG), new Point(1, 1), 0, new int[]{ 0, 4, 4, 3, 2, 1, 2, 1, 2, 2 }, 4, false);

    }

    void checkDownPlace(TetrisBoard board, Piece piece, Point location, int rowsCleared, int[] colHeights, int maxHeight, boolean isDrop) {
        board.nextPiece(piece, location);
        if (isDrop) assertEquals(Board.Result.PLACE, Move.executeDrop(board));
        else assertEquals(Board.Result.PLACE, Move.executeDown(board));
        assertNull(board.getCurrentPiece());
        assertNull(board.getLowerLeftPos());
        assertEquals(rowsCleared, board.getRowsCleared());
        assertArrayEquality(colHeights, board.getColHeight());
        assertEquals(maxHeight, board.getMaxHeight());
    }

    int rand(int range) {
        return (int) (Math.random() * range);
    }

    @Test
    void testExecuteDrop() {

        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeDrop(emptyBoard));

        //Test piece being placed
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                int[] skirt = currPiece.getSkirt();
                Point[] body = currPiece.getBody();

                for (int k = 0; k < skirt.length; k++) {

                    int skirtVal = skirt[k];

                    if (skirtVal == Integer.MAX_VALUE) continue;

                    TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5 + rand(10)), new Point[]{new Point(5 + k, 4 + skirtVal)});
                    assertEquals(Board.Result.PLACE, Move.executeDrop(board));

                    for (Point p : body) {
                        assertEquals(currPiece.getType(), board.getGrid(5 + p.x, 5 + p.y));
                    }

                }

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test correctness of column height and rows cleared updates
        TetrisBoard board = createEmptyBoard(null, null, new Point[]{});

        checkDownPlace(board, new TetrisPiece(Piece.PieceType.T), new Point(0, -1 + rand(10)), 0, new int[]{ 1, 2, 1, 0, 0, 0, 0, 0, 0, 0 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_DOG), new Point(2, 0 + rand(10)), 0, new int[]{ 1, 2, 2, 3, 3, 0, 0, 0, 0, 0 }, 3, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.STICK), new Point(3, -2), 0, new int[]{ 1, 2, 2, 3, 3, 1, 1, 0, 0, 0 }, 3, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_L), new Point(7, -1 + rand(10)), 1, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 0, 0 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.SQUARE), new Point(8, 0 + rand(10)), 0, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 2, 2 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, -1), 0, new int[]{ 0, 1, 1, 2, 2, 1, 2, 1, 2, 2 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_DOG), new Point(1, 1 + rand(10)), 0, new int[]{ 0, 4, 4, 3, 2, 1, 2, 1, 2, 2 }, 4, true);


    }

    @Test
    void testExecuteTurn() {

        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeTurn(emptyBoard, true));
        assertEquals(Board.Result.NO_PIECE, Move.executeTurn(emptyBoard, false));

        //Test piece being placed
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                TetrisBoard filledBoard = createFilledBoard(currPiece, new Point(4, 4), new Point[]{}, false);
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeTurn(filledBoard, true));
                assertEquals(currPiece, filledBoard.getCurrentPiece());
                assertEquals(4, filledBoard.getLowerLeftPos().x);
                assertEquals(4, filledBoard.getLowerLeftPos().y);
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeTurn(filledBoard, false));
                assertEquals(currPiece, filledBoard.getCurrentPiece());
                assertEquals(4, filledBoard.getLowerLeftPos().x);
                assertEquals(4, filledBoard.getLowerLeftPos().y);

                Point[] clockwiseKicks;
                Point[] counterclockwiseKicks;

                if (currPiece.getType() == Piece.PieceType.SQUARE) {

                    clockwiseKicks = new Point[1];
                    clockwiseKicks[0] = new Point(0, 0);

                    counterclockwiseKicks = new Point[1];
                    counterclockwiseKicks[0] = new Point(0, 0);

                } else if (currPiece.getType() == Piece.PieceType.STICK) {

                    clockwiseKicks = Piece.I_CLOCKWISE_WALL_KICKS[i];
                    counterclockwiseKicks = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[i];

                } else {

                    clockwiseKicks = Piece.NORMAL_CLOCKWISE_WALL_KICKS[i];
                    counterclockwiseKicks = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[i];

                }

                for (int k = 0; k < clockwiseKicks.length; k++) {

                    //test clockwise turn
                    int dx = clockwiseKicks[k].x, dy = clockwiseKicks[k].y;
                    Point[] body = currPiece.clockwisePiece().getBody();
                    Point[] empty = new Point[body.length];
                    for (int l = 0; l < body.length; l++) {
                        empty[l] = new Point(body[l].x + dx + 4, body[l].y + dy + 4);
                    }
                    TetrisBoard board = createFilledBoard(currPiece, new Point(4, 4), empty, false);
                    assertEquals(Board.Result.SUCCESS, Move.executeTurn(board, true));
                    assertEquals(currPiece.clockwisePiece().getType(), board.getCurrentPiece().getType());
                    assertEquals(currPiece.clockwisePiece().getRotationIndex(), board.getCurrentPiece().getRotationIndex());
                    assertEquals(4 + dx, board.getLowerLeftPos().x);
                    assertEquals(4 + dy, board.getLowerLeftPos().y);

                    //test counterclockwise turn
                    dx = counterclockwiseKicks[k].x;
                    dy = counterclockwiseKicks[k].y;
                    body = currPiece.counterclockwisePiece().getBody();
                    empty = new Point[body.length];
                    for (int l = 0; l < body.length; l++) {
                        empty[l] = new Point(body[l].x + dx + 4, body[l].y + dy + 4);
                    }
                    board = createFilledBoard(currPiece, new Point(4, 4), empty, false);
                    System.out.println("" + dx + " " + dy + " " + j);
                    assertEquals(Board.Result.SUCCESS, Move.executeTurn(board, false));
                    assertEquals(currPiece.counterclockwisePiece().getType(), board.getCurrentPiece().getType());
                    assertEquals(currPiece.counterclockwisePiece().getRotationIndex(), board.getCurrentPiece().getRotationIndex());
                    assertEquals(4 + dx, board.getLowerLeftPos().x);
                    assertEquals(4 + dy, board.getLowerLeftPos().y);

                }

                currPiece = currPiece.clockwisePiece();

            }
        }

    }

}