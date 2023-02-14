package assignment;

import java.util.ArrayList;

public class IfGTCommand extends DoubleRegisterJumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int r1 = args.get(0);
        int r2 = args.get(1);
        int r1Value = critter.getReg(r1);
        int r2Value = critter.getReg(r2);

        if (r1Value > r2Value) {
            int lineChange = args.get(2);
            return computeJump(critter, currentLine, lineChange);
        }
        else return currentLine + 1;
    }

}
