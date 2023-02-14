package assignment;

public abstract class JumpProcessorCommand extends Command {
    boolean isRelative;
    boolean isRegister;

    public int computeJump(Critter critter, int currentLine, int lineChange) {
        if (isRelative) {
            return currentLine + lineChange;
        } else if (isRegister) {
            return critter.getReg(lineChange);
        } else {
            return lineChange;
        }
    }
}
