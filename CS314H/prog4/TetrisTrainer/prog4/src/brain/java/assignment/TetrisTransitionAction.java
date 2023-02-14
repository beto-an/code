package assignment;

import burlap.mdp.core.action.Action;

public class TetrisTransitionAction implements Action {
    private BoardState state;

    public TetrisTransitionAction(BoardState s) {
        state = s;
    }

    @Override
    public String actionName() {
        return null;
    }

    @Override
    public Action copy() {
        return new TetrisTransitionAction(state);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TetrisTransitionAction)) {
            return false;
        }
        TetrisTransitionAction that = (TetrisTransitionAction) obj;
        return that.state.equals(this.state);
    }

    public BoardState getBoardState() {
        return state;
    }

    public void cleanup() {
        state.cleanup();
        state = null;
    }
}
