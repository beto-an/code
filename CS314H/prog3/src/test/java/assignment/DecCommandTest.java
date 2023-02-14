package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class DecCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(0);
        testCommand(1);
        testCommand(5);
        testCommand(-5);
        testCommand(48231);
        testCommand(-392871);
        testCommand(21);
        testCommand(27);
    }

    public void testCommand(int a) {
        Command command = new DecCommand();

        ArrayList<String> args = new ArrayList<>();
        args.add("r1");

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getReg(1)).thenReturn(a);


        command.executeCommand(c, 0);


        verify(c).setReg(1, a - 1);

    }
}