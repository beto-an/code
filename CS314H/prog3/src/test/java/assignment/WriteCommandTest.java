package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class WriteCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(0);
        testCommand(1);
        testCommand(-1);
        testCommand(19483);
        testCommand(-8349);
        testCommand(23);
        testCommand(5);
    }

    public void testCommand(int a) {

        Command command = new WriteCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add("r1");
        args.add("" + a);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        command.executeCommand(c, 0);

        verify(c).setReg(1, a);

    }

}