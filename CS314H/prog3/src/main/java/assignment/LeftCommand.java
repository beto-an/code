package assignment;

public class LeftCommand extends NoArgumentCommand {

    public static final boolean endTurn = true;

    public boolean isTurnEnded() {
        return endTurn;
    }

    public int executeCommand(Critter critter, int currentLine) {

        critter.left();

        return currentLine + 1;

    }

}
