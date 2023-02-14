package assignment;

import java.util.ArrayList;

public class IfStarvingCommand extends JumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();

        if (critter.getHungerLevel() == Critter.HungerLevel.STARVING) {
            int lineChange = args.get(0);
            return computeJump(critter, currentLine, lineChange);
        }

        return currentLine + 1;

    }

}
