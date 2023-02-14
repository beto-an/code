package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class IfStarvingCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(Critter.HungerLevel.HUNGRY, "24445", 6);
        testCommand(Critter.HungerLevel.HUNGRY, "r10", 6);
        testCommand(Critter.HungerLevel.HUNGRY, "+10", 6);
        testCommand(Critter.HungerLevel.HUNGRY, "-10", 6);

        testCommand(Critter.HungerLevel.STARVING, "24445", 24445);
        testCommand(Critter.HungerLevel.STARVING, "r10", 10);
        testCommand(Critter.HungerLevel.STARVING, "+10", 15);
        testCommand(Critter.HungerLevel.STARVING, "-10", -5);

        testCommand(Critter.HungerLevel.SATISFIED, "24445", 6);
        testCommand(Critter.HungerLevel.SATISFIED, "r10", 6);
        testCommand(Critter.HungerLevel.SATISFIED, "+10", 6);
        testCommand(Critter.HungerLevel.SATISFIED, "-10", 6);

    }

    //a is the value returned by the critter method
    //n is the line change
    //nextLine is what the next line should be
    public void testCommand(Critter.HungerLevel a, String n, int nextLine) {
        Command command = new IfStarvingCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add(n);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getHungerLevel()).thenReturn(a);
        when(c.getReg(10)).thenReturn(10);

        int line = command.executeCommand(c, 5);

        assertEquals("jumped to wrong line", nextLine, line);

        verify(c).getHungerLevel();

    }

}