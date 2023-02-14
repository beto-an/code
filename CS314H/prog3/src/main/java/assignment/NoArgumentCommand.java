package assignment;

import java.util.ArrayList;

public abstract class NoArgumentCommand extends Command{
    public static final int minNumArgs = 0;

    public static final int maxNumArgs = 0;

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

        super.args = new ArrayList<Integer>();
    }

}
