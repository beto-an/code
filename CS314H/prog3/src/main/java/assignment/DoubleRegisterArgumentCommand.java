package assignment;

import java.util.ArrayList;

public abstract class DoubleRegisterArgumentCommand extends Command {

    public static final int minNumArgs = 2;

    public static final int maxNumArgs = 2;

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

        final Integer parsedFirstRegister = ArgumentInterpreter.interpretRegister(args.get(0));
        if (parsedFirstRegister == null) {
            return;
        }
        parsedArgs.add(parsedFirstRegister);

        final Integer parsedSecondRegister = ArgumentInterpreter.interpretRegister(args.get(1));
        if (parsedSecondRegister == null) {
            return;
        }
        parsedArgs.add(parsedSecondRegister);

        super.args = parsedArgs;

    }
}
