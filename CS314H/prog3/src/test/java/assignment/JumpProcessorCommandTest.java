package assignment;

import junit.framework.TestCase;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JumpProcessorCommandTest extends TestCase {

    public void testComputeJump() {

        int[] currentLine = {
                1, 2, 3, 4, 5, 6, 7, 8, 10
        };

        int[] lineChange = {
                10, 8, 7, 4, 2, 0, -3, -2, 29138
        };

        boolean[] isRelative = {
                false, false, true, false, false, true, true, true, true
        };

        boolean[] isRegister = {
                false, false, false, true, true, false, false, false, false
        };

        int[] expectedLine = {
                10, 8, 10, 4, 2, 6, 4, 6, 29148
        };

        for (int i = 0; i < currentLine.length; i++) {

            JumpProcessorCommand processor = mock(JumpProcessorCommand.class, Mockito.CALLS_REAL_METHODS);
            processor.isRelative = isRelative[i];
            processor.isRegister = isRegister[i];

            Critter c = mock(Critter.class);
            when(c.getReg(lineChange[i])).thenReturn(lineChange[i]);

            int line = processor.computeJump(c, currentLine[i], lineChange[i]);

            assertEquals("processed line incorrectly for case " + i, expectedLine[i], line);

        }

    }

}