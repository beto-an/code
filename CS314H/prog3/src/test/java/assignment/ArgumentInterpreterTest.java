package assignment;

import junit.framework.TestCase;

public class ArgumentInterpreterTest extends TestCase {

    public void testInterpretInteger() {

        String[] inputs = {
                "1", "1000000", "-1000", "0", "123", "-1", "2", "a", "", "ksdafjhoi83291l l,las",
                " ", "2147483647", "-2147483648", "1000000000000"
        };

        Integer[] results = {
                1, 1000000, -1000, 0, 123, -1, 2, null, null, null, null, 2147483647, -2147483648, null
        };

        for (int i = 0; i < inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretInteger(inputs[i]);
            assertEquals("interpretInteger failed", results[i], result);
        }

    }

    public void testInterpretIntegerWithOverflow() {

        String[] inputs = {
                "1", "1000000", "-1000", "0", "123", "-1", "2", "a", "", "ksdafjhoi83291l l,las",
                " ", "2147483647", "-2147483648"
        };

        Integer[] results = {
                1, 1000000, -1000, 0, 123, -1, 2, null, null, null, null, 2147483647, -2147483648
        };

        for (int i = 0; i < inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretIntegerWithOverflow(inputs[i]);
            assertEquals("interpreting regular integer failed", results[i], result);
        }

        String[] overflow_inputs = {
                "2147483648", "-2147483649", "10293892389237", "-38294791239112"
        };

        Integer[] overflow_results = {
                -2147483648, -2147483648, -2147483648, -2147483648
        };

        for (int i = 0; i < overflow_inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretIntegerWithOverflow(overflow_inputs[i]);
            assertEquals("interpreting overflow integer failed", overflow_results[i], result);
        }

    }

    public void testInterpretBearing() {

        String[] inputs = {
                "" + Critter.FRONT, "" + Critter.FRONT_LEFT, "" + Critter.FRONT_RIGHT, "" + Critter.LEFT,
                "" + Critter.REAR, "" + Critter.REAR_LEFT, "" + Critter.REAR_RIGHT, "" + Critter.RIGHT, "a", "",
                "ksdafjhoi83291l l,las", " ", "2147483647", "-2147483648", "1000000000000", "-1", "1", "100", "-100"
        };

        Integer[] results = {
                Critter.FRONT, Critter.FRONT_LEFT, Critter.FRONT_RIGHT, Critter.LEFT, Critter.REAR, Critter.REAR_LEFT,
                Critter.REAR_RIGHT, Critter.RIGHT, null, null, null, null, null, null, null, null, null, null, null
        };

        for (int i = 0; i < inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretBearing(inputs[i]);
            assertEquals("interpretBearing failed", results[i], result);
        }

    }

    public void testInterpretRegister() {

        for (int i = 1; i <= 10; i++) {
            assertEquals("interpretRegister failed for actual register", i, ArgumentInterpreter.interpretRegister("r" + i).intValue());
        }

        String[] inputs = {
                "r1000000", "r-1000", "r0", "r123", "-1", "r20", "ra", "", "ksdafjhoi83291l l,las",
                " ", "2147483647", "-2147483648", "1000000000000", "r", "r11", "r 1", "1r", "r+2"
        };

        for (int i = 0; i < inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretRegister(inputs[i]);
            assertEquals("interpretRegister failed for fake register", null, result);
        }

    }

    public void testInterpretJump() {

        for (int i = 1; i <= 10; i++) {
            assertEquals("interpretJump failed for actual register", i, ArgumentInterpreter.interpretJump("r" + i, true).intValue());
        }

        String[] fakeRegisterInputs = {
                "r1000000", "r-1000", "r0", "r123", "-1", "r20", "ra", "", "ksdafjhoi83291l l,las",
                " ", "2147483647", "-2147483648", "1000000000000", "r", "r11", "r 1", "1r", "r+2"
        };

        for (int i = 0; i < fakeRegisterInputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretJump(fakeRegisterInputs[i], true);
            assertEquals("interpretJump failed for fake register", null, result);
        }

        String[] inputs = {
                "1", "1000000", "-1000", "0", "123", "-1", "2", "a", "", "ksdafjhoi83291l l,las",
                " ", "2147483647", "-2147483648", "+-1", "+1", "-+1"
        };

        Integer[] results = {
                1, 1000000, -1000, 0, 123, -1, 2, null, null, null, null, 2147483647, -2147483648, null, 1, null
        };

        for (int i = 0; i < inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretJump(inputs[i], false);
            assertEquals("interpreting regular integer failed", results[i], result);
        }

        String[] overflow_inputs = {
                "2147483648", "-2147483649", "10293892389237", "-38294791239112"
        };

        Integer[] overflow_results = {
                -2147483648, -2147483648, -2147483648, -2147483648
        };

        for (int i = 0; i < overflow_inputs.length; i++) {
            Integer result = ArgumentInterpreter.interpretJump(overflow_inputs[i], false);
            assertEquals("interpreting overflow integer failed", overflow_results[i], result);
        }

    }

    public void testIsRelative() {

        String[] inputs = {
                "a", "", "ksdafjhoi83291l l,las", " ", "2147483647", "-2147483648", "1000000000000", "-1", "1", "100",
                "-100", "+1", "+0", "+37654362", "-0"
        };

        boolean[] results = {
                false, false, false, false, false, true, false, true, false, false, true, true, true, true, true
        };

        for (int i = 0; i < inputs.length; i++) {
            boolean result = ArgumentInterpreter.isRelative(inputs[i]);
            assertEquals("isRelative failed", results[i], result);
        }

    }

    public void testIsRegister() {

        for (int i = 1; i <= 10; i++) {
            assertTrue("isRegister failed for actual register", ArgumentInterpreter.isRegister("r" + i));
        }

        String[] trueInputs = {
                "r1000000", "r-1000", "r0", "r123", "r20", "ra", "r11", "r 1", "r+2"
        };

        for (int i = 0; i < trueInputs.length; i++) {
            assertTrue("isRegister failed for true input", ArgumentInterpreter.isRegister(trueInputs[i]));
        }

        String[] fakeInputs = {
                "1", "1000000", "-1000", "0", "123", "-1", "2", "a", "", "ksdafjhoi83291l l,las", " ", "2147483647",
                "-2147483648", "1000000000000"
        };

        for (int i = 0; i < fakeInputs.length; i++) {
            assertFalse("isRegister failed for fake register", ArgumentInterpreter.isRegister(fakeInputs[i]));
        }

    }

}