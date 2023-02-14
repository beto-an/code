package assignment;

import java.util.ArrayList;

public class SubCommand extends DoubleRegisterArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int r1 = args.get(0);
        int r2 = args.get(1);
        int r1Value = critter.getReg(r1);
        int r2Value = critter.getReg(r2);

        r1Value -= r2Value;
        critter.setReg(r1, r1Value);

        return currentLine + 1;
    }

}
