package assignment;

import java.util.ArrayList;

public class DecCommand extends RegisterArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int r1 = args.get(0);
        int r1Value = critter.getReg(r1);

        r1Value--;
        critter.setReg(r1, r1Value);

        return currentLine + 1;
    }

}
