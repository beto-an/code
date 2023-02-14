package assignment;

import java.util.ArrayList;

public class WriteCommand extends RegisterIntegerArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();
        int r1 = args.get(0);

        critter.setReg(r1, args.get(1));

        return currentLine + 1;
    }

}
