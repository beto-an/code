package assignment;

import junit.framework.TestCase;

import java.util.ArrayList;

public class CommandProcessorTest extends TestCase {

    public void testProcessCommand() {

        String[] nullTest = {
                "", " ", "not a valid command", "i like pickles and sandwiches", "hop apple", "iflt purple tree",
                "iflt ", "infect trees", "go -021-9435787", "go +)(UJUHINbdsjkn", "go fjoierguhnlsrj", "go", "infect r2",
                "henry", "iflt 1", "ifally 45 10 10", "ifally        45 10 10", "write 3", "write r3", "write r3 10 10",
                "dec 1", "dec -1", "ifrandom", "ifrandom 201392 0293", "           ", "null", "add r1 r12"
        };

        for (String str:nullTest) {
            assertNull("processCommand does not return null",  CommandProcessor.processCommand(str));
        }

        String[] inputs = {
                "  hop   ",
                "infect 1",
                "infect",
                "go +1",
                "go -1",
                "go 1",
                "ifrandom 5",
                "ifempty 45     10",
                "ifangle 180 225 3",
                "write r3 2",
                "add r3 r6",
                "inc r7",
                "iflt r10 r1 4895"
        };

        int[][] outputs = {
                {},
                {1},
                {},
                {1},
                {-1},
                {1},
                {5},
                {45, 10},
                {180, 225, 3},
                {3, 2},
                {3, 6},
                {7},
                {10, 1, 4895}
        };

        for (int i = 0; i < inputs.length; i++) {

            Command command = CommandProcessor.processCommand(inputs[i]);
            assertEquals("command should not be null", false, command == null);

            ArrayList<Integer> args = command.getArgs();
            assertEquals("args are of different lengths", outputs[i].length, args.size());

            for (int j = 0; j < outputs[i].length; j++) {
                assertEquals("args have different values", outputs[i][j], args.get(j).intValue());
            }

        }

    }

    public void testFindCommand() {

        assertTrue("found wrong command", CommandProcessor.findCommand("hop") instanceof HopCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("left") instanceof LeftCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("right") instanceof RightCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("infect") instanceof InfectCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("eat") instanceof EatCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("go") instanceof GoCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifrandom") instanceof IfRandomCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifhungry") instanceof IfHungryCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifstarving") instanceof IfStarvingCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifempty") instanceof IfEmptyCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifally") instanceof IfAllyCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifenemy") instanceof IfEnemyCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifwall") instanceof IfWallCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifangle") instanceof IfAngleCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("write") instanceof WriteCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("add") instanceof AddCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("sub") instanceof SubCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("inc") instanceof IncCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("dec") instanceof DecCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("iflt") instanceof IfLTCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifeq") instanceof IfEqCommand);
        assertEquals("found wrong command", true, CommandProcessor.findCommand("ifgt") instanceof IfGTCommand);

        assertEquals("did not find null", null, CommandProcessor.findCommand("hello"));
        assertEquals("did not find null", null, CommandProcessor.findCommand("fkjhoq32iy8um948 9834yt pw3[2-0 "));
        assertEquals("did not find null", null, CommandProcessor.findCommand("10937289012 "));
        assertEquals("did not find null", null, CommandProcessor.findCommand(""));
        assertEquals("did not find null", null, CommandProcessor.findCommand(" "));
        assertEquals("did not find null", null, CommandProcessor.findCommand("hopp"));
        assertEquals("did not find null", null, CommandProcessor.findCommand("ifltwrite"));
        assertEquals("did not find null", null, CommandProcessor.findCommand("iflt write"));
        assertEquals("did not find null", null, CommandProcessor.findCommand("go eat"));

    }

}