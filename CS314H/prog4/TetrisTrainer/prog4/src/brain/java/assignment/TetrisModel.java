package assignment;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.MDPSolver;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QProvider;
import burlap.behavior.valuefunction.QValue;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;
import burlap.statehashing.HashableState;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import it.unimi.dsi.fastutil.Hash;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TetrisModel extends MDPSolver implements Planner, QProvider {
    protected Map<HashableState, Double> valueFunction;
    protected ValueFunction vinit;
    protected int numIterations;

    protected BoardState currentState;

    protected Policy p;

    public TetrisModel(SADomain domain, double gamma,
                       HashableStateFactory hashingFactory, ValueFunction vinit, int numIterations) {
        this.solverInit(domain, gamma, hashingFactory);
        this.vinit = vinit;
        this.numIterations = numIterations;
        this.valueFunction = new HashMap<HashableState, Double>();
    }

    @Override
    public double value(State s) {
        Double d = this.valueFunction.get(hashingFactory.hashState(s));
        if (d == null) {
            return vinit.value(s);
        }
        return d;
    }

    @Override
    public List<QValue> qValues(State s) {
        List<Action> applicableActions = this.applicableActions(s);
        List<QValue> qs = new ArrayList<QValue>(applicableActions.size());
        for (Action a : applicableActions) {
            qs.add(new QValue(s, a, this.qValue(s, a)));
        }
        return qs;
    }

    @Override
    public double qValue(State s, Action a) {

        if (this.model.terminal(s)) {
            return 0.;
        }

        //what are the possible outcomes?
        List<TransitionProb> tps = ((FullModel) this.model).transitions(s, a);

        //aggregate over each possible outcome
        double q = 0.;
        for (TransitionProb tp : tps) {
            //what is reward for this transition?
            double r = tp.eo.r;
            //System.out.println(r);
            //what is the value for the next state?
            double vp = this.valueFunction.get(this.hashingFactory.hashState(tp.eo.op));
            // System.out.println(vp);
            //add contribution weighted by transition probability and
            //discounting the next state
            q += tp.p * (r + this.gamma * vp);
        }
        //System.out.println(q);
        return q;

    }

    @Override
    public GreedyQPolicy planFromState(State initialState) {

        HashableState hashedInitialState = this.hashingFactory.hashState(initialState);
        if (this.valueFunction.containsKey(hashedInitialState)) {
            return new GreedyQPolicy(this); //already performed planning here!
        }

        //if the state is new, then find all reachable states from it first
        this.performReachabilityFrom(initialState);

        //now perform multiple iterations over the whole state space
        for (int i = 0; i < this.numIterations; i++) {
            //iterate over each state
            for (HashableState sh : this.valueFunction.keySet()) {
                //update its value using the bellman equation
                this.valueFunction.put(sh, QProvider.Helper.maxQ(this, sh.s()));
            }
        }
        return new GreedyQPolicy(this);
    }

    @Override
    public void resetSolver() {
        this.valueFunction.clear();
    }

    public void performReachabilityFrom(State seedState) {

        Set<HashableState> hashedStates = StateReachability.getReachableHashedStates(seedState, this.domain, this.hashingFactory);

        //initialize the value function for all states
        for (HashableState hs : hashedStates) {
            if (!this.valueFunction.containsKey(hs)) {
                this.valueFunction.put(hs, this.vinit.value(hs.s()));
            }
        }

    }

    public HashMap<HashableState, Double> getValueFunction() {
        return (HashMap<HashableState, Double>) valueFunction;
    }

    public static TetrisModel initialize(int depth) {
        SADomain domain = new SADomain();

        domain.addActionType(new TetrisActionType());
        domain.setModel(new TetrisPieceStateModel());

        double gamma = 0.99;

        HashableStateFactory hsf = new SimpleHashableStateFactory();
        //setup vi with 0.99 discount factor, a value
        //function initialization that initializes all states to value 0, and which will
        //run for 30 iterations over the state space
        TetrisModel tm = new TetrisModel(domain, 1, hsf,
                new ConstantValueFunction(0.0), depth);


        TetrisBoard tb = new TetrisBoard(10, 24);
        tb.setCurrentPiece(TetrisBoard.getRandomPiece());
        tm.currentState = new BoardState(tb);
        tm.p = tm.planFromState(tm.currentState);

        return tm;
    }

    public void getNextState() {
        Action a = p.action(this.currentState);
        currentState = ((TetrisTransitionAction) a).getBoardState();
    }
    public static void main(String[] args) throws IOException {

        TetrisModel tm = initialize(40000);
        Policy p = tm.p;

        //run planning from our initial state
        HashMap<HashableState, Double> valueFunction = tm.getValueFunction();
        BufferedWriter bfw = new BufferedWriter(new FileWriter("BoardInputs.txt", true));
        Action a;
        int i = 0;
        do {
            i++;
            /*
            a = p.action(tm.currentState);
            tm.currentState = ((TetrisTransitionAction) a).getBoardState();
            System.out.println(tm.currentState.getTetrisBoard());
            */
            tm.getNextState();
            // System.out.println(tm.currentState.getTetrisBoard());
            /*
            List<QValue> qValues = tm.qValues(tm.currentState);
            double value = -Double.MAX_VALUE;
            for (QValue qValue : qValues) {
                value = Math.max(value, qValue.q);
            }
            */
            // System.out.println(value);
            /*
            ArrayList<int[]> inputs = tm.currentState.getInputs();
            bfw.write(BoardInputs.flattenInputs(inputs, value - valueFunction.get(tm.hashingFactory.hashState(tm.currentState))));
            bfw.newLine();
            System.out.println("Value: " + (value - valueFunction.get(tm.hashingFactory.hashState(tm.currentState))));
             */
            if (tm.currentState.getTetrisBoard().maxHeight>20) {
                System.out.println("Iterations before Death: " + i);
                break;
            }
            System.out.println(i);
        } while (true);
        bfw.close();
    }

    public void cleanup() {
        valueFunction.clear();
        vinit = null;
    }

}
