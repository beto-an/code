package assignment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Command {
    ArrayList<Integer> args;

    public static final boolean endTurn = false;

    public Command() { }

    public ArrayList<Integer> getArgs() {
        return args;
    }


    public abstract void processTokens(ArrayList<String> args);

    public boolean isArgumentsInvalid() {
        return args == null;
    }

    public abstract int getMinNumArgs();

    public abstract int getMaxNumArgs();

    public boolean isTurnEnded() {
        return endTurn;
    }

    public boolean isArgumentSizeInvalid(ArrayList<String> args) {
        return args.size() < getMinNumArgs() || args.size() > getMaxNumArgs();
    }

    public abstract int executeCommand(Critter critter, int currentLine);

}
