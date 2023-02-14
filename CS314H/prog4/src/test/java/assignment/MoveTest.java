package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


//Test the Move class
class MoveTest {


    //Helper method that creates a completely filled board, except for a set of points to keep empty
    TetrisBoard createFilledBoard(Piece piece, Point lowerLeftPos, Point[] empty, boolean keepBoundingBoxEmpty) {

        //Create a new board
        TetrisBoard board = new TetrisBoard(10, 24);

        //Preprocess empty points
        HashSet<Point> hs = new HashSet<>();
        for (Point p : empty) {
            hs.add(p);
        }

        //Fill each square a random PieceType unless it should be empty
        ArrayList<BoardRow> grid = board.getGrid();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 10; j++) {
                if (hs.contains(new Point(j, i))) continue;
                grid.get(i).setEntry(j, TetrisBoard.getRandomPiece().getType());
            }
        }

        //Keep the bounding box empty if needed
        if (keepBoundingBoxEmpty) {
            for (int i = lowerLeftPos.y; i < lowerLeftPos.y + piece.getHeight(); i++) {
                for (int j = lowerLeftPos.x; j < lowerLeftPos.x + piece.getWidth(); j++) {
                    grid.get(i).setEntry(j, null);
                }
            }
        }

        //Set the next piece and position
        board.nextPiece(piece, lowerLeftPos);
        return board;

    }


    //Helper method that creates an empty board except for a set number of filled points
    TetrisBoard createEmptyBoard(Piece piece, Point lowerLeftPos, Point[] filled) {

        //Create the board and add the filled points
        TetrisBoard board = new TetrisBoard(10, 24);
        ArrayList<BoardRow> grid = board.getGrid();
        for (Point p : filled) {
            grid.get(p.y).setEntry(p.x, TetrisBoard.getRandomPiece().getType());
        }

        //Set the next piece and position
        board.nextPiece(piece, lowerLeftPos);
        return board;

    }


    //Number of empty columns from left of bounding box for each piece and orientation
    static int[][] leftdx = {
            { 0, 1, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 2, 0, 1 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 },
            { 0, 1, 0, 0 }
    };


    //Number of empty columns from right of bounding box for each piece and orientation
    static int[][] rightdx = {
            { 0, 0, 0, 1 },
            { 0, 0, 0, 0 },
            { 0, 1, 0, 2 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 },
            { 0, 0, 0, 1 }
    };


    //Tests both Action.LEFT and Action.RIGHT
    @Test
    void testExecuteShift() {

        //Test null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeShift(emptyBoard, 0, 0));

        //Test moving left
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                //Move left into filled squares, should fail
                int col = 4 + leftdx[j][i];
                TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)});
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeShift(board, -1, 0));
                assertEquals(new Point(5, 5), board.getLowerLeftPos());

                //Move left into empty squares, should pass
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

                //Move right into filled squares, should fail
                int col = currPiece.getWidth() + 5 - rightdx[j][i];
                TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)});
                assertEquals(Board.Result.OUT_BOUNDS, Move.executeShift(board, 1, 0));
                assertEquals(new Point(5, 5), board.getLowerLeftPos());

                //Move right into empty squares, should pass
                board = createFilledBoard(currPiece, new Point(5, 5), new Point[]{new Point(col, 5), new Point(col, 6), new Point(col, 7), new Point(col, 8)}, true);
                assertEquals(Board.Result.SUCCESS, Move.executeShift(board, 1, 0));
                assertEquals(new Point(6, 5), board.getLowerLeftPos());

                currPiece = currPiece.clockwisePiece();

            }
        }

    }


    //Helper method for asserting that two arrays are equal
    void assertArrayEquality(int[] expected, int[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }


    //Test Action.DOWN
    @Test
    void testExecuteDown() {

        //Test null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeDown(emptyBoard));

        //Test successfully moving down into empty squares
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                TetrisBoard board = createFilledBoard(currPiece, new Point(5, 5), new Point[]{new Point(5, 4), new Point(6, 4), new Point(7, 4), new Point(8, 4)}, true);
                assertEquals(Board.Result.SUCCESS, Move.executeDown(board));
                assertEquals(new Point(5, 4), board.getLowerLeftPos());

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test piece being placed (moving down intersects with the grid or boundary)
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];

            for (int i = 0; i < 4; i++) {

                int[] skirt = currPiece.getSkirt();
                Point[] body = currPiece.getBody();

                //Fill a square just under each of the lowest squares on the piece's body, test for intersection
                for (int k = 0; k < skirt.length; k++) {

                    int skirtVal = skirt[k];

                    if (skirtVal == Integer.MAX_VALUE) continue;

                    TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5), new Point[]{new Point(5 + k, 4 + skirtVal)});
                    assertEquals(Board.Result.PLACE, Move.executeDown(board));

                    //Test that the piece is placed in the right place
                    for (Point p : body) {
                        assertEquals(currPiece.getType(), board.getGrid(5 + p.x, 5 + p.y));
                    }

                }

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test correctness of column height and rows cleared updates for each PieceType
        TetrisBoard board = createEmptyBoard(null, null, new Point[]{});

        checkDownPlace(board, new TetrisPiece(Piece.PieceType.T), new Point(0, -1), 0, new int[]{ 1, 2, 1, 0, 0, 0, 0, 0, 0, 0 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_DOG), new Point(2, 0), 0, new int[]{ 1, 2, 2, 3, 3, 0, 0, 0, 0, 0 }, 3, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.STICK), new Point(3, -2), 0, new int[]{ 1, 2, 2, 3, 3, 1, 1, 0, 0, 0 }, 3, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_L), new Point(7, -1), 1, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 0, 0 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.SQUARE), new Point(8, 0), 0, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 2, 2 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, -1), 0, new int[]{ 0, 1, 1, 2, 2, 1, 2, 1, 2, 2 }, 2, false);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_DOG), new Point(1, 1), 0, new int[]{ 0, 4, 4, 3, 2, 1, 2, 1, 2, 2 }, 4, false);

    }


    //Helper method for running one set of tests determining if the piece is placed correctly
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


    //Helper method that returns a random int from 0 to range - 1
    int rand(int range) {
        return (int) (Math.random() * range);
    }


    //Tests Action.DROP
    @Test
    void testExecuteDrop() {

        //Test for null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeDrop(emptyBoard));

        //Test piece being placed
        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                int[] skirt = currPiece.getSkirt();
                Point[] body = currPiece.getBody();

                //Test dropping a piece down to intersect on each of its body's lowest points
                for (int k = 0; k < skirt.length; k++) {

                    int skirtVal = skirt[k];

                    if (skirtVal == Integer.MAX_VALUE) continue;

                    TetrisBoard board = createEmptyBoard(currPiece, new Point(5, 5 + rand(10)), new Point[]{new Point(5 + k, 4 + skirtVal)});
                    assertEquals(Board.Result.PLACE, Move.executeDrop(board));

                    //Make sure piece is placed correctly
                    for (Point p : body) {
                        assertEquals(currPiece.getType(), board.getGrid(5 + p.x, 5 + p.y));
                    }

                }

                currPiece = currPiece.clockwisePiece();

            }
        }

        //Test correctness of column height and rows cleared updates for dropped pieces
        TetrisBoard board = createEmptyBoard(null, null, new Point[]{});

        checkDownPlace(board, new TetrisPiece(Piece.PieceType.T), new Point(0, -1 + rand(10)), 0, new int[]{ 1, 2, 1, 0, 0, 0, 0, 0, 0, 0 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_DOG), new Point(2, 0 + rand(10)), 0, new int[]{ 1, 2, 2, 3, 3, 0, 0, 0, 0, 0 }, 3, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.STICK), new Point(3, -2), 0, new int[]{ 1, 2, 2, 3, 3, 1, 1, 0, 0, 0 }, 3, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_L), new Point(7, -1 + rand(10)), 1, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 0, 0 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.SQUARE), new Point(8, 0 + rand(10)), 0, new int[]{ 0, 1, 1, 2, 2, 0, 0, 1, 2, 2 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, -1), 0, new int[]{ 0, 1, 1, 2, 2, 1, 2, 1, 2, 2 }, 2, true);
        checkDownPlace(board, new TetrisPiece(Piece.PieceType.LEFT_DOG), new Point(1, 1 + rand(10)), 0, new int[]{ 0, 4, 4, 3, 2, 1, 2, 1, 2, 2 }, 4, true);


    }


    //Helper method for finding absolute positions of a piece's body
    Point[] getPosition(Point[] body, Point start) {
        Point[] newBody = new Point[body.length];
        for (int i = 0; i < body.length; i++) {
            newBody[i] = new Point(body[i].x + start.x, body[i].y + start.y);
        }
        return newBody;
    }


    //Tests both Action.CLOCKWISE and Action.COUNTERCLOCKWISE
    @Test
    void testExecuteTurn() {

        //Test for null piece
        TetrisBoard emptyBoard = createEmptyBoard(null, null, new Point[]{});
        assertEquals(Board.Result.NO_PIECE, Move.executeTurn(emptyBoard, true));
        assertEquals(Board.Result.NO_PIECE, Move.executeTurn(emptyBoard, false));

        for (int j = 0; j < 7; j++) {
            Piece currPiece = TetrisBoard.pieces[j];
            for (int i = 0; i < 4; i++) {

                //Test for failure when piece rotated into filled board
                if (currPiece.getType() != Piece.PieceType.SQUARE) {
                    TetrisBoard filledBoard = createFilledBoard(currPiece, new Point(4, 4), getPosition(currPiece.getBody(), new Point(4, 4)), false);
                    assertEquals(Board.Result.OUT_BOUNDS, Move.executeTurn(filledBoard, true));
                    assertEquals(currPiece, filledBoard.getCurrentPiece());
                    assertEquals(4, filledBoard.getLowerLeftPos().x);
                    assertEquals(4, filledBoard.getLowerLeftPos().y);

                    assertEquals(Board.Result.OUT_BOUNDS, Move.executeTurn(filledBoard, false));
                    assertEquals(currPiece, filledBoard.getCurrentPiece());
                    assertEquals(4, filledBoard.getLowerLeftPos().x);
                    assertEquals(4, filledBoard.getLowerLeftPos().y);
                }

                Point[] clockwiseKicks;
                Point[] counterclockwiseKicks;

                //Determine correct set of kicks to use
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

                    //Test successful clockwise turn for this set of kicks
                    int dx = clockwiseKicks[k].x, dy = clockwiseKicks[k].y;
                    Point[] body = currPiece.clockwisePiece().getBody();
                    Point[] empty = new Point[body.length * 2];
                    for (int l = 0; l < body.length; l++) {
                        empty[l] = new Point(body[l].x + dx + 4, body[l].y + dy + 4);
                    }
                    Point[] newBody = getPosition(currPiece.getBody(), new Point(4, 4));
                    for (int l = body.length; l < empty.length; l++) {
                        empty[l] = newBody[l - body.length];
                    }
                    TetrisBoard board = createFilledBoard(currPiece, new Point(4, 4), empty, false);
                    for (Point p : newBody) {
                        board.fillEntry(p.x, p.y);
                    }
                    for (int l = 0; l < body.length; l++) {
                        board.emptyEntry(empty[l].x, empty[l].y);
                    }
                    assertEquals(Board.Result.SUCCESS, Move.executeTurn(board, true));
                    assertEquals(currPiece.clockwisePiece().getType(), board.getCurrentPiece().getType());
                    assertEquals(currPiece.clockwisePiece().getRotationIndex(), board.getCurrentPiece().getRotationIndex());
                    assertEquals(4 + dx, board.getLowerLeftPos().x);
                    assertEquals(4 + dy, board.getLowerLeftPos().y);

                    //Test successful counterclockwise turn for this set of kicks
                    dx = counterclockwiseKicks[k].x;
                    dy = counterclockwiseKicks[k].y;
                    body = currPiece.counterclockwisePiece().getBody();
                    empty = new Point[body.length * 2];
                    for (int l = 0; l < body.length; l++) {
                        empty[l] = new Point(body[l].x + dx + 4, body[l].y + dy + 4);
                    }
                    newBody = getPosition(currPiece.getBody(), new Point(4, 4));
                    for (int l = body.length; l < empty.length; l++) {
                        empty[l] = newBody[l - body.length];
                    }
                    board = createFilledBoard(currPiece, new Point(4, 4), empty, false);
                    for (Point p : newBody) {
                        board.fillEntry(p.x, p.y);
                    }
                    for (int l = 0; l < body.length; l++) {
                        board.emptyEntry(empty[l].x, empty[l].y);
                    }
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