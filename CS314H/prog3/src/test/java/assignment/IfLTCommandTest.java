package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class IfLTCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(1, 2, "100", 100);
        testCommand(4, 1982, "r10", 10);
        testCommand(984, 985, "+10", 15);
        testCommand(-98372, -2, "-10", -5);
        testCommand(2, 1, "-30298", 6);
        testCommand(2, 1, "r10", 6);
        testCommand(2, 1, "+10", 6);
        testCommand(2, 1, "-10", 6);
        testCommand(1, 1, "3872", 6);
        testCommand(2189, -9372, "4", 6);
        testCommand(-93847, 2, "3", 3);
        testCommand(-38927, -38927, "100", 6);
    }

    //a and b are the two values returned by critter
    //n is the line change
    //nextLine is what the next line should be
    public void testCommand(int a, int b, String n, int nextLine) {
        Command command = new IfLTCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add("r1");
        args.add("r2");
        args.add(n);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getReg(1)).thenReturn(a);
        when(c.getReg(2)).thenReturn(b);

        when(c.getReg(10)).thenReturn(10);

        int line = command.executeCommand(c, 5);

        verify(c).getReg(1);
        verify(c).getReg(2);

        assertEquals("did not jump to correct line", line, nextLine);

    }

}