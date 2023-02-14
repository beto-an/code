package assignment;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TetrisPieceStateModel implements FullModel {

    private static final HashMap<TetrisBoard[], EnvironmentOutcome> BOARDS_TO_OUTCOME = new HashMap<>();

    public TetrisPieceStateModel() {

    }

    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        if (state instanceof BoardState && action instanceof TetrisTransitionAction) {
            return specificSample((BoardState) state, (TetrisTransitionAction) action);
        }
        return null;
    }

    @Override
    public boolean terminal(State state) {
        if (state instanceof BoardState) {
            return boardTerminal((BoardState) state);
        }
        return false;
    }

    public static boolean boardTerminal(BoardState state) {
        return state.terminal();
    }

    public EnvironmentOutcome specificSample(BoardState state, TetrisTransitionAction action) {
        TetrisBoard[] boards = new TetrisBoard[]{state.getTetrisBoard(), action.getBoardState().getTetrisBoard()};
        EnvironmentOutcome output = BOARDS_TO_OUTCOME.get(boards);
        if (output == null) {
            BOARDS_TO_OUTCOME.put(boards, new EnvironmentOutcome(state, action, action.getBoardState(), Rewarder.reward(boards[0], boards[1]), action.getBoardState().terminal()));
            output = BOARDS_TO_OUTCOME.get(boards);
        }
        return output;
    }

    @Override
    public List<TransitionProb> transitions(State state, Action action) {
        ArrayList<TransitionProb> probs = new ArrayList<>();
        EnvironmentOutcome eo = sample(state, action);
        if (eo == null) {
            return null;
        }
        probs.add(new TransitionProb(1, eo));
        return probs;
    }
}
