package assignment;

import java.util.ArrayList;

public class IfAngleCommand extends DoubleBearingJumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int b1 = args.get(0);
        int b2 = args.get(1);

        if (critter.getOffAngle(b1) == b2) {
            int lineChange = args.get(2);
            return computeJump(critter, currentLine, lineChange);
        }

        return currentLine + 1;

    }

}
