package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class GoCommandTest extends TestCase {

    public void testExecuteCommand() {

        testCommand("24445", 24445);
        testCommand("r10", 10);
        testCommand("+10", 15);
        testCommand("-10", -5);

    }

    //n is the line change
    //nextLine is what the next line should be
    public void testCommand(String n, int nextLine) {
        Command command = new GoCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add(n);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getReg(10)).thenReturn(10);

        int line = command.executeCommand(c, 5);

        assertEquals("jumped to wrong line", nextLine, line);

    }

}