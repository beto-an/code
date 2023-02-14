package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


//Test the SimpleBrain class
class SimpleBrainTest {

    @Test
    void findBestNextBoard() {

        //Test empty board
        SimpleBrain brain = new SimpleBrain();
        TetrisBoard board = new TetrisBoard(10, 24);

        TetrisBoard best = SimpleBrain.findBestNextBoard(board);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                assertNull(best.getGrid(i, j));
            }
        }

        //Test board with pieces
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 20));
        Move.executeDrop(board);
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(3, 20));
        Move.executeDrop(board);
        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(3, 20));

        HashSet<Point> filled = new HashSet<>();
        filled.add(new Point(1, 0));
        filled.add(new Point(4, 0));

        best = SimpleBrain.findBestNextBoard(board);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                Point currPoint = new Point(i, j);
                if (filled.contains(currPoint)) assertNotNull(best.getGrid(i, j));
                else assertNull(best.getGrid(i, j));
            }
        }

    }


    //Test that the nextMove function correctly returns the next move for different board states
    @Test
    void nextMove() {

        //Test null
        SimpleBrain brain = new SimpleBrain();
        TetrisBoard board = new TetrisBoard(10, 24);

        assertEquals(null, brain.nextMove(board));

        //Test hold
        brain = new SimpleBrain();
        board = new TetrisBoard(10, 24);
        board.nextPiece(new TetrisPiece(Piece.PieceType.SQUARE), new Point(4, 20));

        assertEquals(Board.Action.HOLD, brain.nextMove(board));
        Move.executeHold(board);

        //Test placing piece
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(0, 20));
        Move.executeDrop(board);
        board.nextPiece(new TetrisPiece(Piece.PieceType.T), new Point(3, 20));
        Move.executeDrop(board);
        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(3, 20));

        assertEquals(Board.Action.CLOCKWISE, brain.nextMove(board));
        Move.executeClockwise(board);

        assertEquals(Board.Action.CLOCKWISE, brain.nextMove(board));
        Move.executeClockwise(board);

        assertEquals(Board.Action.RIGHT, brain.nextMove(board));
        Move.executeRight(board);

        assertEquals(Board.Action.RIGHT, brain.nextMove(board));
        Move.executeRight(board);

        assertEquals(Board.Action.RIGHT, brain.nextMove(board));
        Move.executeRight(board);

        assertEquals(Board.Action.DROP, brain.nextMove(board));
        Move.executeDrop(board);

        board.nextPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG), new Point(4, 20));

        assertEquals(Board.Action.HOLD, brain.nextMove(board));
        Move.executeHold(board);

        assertEquals(Board.Action.CLOCKWISE, brain.nextMove(board));
        Move.executeClockwise(board);

        assertEquals(Board.Action.LEFT, brain.nextMove(board));
        Move.executeLeft(board);

        assertEquals(Board.Action.LEFT, brain.nextMove(board));
        Move.executeLeft(board);

        assertEquals(Board.Action.DROP, brain.nextMove(board));
        Move.executeDrop(board);

    }


}