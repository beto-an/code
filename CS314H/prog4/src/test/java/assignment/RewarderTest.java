package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


//Test the Rewarder class
class RewarderTest {


    //Test that the valueOfState function returns the correct value
    @Test
    void valueOfState() {

        //Empty board
        TetrisBoard tb = new TetrisBoard(10, 24);
        assertEquals(0, Rewarder.valueOfState(tb));

        //Board with pieces
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(-2.51, Rewarder.valueOfState(tb), 0.01);

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(-15.43, Rewarder.valueOfState(tb), 0.01);

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(-41.31, Rewarder.valueOfState(tb), 0.01);

    }


    //Test that the countHoles function correctly counts holes that exist in the grid
    @Test
    void countHoles() {

        //Empty board
        TetrisBoard tb = new TetrisBoard(10, 24);
        assertEquals(0, Rewarder.countHoles(tb));

        //Board with pieces
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(0, Rewarder.countHoles(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(1, Rewarder.countHoles(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(3, Rewarder.countHoles(tb));

    }


    //Test that the calculateBumpiness function correctly calculates a bumpiness score
    @Test
    void calculateBumpiness() {

        //Empty board
        TetrisBoard tb = new TetrisBoard(10, 24);
        assertEquals(0, Rewarder.calculateBumpiness(tb));

        //Board with pieces
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(1, Rewarder.calculateBumpiness(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(3, Rewarder.calculateBumpiness(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(11, Rewarder.calculateBumpiness(tb));

    }


    //Test that the calculateHeights function correctly sums the column heights
    @Test
    void calculateHeights() {

        //Empty board
        TetrisBoard tb = new TetrisBoard(10, 24);
        assertEquals(0, Rewarder.calculateHeights(tb));

        //Board with pieces
        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.STICK));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(4, Rewarder.calculateHeights(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(9, Rewarder.calculateHeights(tb));

        tb.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_DOG));
        tb.setLowerLeftPos(new Point(0, 16));
        tb.move(Board.Action.DROP);
        assertEquals(15, Rewarder.calculateHeights(tb));

    }


}