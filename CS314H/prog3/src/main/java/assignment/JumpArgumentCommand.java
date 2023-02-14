package assignment;

import java.util.ArrayList;

public abstract class JumpArgumentCommand extends JumpProcessorCommand {
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

        isRelative = ArgumentInterpreter.isRelative(args.get(0));
        isRegister = ArgumentInterpreter.isRegister(args.get(0));

        final Integer parsedJump = ArgumentInterpreter.interpretJump(args.get(0), isRegister);
        if (parsedJump == null) {
            return;
        }
        parsedArgs.add(parsedJump);

        super.args = parsedArgs;

    }
}
