package assignment;

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class JumpArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"45"},
                {},
                {""},
                {"45", "22", "420"},
                {"22", "22", "420"},
                {"280"},
                {"3938"},
                {"2 48oq234n hrp09 7y43q2"},
                {"1", "3"},
                {"+832"},
                {"-38"},
                {"r3"},
                {"r1"},
                {"r11"},
                {"29huenwfolsd"}
        };

        Integer[][] expectedOutputs = {
                {45},
                null,
                null,
                null,
                null,
                {280},
                {3938},
                null,
                null,
                {832},
                {-38},
                {3},
                {1},
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

            JumpArgumentCommand command = mock(JumpArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
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