package assignment;

import java.util.ArrayList;

public class InfectCommand extends Command {

    public static final int minNumArgs = 0;

    public static final int maxNumArgs = 1;

    public static final boolean endTurn = true;

    public boolean isTurnEnded() {
        return endTurn;
    }

    public int getMinNumArgs() {
        return minNumArgs;
    }

    public int getMaxNumArgs() {
        return maxNumArgs;
    }

    public void processTokens(ArrayList<String> args) {

        if (isArgumentSizeInvalid(args)) {
            return;
        }

        ArrayList<Integer> parsedArgs = new ArrayList<Integer>();

        if (args.size() > 0) {

            final Integer parsedInteger = ArgumentInterpreter.interpretIntegerWithOverflow(args.get(0));
            if (parsedInteger == null) {
                return;
            }
            parsedArgs.add(parsedInteger);

        }

        super.args = parsedArgs;

    }

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();

        if (args.size() == 0) critter.infect();
        else critter.infect(args.get(0));

        return currentLine + 1;

    }

}
