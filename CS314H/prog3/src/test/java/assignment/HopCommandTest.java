package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HopCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand();
    }

    public void testCommand() {

        Command command = new HopCommand();

        ArrayList<String> args = new ArrayList<>();

        command.processTokens(args);

        Critter c = mock(Critter.class);

        command.executeCommand(c, 0);

        verify(c).hop();
    }
}