package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class IfRandomCommandTest extends TestCase {

    public void testExecuteCommand() {
        assertEquals("did not jump to correct line", testCommand(true, "123"), 123);
        assertEquals("did not jump to correct line", testCommand(true, "r10"), 10);
        assertEquals("did not jump to correct line", testCommand(true, "+10"), 15);
        assertEquals("did not jump to correct line", testCommand(true, "-10"), -5);

        assertEquals("did not jump to correct line", testCommand(false, "123"), 6);
        assertEquals("did not jump to correct line", testCommand(false, "r10"), 6);
        assertEquals("did not jump to correct line", testCommand(false, "+10"), 6);
        assertEquals("did not jump to correct line", testCommand(false, "-10"), 6);

        int jumpCount = 0;
        for (int i = 0; i < 100000; i++) {
            if (testCommand(Math.random() < 0.5, "10") == 10) jumpCount++;
        }

        assertEquals("randomized line jumping isn't random", 0.5, jumpCount / 100000.0, 0.01);
    }

    //a is the value returned by the critter method
    //n is the line change
    //return the line to jump to
    public int testCommand(boolean a, String n) {
        Command command = new IfRandomCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add(n);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.ifRandom()).thenReturn(a);
        when(c.getReg(10)).thenReturn(10);

        int line = command.executeCommand(c, 5);

        verify(c).ifRandom();

        return line;

    }

}