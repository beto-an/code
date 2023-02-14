package assignment;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class RegisterArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"r2"},
                {},
                {"45", "90"},
                {"r5", "r2", "r1"},
                {"r"},
                {"r10"},
                {""},
                {"2 48oq234n hrp09 7y43q2"},
                {"r11", "r3"},
                {"r1", "r55"}
        };

        Integer[][] expectedOutputs = {
                {2},
                null,
                null,
                null,
                null,
                {10},
                null,
                null,
                null,
                null
        };

        for (int i = 0; i < inputs.length; i++) {

            RegisterArgumentCommand command = mock(RegisterArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
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

}