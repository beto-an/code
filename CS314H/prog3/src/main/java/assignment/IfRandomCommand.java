package assignment;

import java.util.ArrayList;

public class IfRandomCommand extends JumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();

        if (critter.ifRandom()) {
            int lineChange = args.get(0);
            return computeJump(critter, currentLine, lineChange);
        }

        return currentLine + 1;

    }

}
