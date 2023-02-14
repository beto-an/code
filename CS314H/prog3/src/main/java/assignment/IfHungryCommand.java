package assignment;

import java.util.ArrayList;

public class IfHungryCommand extends JumpArgumentCommand {

    public int executeCommand(Critter critter, int currentLine) {

        ArrayList<Integer> args = getArgs();

        Critter.HungerLevel hunger = critter.getHungerLevel();
        if (hunger == Critter.HungerLevel.STARVING || hunger == Critter.HungerLevel.HUNGRY) {
            int lineChange = args.get(0);
            return computeJump(critter, currentLine, lineChange);
        }

        return currentLine + 1;

    }

}
