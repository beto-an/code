package assignment;

import burlap.mdp.core.state.State;
import burlap.statehashing.HashableState;
import burlap.statehashing.HashableStateFactory;

public class BoardStateHasher implements HashableStateFactory {
    @Override
    public HashableState hashState(State state) {
        if (state instanceof BoardState) {
            return new HashableBoardState((BoardState) state);
        }
        return null;
    }
}
