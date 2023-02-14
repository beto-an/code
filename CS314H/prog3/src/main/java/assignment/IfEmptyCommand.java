package assignment;

import java.util.ArrayList;

public class IfEmptyCommand extends BearingJumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int b1 = args.get(0);

        if (critter.getCellContent(b1) == Critter.EMPTY) {
            int lineChange = args.get(1);
            return computeJump(critter, currentLine, lineChange);
        }

        return currentLine + 1;

    }

}
