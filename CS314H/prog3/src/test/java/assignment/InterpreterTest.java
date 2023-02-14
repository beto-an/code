package assignment;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class InterpreterTest extends TestCase {

    public void testActionCommands() {

        Interpreter in = new Interpreter();
        CritterSpecies cSpecies;
        try {
            cSpecies = in.loadSpecies("./testSpecies/ActionCommands.cri");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Critter c = mock(Critter.class);
        when(c.getCode()).thenReturn(cSpecies.getCode());

        when(c.getNextCodeLine()).thenReturn(1);
        in.executeCritter(c);
        verify(c).setNextCodeLine(2);
        verify(c).hop();

        when(c.getNextCodeLine()).thenReturn(2);
        in.executeCritter(c);
        verify(c).setNextCodeLine(3);
        verify(c).eat();

        when(c.getNextCodeLine()).thenReturn(3);
        in.executeCritter(c);
        verify(c).setNextCodeLine(4);
        verify(c).left();

        when(c.getNextCodeLine()).thenReturn(4);
        in.executeCritter(c);
        verify(c).setNextCodeLine(5);
        verify(c).right();

        when(c.getNextCodeLine()).thenReturn(5);
        in.executeCritter(c);
        verify(c).setNextCodeLine(6);
        verify(c).infect();

        when(c.getNextCodeLine()).thenReturn(6);
        in.executeCritter(c);
        verify(c).setNextCodeLine(7);
        verify(c).infect(0);

    }

    public void testLoadSpecies() {

        Interpreter in = new Interpreter();

        try {

            assertNull("did not return null", in.loadSpecies(null));
            assertNull("did not return null", in.loadSpecies("cnjrhuyh89h32"));
            assertNull("did not return null", in.loadSpecies("./testSpecies/BadCommand.cri"));
            assertNull("did not return null", in.loadSpecies("./testSpecies/NoBlankBeforeComments.cri"));

            CritterSpecies c1 = in.loadSpecies("./testSpecies/BlankFile.cri");
            assertEquals("did not read species properly", 0, c1.getCode().size());
            assertEquals("did not read species properly", "", c1.getName());

            CritterSpecies c2 = in.loadSpecies("./testSpecies/BlankName.cri");
            assertEquals("did not read species properly", 1, c2.getCode().size());
            assertEquals("did not read species properly", "", c2.getName());

            CritterSpecies c3 = in.loadSpecies("./testSpecies/NoBlankLine.cri");
            assertEquals("did not read species properly", 1, c3.getCode().size());
            assertEquals("did not read species properly", "test", c3.getName());

            CritterSpecies c4 = in.loadSpecies("./testSpecies/AllCommands.cri");
            List<Command> listCommands = c4.getCode();
            assertTrue("wrong command", listCommands.get(0) instanceof HopCommand);
            assertTrue("wrong command", listCommands.get(1) instanceof LeftCommand);
            assertTrue("wrong command", listCommands.get(2) instanceof RightCommand);
            assertTrue("wrong command", listCommands.get(3) instanceof InfectCommand);
            assertTrue("wrong command", listCommands.get(4) instanceof InfectCommand);
            assertTrue("wrong command", listCommands.get(5) instanceof EatCommand);
            assertTrue("wrong command", listCommands.get(6) instanceof GoCommand);
            assertTrue("wrong command", listCommands.get(7) instanceof GoCommand);
            assertTrue("wrong command", listCommands.get(8) instanceof GoCommand);
            assertTrue("wrong command", listCommands.get(9) instanceof GoCommand);
            assertTrue("wrong command", listCommands.get(10) instanceof IfRandomCommand);
            assertTrue("wrong command", listCommands.get(11) instanceof IfRandomCommand);
            assertTrue("wrong command", listCommands.get(12) instanceof IfRandomCommand);
            assertTrue("wrong command", listCommands.get(13) instanceof IfRandomCommand);
            assertTrue("wrong command", listCommands.get(14) instanceof IfHungryCommand);
            assertTrue("wrong command", listCommands.get(15) instanceof IfHungryCommand);
            assertTrue("wrong command", listCommands.get(16) instanceof IfHungryCommand);
            assertTrue("wrong command", listCommands.get(17) instanceof IfHungryCommand);
            assertTrue("wrong command", listCommands.get(18) instanceof IfStarvingCommand);
            assertTrue("wrong command", listCommands.get(19) instanceof IfStarvingCommand);
            assertTrue("wrong command", listCommands.get(20) instanceof IfStarvingCommand);
            assertTrue("wrong command", listCommands.get(21) instanceof IfStarvingCommand);
            assertTrue("wrong command", listCommands.get(22) instanceof IfEmptyCommand);
            assertTrue("wrong command", listCommands.get(23) instanceof IfEmptyCommand);
            assertTrue("wrong command", listCommands.get(24) instanceof IfEmptyCommand);
            assertTrue("wrong command", listCommands.get(25) instanceof IfEmptyCommand);
            assertTrue("wrong command", listCommands.get(26) instanceof IfAllyCommand);
            assertTrue("wrong command", listCommands.get(27) instanceof IfAllyCommand);
            assertTrue("wrong command", listCommands.get(28) instanceof IfAllyCommand);
            assertTrue("wrong command", listCommands.get(29) instanceof IfAllyCommand);
            assertTrue("wrong command", listCommands.get(30) instanceof IfEnemyCommand);
            assertTrue("wrong command", listCommands.get(31) instanceof IfEnemyCommand);
            assertTrue("wrong command", listCommands.get(32) instanceof IfEnemyCommand);
            assertTrue("wrong command", listCommands.get(33) instanceof IfEnemyCommand);
            assertTrue("wrong command", listCommands.get(34) instanceof IfWallCommand);
            assertTrue("wrong command", listCommands.get(35) instanceof IfWallCommand);
            assertTrue("wrong command", listCommands.get(36) instanceof IfWallCommand);
            assertTrue("wrong command", listCommands.get(37) instanceof IfWallCommand);
            assertTrue("wrong command", listCommands.get(38) instanceof IfAngleCommand);
            assertTrue("wrong command", listCommands.get(39) instanceof IfAngleCommand);
            assertTrue("wrong command", listCommands.get(40) instanceof IfAngleCommand);
            assertTrue("wrong command", listCommands.get(41) instanceof IfAngleCommand);
            assertTrue("wrong command", listCommands.get(42) instanceof WriteCommand);
            assertTrue("wrong command", listCommands.get(43) instanceof AddCommand);
            assertTrue("wrong command", listCommands.get(44) instanceof SubCommand);
            assertTrue("wrong command", listCommands.get(45) instanceof IncCommand);
            assertTrue("wrong command", listCommands.get(46) instanceof DecCommand);
            assertTrue("wrong command", listCommands.get(47) instanceof IfLTCommand);
            assertTrue("wrong command", listCommands.get(48) instanceof IfLTCommand);
            assertTrue("wrong command", listCommands.get(49) instanceof IfLTCommand);
            assertTrue("wrong command", listCommands.get(50) instanceof IfLTCommand);
            assertTrue("wrong command", listCommands.get(51) instanceof IfEqCommand);
            assertTrue("wrong command", listCommands.get(52) instanceof IfEqCommand);
            assertTrue("wrong command", listCommands.get(53) instanceof IfEqCommand);
            assertTrue("wrong command", listCommands.get(54) instanceof IfEqCommand);
            assertTrue("wrong command", listCommands.get(55) instanceof IfGTCommand);
            assertTrue("wrong command", listCommands.get(56) instanceof IfGTCommand);
            assertTrue("wrong command", listCommands.get(57) instanceof IfGTCommand);
            assertTrue("wrong command", listCommands.get(58) instanceof IfGTCommand);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void testIsCommandLineValid() {

        List<Command> c = new ArrayList<Command>();
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, -1));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 0));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 1));

        c.add(null);
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, -1));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 0));
        assertTrue("size not validated correctly", Interpreter.isCommandLineValid(c, 1));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 2));

        c.add(null);
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, -1));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 0));
        assertTrue("size not validated correctly", Interpreter.isCommandLineValid(c, 1));
        assertTrue("size not validated correctly", Interpreter.isCommandLineValid(c, 2));
        assertFalse("size not validated correctly", Interpreter.isCommandLineValid(c, 3));

    }

}

