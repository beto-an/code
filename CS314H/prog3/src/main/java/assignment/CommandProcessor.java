package assignment;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CommandProcessor {


    public static Command processCommand(String args) {

        StringTokenizer st = new StringTokenizer(args);
        if (!st.hasMoreTokens()) return null;
        String commandName = st.nextToken();
        Command command = findCommand(commandName);
        if (command == null) return null;

        ArrayList<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        command.processTokens(tokens);
        if (command.isArgumentsInvalid()) return null;
        return command;
    }

    public static Command findCommand(String commandName) {
        Command command = null;
        switch(commandName) {
            case("hop"): command = new HopCommand(); break;
            case("left"): command = new LeftCommand(); break;
            case("right"): command = new RightCommand(); break;
            case("infect"): command = new InfectCommand(); break;
            case("eat"): command = new EatCommand(); break;
            case("go"): command = new GoCommand(); break;
            case("ifrandom"): command = new IfRandomCommand(); break;
            case("ifhungry"): command = new IfHungryCommand(); break;
            case("ifstarving"): command = new IfStarvingCommand(); break;
            case("ifempty"): command = new IfEmptyCommand(); break;
            case("ifally"): command = new IfAllyCommand(); break;
            case("ifenemy"): command = new IfEnemyCommand(); break;
            case("ifwall"): command = new IfWallCommand(); break;
            case("ifangle"): command = new IfAngleCommand(); break;
            case("write"): command = new WriteCommand(); break;
            case("add"): command = new AddCommand(); break;
            case("sub"): command = new SubCommand(); break;
            case("inc"): command = new IncCommand(); break;
            case("dec"): command = new DecCommand(); break;
            case("iflt"): command = new IfLTCommand(); break;
            case("ifeq"): command = new IfEqCommand(); break;
            case("ifgt"): command = new IfGTCommand(); break;
        }
        return command;
    }
}
