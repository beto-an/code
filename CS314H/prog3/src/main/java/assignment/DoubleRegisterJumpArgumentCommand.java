package assignment;

import java.util.ArrayList;

public abstract class DoubleRegisterJumpArgumentCommand extends JumpProcessorCommand {
    public static final int minNumArgs = 3;

    public static final int maxNumArgs = 3;

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

        isRelative = ArgumentInterpreter.isRelative(args.get(2));
        isRegister = ArgumentInterpreter.isRegister(args.get(2));

        final Integer parsedJump = ArgumentInterpreter.interpretJump(args.get(2), isRegister);
        if (parsedJump == null) {
            return;
        }
        parsedArgs.add(parsedJump);

        super.args = parsedArgs;

    }
}
