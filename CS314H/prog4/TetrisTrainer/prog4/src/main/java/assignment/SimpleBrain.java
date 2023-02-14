package assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleBrain implements Brain {

    Queue<Board.Action> nextActions;

    @Override
    public Board.Action nextMove(Board currentBoard) {

        if (nextActions != null && !nextActions.isEmpty()) {
            return nextActions.poll();
        }
        if (!(currentBoard instanceof TetrisBoard)) return null;
        TetrisBoard board = (TetrisBoard) currentBoard;

        ArrayList<TetrisBoard> boards = board.generateAllStates();
        boolean isHoldingPiece = false;
        double maxScore = Integer.MIN_VALUE;
        TetrisBoard best = null;
        for (TetrisBoard newTB : boards) {
            if (newTB.getHeldPiece() != null) isHoldingPiece = true;
            double score = Rewarder.reward(board, newTB);
            if (score > maxScore) {
                maxScore = score;
                best = newTB;
            }
        }
        nextActions = best.getActionsTaken();

        if (!isHoldingPiece) {
            nextActions = new LinkedList<>();
            nextActions.add(Board.Action.HOLD);
        }

        if (nextActions == null) return null;

        return nextActions.poll();
    }

    public SimpleBrain() {
        nextActions = new LinkedList<>();
    }

}
