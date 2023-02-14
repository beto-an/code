package assignment;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class RegisterIntegerArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"r2", "1"},
                {},
                {"r3", "r4"},
                {"r6", "r7", "9128"},
                {"r8"},
                {"r10", "5"},
                {""},
                {"2 48oq234n hrp09 7y43q2"},
                {"r9", "r32", "4"},
                {"r3", "+832"},
                {"r3", "-38"},
                {"r11", "421"},
                {"r0", "234"}
        };

        Integer[][] expectedOutputs = {
                {2, 1},
                null,
                null,
                null,
                null,
                {10, 5},
                null,
                null,
                null,
                {3, 832},
                {3, -38},
                null,
                null
        };

        for (int i = 0; i < inputs.length; i++) {

            RegisterIntegerArgumentCommand command = mock(RegisterIntegerArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
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