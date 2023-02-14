package assignment;

import java.util.ArrayList;

public abstract class BearingJumpArgumentCommand extends JumpProcessorCommand {

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

        final Integer parsedBearing = ArgumentInterpreter.interpretBearing(args.get(0));
        if (parsedBearing == null) {
            return;
        }
        parsedArgs.add(parsedBearing);

        isRelative = ArgumentInterpreter.isRelative(args.get(1));
        isRegister = ArgumentInterpreter.isRegister(args.get(1));

        final Integer parsedJump = ArgumentInterpreter.interpretJump(args.get(1), isRegister);
        if (parsedJump == null) {
            return;
        }
        parsedArgs.add(parsedJump);

        super.args = parsedArgs;

    }
}
