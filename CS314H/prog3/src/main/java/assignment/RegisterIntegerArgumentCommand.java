package assignment;

import java.util.ArrayList;

public abstract class RegisterIntegerArgumentCommand extends Command {

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

        final Integer parsedRegister = ArgumentInterpreter.interpretRegister(args.get(0));
        if (parsedRegister == null) {
            return;
        }
        parsedArgs.add(parsedRegister);

        final Integer parsedInteger = ArgumentInterpreter.interpretInteger(args.get(1));
        if (parsedInteger == null) {
            return;
        }
        parsedArgs.add(parsedInteger);

        super.args = parsedArgs;

    }
}
