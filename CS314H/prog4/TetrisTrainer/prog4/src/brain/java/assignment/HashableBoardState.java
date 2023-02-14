package assignment;

import burlap.mdp.core.state.State;
import burlap.statehashing.HashableState;

public class HashableBoardState implements HashableState {

    private final BoardState board;

    public HashableBoardState(BoardState board) {
        this.board = board;
    }

    @Override
    public State s() {
        return board;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HashableBoardState) {
            HashableBoardState hbs = (HashableBoardState) obj;
            return hbs.board.equals(this.board);
        }
        return false;
    }


}
