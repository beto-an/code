package assignment;

import java.util.ArrayList;

public class GoCommand extends JumpArgumentCommand {

    public static final int minNumArgs = 1;

    public static final int maxNumArgs = 1;

    public int getMinNumArgs() {
        return minNumArgs;
    }

    public int getMaxNumArgs() {
        return maxNumArgs;
    }

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int lineChange = args.get(0);

        return computeJump(critter, currentLine, lineChange);

    }

}
