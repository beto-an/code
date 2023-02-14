package assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Responsible for loading critter species from text files and interpreting the
 * simple Critter language.
 * 
 * For more information on the purpose of the below two methods, see the
 * included API/ folder and the project description.
 */
public class Interpreter implements CritterInterpreter {

	//Execute critter behavior code from last stopped position
	public void executeCritter(Critter c) {

		if (c == null) return;

		int currCommandLine = c.getNextCodeLine();
		List<Command> commandList = c.getCode();

		if (!isCommandLineValid(commandList, currCommandLine)) return;
		Command currCommand;

		do {

			currCommand = commandList.get(currCommandLine - 1);
			currCommandLine = currCommand.executeCommand(c, currCommandLine);
			if (!isCommandLineValid(commandList, currCommandLine)) {
				c.setNextCodeLine(currCommandLine);
				return;
			}

		} while (!currCommand.isTurnEnded());

		c.setNextCodeLine(currCommandLine);

	}

	public static boolean isCommandLineValid(List<Command> c, int nextCommandLine) {
		return nextCommandLine > 0 && nextCommandLine <= c.size();
	}


	//Load critter name and behavior code from file, store in CritterSpecies object
	public CritterSpecies loadSpecies(String filename) throws IOException {

		if (filename == null) return null;

		//Verify input file exists and is readable
		File inputFile = new File(filename);
		if (!Files.isReadable(inputFile.toPath())) {
			return null;
		}
		BufferedReader in = new BufferedReader(new FileReader(inputFile));

		//Read in the name of the critter (the first line)
		String name = "";
		if (in.ready()) {
			name = in.readLine();
		}

		//Read in and store the instructions of the critter in an Instruction ArrayList
		ArrayList<Command> commandList = new ArrayList<>();
		while (in.ready()) {

			String line = in.readLine();

			//Blank line denotes end of behavior code
			StringTokenizer st = new StringTokenizer(line);
			if (!st.hasMoreTokens()) break;

			//Try to create a new command
			Command command = CommandProcessor.processCommand(line);
			if (command == null) {
				return null;
			}
			commandList.add(command);

		}

		//Construct and return the CritterSpecies object
		CritterSpecies critter = new CritterSpecies(name, commandList);
		return critter;
	}


}
