package assignment;

import java.util.ArrayList;

public abstract class DoubleBearingJumpArgumentCommand extends JumpProcessorCommand {

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

        final Integer parsedFirstBearing = ArgumentInterpreter.interpretBearing(args.get(0));
        if (parsedFirstBearing == null) {
            return;
        }
        parsedArgs.add(parsedFirstBearing);

        final Integer parsedSecondBearing = ArgumentInterpreter.interpretBearing(args.get(1));
        if (parsedSecondBearing == null) {
            return;
        }
        parsedArgs.add(parsedSecondBearing);

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
