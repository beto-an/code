package assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


//A simple brain that implements the results of the genetic algorithm
public class SimpleBrain implements Brain {


    //Store a list of actions to take, could be empty
    private Queue<Board.Action> nextActions;


    //Helper function to return the next possible best board state given a current board state
    public static TetrisBoard findBestNextBoard(TetrisBoard board) {

        //Generate all possible boards resulting from placing the current piece in all places and orientations
        ArrayList<TetrisBoard> boards = board.generateAllStates();
        double maxScore = Integer.MIN_VALUE;
        TetrisBoard nextBest = null;

        //Loop through all future boards and assign a score to each, as calculated by Rewarder
        for (TetrisBoard newTB : boards) {

            //Get the score for the board and update values as necessary
            double score = Rewarder.reward(board, newTB);
            if (score > maxScore) {
                maxScore = score;
                nextBest = newTB;
            }

        }

        return nextBest;

    }


    //Get the next move based on the current board state
    @Override
    public Board.Action nextMove(Board currentBoard) {

        //If there is already a move queued up, return the next queued move
        if (nextActions != null && !nextActions.isEmpty()) {
            return nextActions.poll();
        }

        //If the board isn't a TetrisBoard, we can't calculate a move
        if (!(currentBoard instanceof TetrisBoard)) return null;
        TetrisBoard board = (TetrisBoard) currentBoard;

        //If no piece, then don't do anything
        if (board.getCurrentPiece() == null) return null;

        //If we aren't yet holding a piece, then just hold the current piece
        if (board.getHeldPiece() == null) {

            nextActions = new LinkedList<>();
            nextActions.add(Board.Action.HOLD);

        } else {

            //Set our next actions to be the actions that get us to the best possible next board
            nextActions = findBestNextBoard(board).getActionsTaken();

        }

        //If no valid actions, then return null
        if (nextActions == null) return null;

        //Return the next action to take
        return nextActions.poll();

    }


    //Simple constructor
    public SimpleBrain() {
        nextActions = new LinkedList<>();
    }


}
