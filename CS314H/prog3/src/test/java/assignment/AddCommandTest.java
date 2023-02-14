package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AddCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(0, -5);
        testCommand(0, 5);
        testCommand(5, 0);
        testCommand(-5, 0);
        testCommand(48231, 4738);
        testCommand(-392871, 387463821);
        testCommand(21, -2);
        testCommand(0, 0);
    }

    public void testCommand(int a, int b) {
        Command command = new AddCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add("r1");
        args.add("r2");

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getReg(1)).thenReturn(a);
        when(c.getReg(2)).thenReturn(b);

        command.executeCommand(c, 0);

        verify(c).setReg(1, a + b);

    }
}