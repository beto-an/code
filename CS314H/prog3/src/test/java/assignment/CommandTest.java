package assignment;

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest extends TestCase {

    public void testIsArgumentSizeInvalid() {

        Command c = mock(Command.class, Mockito.CALLS_REAL_METHODS);
        when(c.getMinNumArgs()).thenReturn(4);
        when(c.getMaxNumArgs()).thenReturn(4);

        ArrayList<String> args = new ArrayList<>();

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add("");
        args.add("9823 jnf iw;j'");
        args.add("-89347y");

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add(" ");

        assertFalse("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add("-o0fper");

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        when(c.getMinNumArgs()).thenReturn(0);
        when(c.getMaxNumArgs()).thenReturn(0);

        args = new ArrayList<>();

        assertFalse("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add("");

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add(" ");

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        when(c.getMinNumArgs()).thenReturn(1);
        when(c.getMaxNumArgs()).thenReturn(2);

        args = new ArrayList<>();

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add("");

        assertFalse("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add(" ");

        assertFalse("argument size is not properly checked", c.isArgumentSizeInvalid(args));

        args.add(" ");

        assertTrue("argument size is not properly checked", c.isArgumentSizeInvalid(args));



    }

}