package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class IfAngleCommandTest extends TestCase {

    public void testExecuteCommand() {
        testCommand(0, 0, "234", 234);
        testCommand(45, 45, "r10", 10);
        testCommand(135, 135, "+10", 15);
        testCommand(180, 180, "-10", -5);
        testCommand(0, 45, "234", 6);
        testCommand(90, 45, "r10", 6);
        testCommand(135, 270, "+10", 6);
        testCommand(180, 315, "-10", 6);

    }

    //b1 is the value returned by critter method
    //b2 is the bearing to match
    //n is the line change
    //nextLine is what the next line should be
    public void testCommand(int b1, int b2, String n, int nextLine) {
        Command command = new IfAngleCommand();

        ArrayList<String> args = new ArrayList<>();

        args.add("0");
        args.add("" + b2);
        args.add(n);

        command.processTokens(args);

        Critter c = mock(Critter.class);

        when(c.getOffAngle(0)).thenReturn(b1);
        when(c.getReg(10)).thenReturn(10);

        int line = command.executeCommand(c, 5);

        assertEquals("jumped to wrong line", nextLine, line);

        verify(c).getOffAngle(0);

    }

}