package assignment;

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InfectCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"1"},
                {},
                {"29831"},
                {"-821"},
                {"0"},
                {"2987364378238"},
                {""},
                {"0483jiroenfd"},
                {"219", "92"}
        };

        Integer[][] expectedOutputs = {
                {1},
                {},
                {29831},
                {-821},
                {0},
                {Integer.MIN_VALUE},
                null,
                null,
                null
        };

        for (int i = 0; i < inputs.length; i++) {

            Command command = new InfectCommand();
            ArrayList<String> input = new ArrayList<String>(List.of(inputs[i]));

            command.processTokens(input);
            ArrayList<Integer> output = command.getArgs();

            if (expectedOutputs[i] == null) {
                assertNull("output is not null for case " + i, output);
            } else {

                assertNotNull("output is null for case " + i, output);

                assertEquals("outputs are of different lengths for case " + i, expectedOutputs[i].length, output.size());

                for (int j = 0; j < expectedOutputs[i].length; j++) {
                    assertEquals("processed tokens incorrectly for case " + i, expectedOutputs[i][j], output.get(j));
                }

            }

        }

    }

    public void testExecuteCommand() {
        testCommand(null);
        testCommand(1);
        testCommand(-2382);
        testCommand(92387823);
    }

    //n is the line number, null if no args
    public void testCommand(Integer n) {

        Command command = new InfectCommand();

        ArrayList<String> args = new ArrayList<>();

        if (n != null) args.add(n.toString());

        command.processTokens(args);

        Critter c = mock(Critter.class);

        command.executeCommand(c, 0);

        if (n == null) verify(c).infect();
        else verify(c).infect(n.intValue());

    }

}