package assignment;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TetrisActionType implements ActionType {

    private static int count =  0;
    @Override
    public String typeName() {
        return null;
    }

    @Override
    public Action associatedAction(String s) {
        return null;
    }

    @Override
    public List<Action> allApplicableActions(State state) {
        if (!(state instanceof BoardState)) {
            return null;
        }
        BoardState board = (BoardState) state;
        ArrayList<Action> actions = new ArrayList<>();
        ArrayList<TetrisBoard> possibleBoards = board.getTetrisBoard().generateAllStates();
        for (TetrisBoard b : possibleBoards) {
            actions.add(new TetrisTransitionAction(new BoardState(b)));
        }
        return actions;
    }
}
