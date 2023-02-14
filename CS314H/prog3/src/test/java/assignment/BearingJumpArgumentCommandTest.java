package assignment;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class BearingJumpArgumentCommandTest extends TestCase {

    public void testProcessTokens() {

        String[][] inputs = {
                {"45", "0"},
                {},
                {"45"},
                {"45", "22", "420"},
                {"22", "22", "420"},
                {"315", "55"},
                {""},
                {"2 48oq234n hrp09 7y43q2"},
                {"1", "3"},
                {"180", "+832"},
                {"225", "-38"},
                {"270", "r3"},
                {"135", "r1"},
                {"45", "r11"},
                {"0", "29huenwfolsd"}
        };

        Integer[][] expectedOutputs = {
                {45, 0},
                null,
                null,
                null,
                null,
                {315, 55},
                null,
                null,
                null,
                {180, 832},
                {225, -38},
                {270, 3},
                {135, 1},
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

            BearingJumpArgumentCommand command = mock(BearingJumpArgumentCommand.class, Mockito.CALLS_REAL_METHODS);
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