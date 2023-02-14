package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class IfEqCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(2, 2, "100", 100);
        testCommand(10, 10, "r10", 10);
        testCommand(-98172, -98172, "+10", 15);
        testCommand(983, 983, "-10", -5);
        testCommand(1, 5, "-30298", 6);
        testCommand(-92873, -3253263, "r10", 6);
        testCommand(-76, 1, "+10", 6);
        testCommand(999999, 888888, "-1", 6);
        testCommand(1, 1, "3872", 3872);
        testCommand(2189, -9372, "4", 6);
        testCommand(-93847, 2, "3", 6);
        testCommand(-38927, -38927, "100", 100);
    }

    //a and b are the two values returned by critter
    //n is the line change
    //nextLine is what the next line should be
    public void testCommand(int a, int b, String n, int nextLine) {
        Command command = new IfEqCommand();

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