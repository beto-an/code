package assignment;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DoubleBearingJumpArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"45", "0", "1"},
                {},
                {"45", "90"},
                {"45", "315", "420", "485"},
                {"225"},
                {"315", "45", "5"},
                {""},
                {"2 48oq234n hrp09 7y43q2"},
                {"1", "3", "4"},
                {"180", "180", "+832"},
                {"225", "90", "-38"},
                {"270", "0", "r3"},
                {"135", "135", "r1"},
                {"45", "r11"},
                {"0", "29huenwfolsd"}
        };

        Integer[][] expectedOutputs = {
                {45, 0, 1},
                null,
                null,
                null,
                null,
                {315, 45, 5},
                null,
                null,
                null,
                {180, 180, 832},
                {225, 90, -38},
                {270, 0, 3},
                {135, 135, 1},
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

            DoubleBearingJumpArgumentCommand command = mock(DoubleBearingJumpArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
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