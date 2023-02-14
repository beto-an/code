package assignment;

import java.util.ArrayList;

public abstract class RegisterArgumentCommand extends Command {

    public static final int minNumArgs = 1;

    public static final int maxNumArgs = 1;

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

        super.args = parsedArgs;

    }
}
