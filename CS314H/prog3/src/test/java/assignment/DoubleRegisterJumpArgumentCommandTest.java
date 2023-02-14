package assignment;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DoubleRegisterJumpArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"r2", "r1", "1"},
                {},
                {"r3", "r4"},
                {"r6", "r7", "9128", "33"},
                {"r8"},
                {"r10", "r1", "5"},
                {""},
                {"2 48oq234n hrp09 7y43q2"},
                {"r9", "r32", "4"},
                {"r3", "r2", "+832"},
                {"r3", "r2", "-38"},
                {"r3", "r2", "r3"},
                {"r3", "r2", "r1"},
                {"r3", "r2", "r11"},
                {"0", "29huenwfolsd"}
        };

        Integer[][] expectedOutputs = {
                {2, 1, 1},
                null,
                null,
                null,
                null,
                {10, 1, 5},
                null,
                null,
                null,
                {3, 2, 832},
                {3, 2, -38},
                {3, 2, 3},
                {3, 2, 1},
                null,
                null
        };

        boolean[] expectedRelative = {
                false, false, false, false, false, false, false, false, false, true, true, false, false, false, false
        };

        boolean[] expectedRegister = {
                false, false, false, false, false, false, false, false, false, false, false, true, true, true, true
        };

        for (int i = 0; i < inputs.length; i++) {

            DoubleRegisterJumpArgumentCommand command = mock(DoubleRegisterJumpArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
            ArrayList<String> input = new ArrayList<String>(List.of(inputs[i]));

            command.processTokens(input);
            ArrayList<Integer> output = command.getArgs();

            if (expectedOutputs[i] == null) {
                assertNull("output is not null for case " + i, output);
            } else {

                assertNotNull("output is null for case " + i, output);

                assertEquals("outputs are of different lengths for case " + i, expectedOutputs[i].length, output.size());

                assertEquals("calculated isRelative incorrectly for case " + i, expectedRelative[i], command.isRelative);
                assertEquals("calculated isRegister incorrectly for case " + i, expectedRegister[i], command.isRegister);

                for (int j = 0; j < expectedOutputs[i].length; j++) {
                    assertEquals("processed tokens incorrectly for case " + i, expectedOutputs[i][j], output.get(j));
                }

            }

        }

    }

}