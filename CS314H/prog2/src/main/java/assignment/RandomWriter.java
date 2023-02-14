package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


/*
 * TextProcessor which generates strings based on training input.
 */
public class RandomWriter implements TextProcessor {

    /**
     * Maximum number of characters per line in output.
     */
    private static final int CPL = 66;

    /**
     * Power used for computing hashes. Preferably a prime number
     */
    private static final int HASH_POWER = 31;

    /**
     * (Pseudo)Random Generator Stream used generating random indices and seeds.
     */

    private final Random rng;

    /**
     * HashMap that contains all possible next character (positions) for every possible seed.
     */
    private final HashMap<SeedIndex, ArrayList<Integer>> seedNextChar;

    /**
     * Level of analysis i.e. length of seed.
     */
    private final int level;

    /**
     * Input string used for model training.
     */
    private final StringBuilder trainingString;

    /**
     * Output string generated from model.
     */
    private final StringBuilder outputString;

    /**
     * Stores powers of HASH_POWER for compute running hash.
     */
    private final int[] powers;


    /**
     * Stores the hashes when precomputed.
     */
    private int[] hashes;

    /**
     * Maximal Index for any seed.
     */
    private int indexRange;


    /**
     * Private Constructor for RandomWriter.
     *
     * @param level The length of the seeds considered.
     */
    private RandomWriter(int level) {

        rng = new Random();
        seedNextChar = new HashMap<>();
        this.level = level;
        trainingString = new StringBuilder();
        outputString = new StringBuilder();

        powers = new int[this.level];
        if (level == 0) {
            return;
        }
        powers[0] = 1;
        for (int i = 1; i < level; i++) {
            powers[i] = powers[i - 1] * HASH_POWER;
        }
    }

    /**
     * Factory Constructor for RandomWriter.
     *
     * @param level The length of the seeds considered.
     * @return A RandomWriter instance with the given level.
     */
    public static TextProcessor createProcessor(int level) {
        return new RandomWriter(level);
    }

    /**
     * Reads the input file text and precomputes hashes.
     *
     * @param inputFilename Source text file containing training strings.
     * @throws IOException if file input is not readable
     */
    public void readText(String inputFilename) throws IOException {
        readInFile(inputFilename);
        if (level == 0) {
            return;
        }
        instantiateConstants();
        constructHashMap();
    }

    /**
     * @param outputFilename Destination text file to write generated text.
     * @param length         Length of text to generate (non-negative).
     * @throws IOException if file output is not writeable
     */
    public void writeText(String outputFilename, int length) throws IOException {
        if (length == 0) {
            BufferedWriter bfw = new BufferedWriter(new FileWriter((outputFilename)));
            bfw.close();
        }
        if (level == 0) {
            for (int i = 0; i < length; i++) {
                outputString.append(trainingString.charAt(rng.nextInt(trainingString.length())));
            }
        } else {
            SeedIndex currentPosition = initializeRandomSeed();
            while (outputString.length() < length) {
                int nextCharPosition = appendNextChar(currentPosition);
                if (isLastSeed(nextCharPosition)) {
                    currentPosition = initializeRandomSeed();
                } else {
                    currentPosition = new SeedIndex(nextCharPosition);
                }
            }
        }
        outputString.delete(length, outputString.length());
        writeOutput(outputFilename, outputString.toString());
    }

    /**
     * Reads in source file as a StringBuilder.
     *
     * @param inputFilename Source text file containing training strings.
     * @throws IOException if file input is not readable
     */
    protected void readInFile(String inputFilename) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(inputFilename));
        while (bfr.ready()) {
            trainingString.append(bfr.readLine());
            if (trainingString.charAt(trainingString.length() - 1) != ' ') {
                trainingString.append(" ");
            }
        }
        bfr.close();
    }

    /**
     * Instantiates relevant constants used within the class.
     */
    protected void instantiateConstants() {
        indexRange = trainingString.length() - level;
        hashes = new int[indexRange + 1];
    }

    /**
     * Constructs a HashMap which tracks each seed's next character.
     */
    protected void constructHashMap() {
        for (int i = 0; i < indexRange; i++) {
            computeHash(i);
            SeedIndex currentPosition = new SeedIndex(i);
            ArrayList<Integer> currentNextChar = seedNextChar.get(currentPosition);
            if (currentNextChar == null) {
                currentNextChar = new ArrayList<>();
                seedNextChar.put(currentPosition, currentNextChar);
            }
            currentNextChar.add(i + 1);
        }
        computeHash(indexRange);
    }

    /**
     * Computes a rolling hash at the given position.
     *
     * @param position SeedIndex location to compute hash.
     */
    protected void computeHash(final int position) {
        if (position == 0) {
            hashes[0] = 0;
            for (int i = 0; i < level; i++) {
                hashes[0] += powers[level - 1 - i] * trainingString.charAt(i);
            }
        } else {
            hashes[position] = (hashes[position - 1] - trainingString.charAt(position - 1) * powers[level - 1]) * HASH_POWER
                    + trainingString.charAt(position + level - 1);
        }
    }

    /**
     * Writes and formats the generated text to the output file.
     *
     * @param outputFilename Destination text file to write generated text.
     * @param output         output string.
     * @throws IOException if file output is not writeable
     */
    protected void writeOutput(String outputFilename, String output) throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter(outputFilename));
        int position = 0;
        while (position < output.length()) {
            final int nextLineCharCount = Math.min(CPL, output.length() - position);
            bfw.write(output, position, nextLineCharCount);
            bfw.newLine();
            position += nextLineCharCount;
        }
        bfw.close();
    }

    /**
     * Appends a random seed to the string output and continues generating from this seed.
     *
     * @return Randomly chosen SeedIndex.
     */

    protected SeedIndex initializeRandomSeed() {
        int randomIndex = rng.nextInt(indexRange + 1);
        while (isLastSeed(randomIndex)) {
            randomIndex = rng.nextInt(indexRange + 1);
        }
        final SeedIndex randomSeed = new SeedIndex(randomIndex);
        outputString.append(randomSeed.getSubstring()); // Concatenate initial seed
        return randomSeed;
    }

    /**
     * Randomly selects and appends the next character to the output string.
     *
     * @param currentPosition SeedIndex position of current seed.
     * @return the position of the next char that was randomly selected
     */
    protected int appendNextChar(SeedIndex currentPosition) {
        final ArrayList<Integer> possibleNextChars = seedNextChar.get(currentPosition);
        int nextCharPosition = possibleNextChars.get(rng.nextInt(possibleNextChars.size()));
        outputString.append(trainingString.charAt(nextCharPosition + level - 1));
        return nextCharPosition;
    }

    /**
     * Check if the current seed is the last seed in the training string AND this seed occurs nowhere else in the training string.
     *
     * @param position Position of seed in consideration.
     * @return Truth value of the indicated position being the last seed in the training string AND this seed occurs nowhere else in the training string.
     */
    protected boolean isLastSeed(int position) {
        if (position == indexRange) {
            final SeedIndex lastSeed = new SeedIndex(indexRange);
            return !seedNextChar.containsKey(lastSeed);
        }
        return false;
    }

    /**
     * Gets next character tracking HashMap.
     *
     * @return HashMap containing all possible seeds and their possible following characters.
     */
    protected HashMap<SeedIndex, ArrayList<Integer>> getSeedNextChar() {
        return seedNextChar;
    }

    /**
     * Gets the length of the seeds.
     *
     * @return the length of the seeds.
     */
    protected int getLevel() {
        return level;
    }

    /**
     * Gets the string used for training.
     *
     * @return the parsed input string.
     */
    protected StringBuilder getTrainingString() {
        return trainingString;
    }

    /**
     * Gets the string written to output.
     *
     * @return the generated output string.
     */
    protected StringBuilder getOutputString() {
        return outputString;
    }

    /**
     * Gets the static array of powers of HASH_POWER.
     *
     * @return an array of increasing integer powers of HASH_POWER.
     */

    protected int[] getPowers() {
        return powers;
    }

    /**
     * Gets the computed has values of all the seeds.
     *
     * @return an array of integer hash values for SeedIndex.
     */
    protected int[] getHashes() {
        return hashes;
    }

    /**
     * Gets the range of position of seeds.
     *
     * @return the maximal SeedIndex position.
     */
    protected int getIndexRange() {
        return indexRange;
    }

    /**
     * Wrapper class that describes the position of a seed with the training string.
     */
    public class SeedIndex {
        /**
         * Position of the contained seed.
         */
        private final int position;

        /**
         * HashSet that contains positions of all other equal seeds.
         */
        private HashSet<Integer> equalSeedPositions;

        public SeedIndex(final int position) {
            this.position = position;
        }

        /**
         * Gets the hash value of each seed.
         *
         * @return the precomputed hash value of the substring contained.
         */
        public int hashCode() {
            return hashes[position];
        }

        /**
         * Gets the position of the seed contained.
         *
         * @return The position of the seed contained.
         */

        public int getPosition() {
            return position;
        }

        /**
         * Gets the string of the seed contained.
         *
         * @return The string of the seed contained.
         */
        public String getSubstring() {
            return trainingString.substring(position, position + level);
        }

        /**
         * Computes if two SeedIndex objects contained equal seeds.
         * @param that The targeted SeedIndex object for equality comparison.
         * @return The truth value describing if the two SeedIndex objects contain equal seeds.
         */
        public boolean equals(Object that) {
            /*
             * Three ways to guarantee equality:
             * 1) SeedIndices have the same position (easy - O(1))
             * 2) SeedIndices were already computed to be equal (easy - O(1))
             * 3) SeedIndices have exactly the same character arrays (hard - O(k))
             */
            if (that instanceof SeedIndex) {
                SeedIndex thatSeedIndex = (SeedIndex) that;

                //Method 1
                if (thatSeedIndex.position == this.position) {
                    return true;
                }

                // Method 2
                HashSet<Integer> equalToThat = thatSeedIndex.equalSeedPositions;
                if (equalToThat != null) {
                    if (equalToThat.contains(this.position)) {
                        return true;
                    }
                }

                //Method 3
                for (int i = 0; i < level; i++) {
                    if (trainingString.charAt(this.position + i) != trainingString.charAt(thatSeedIndex.position + i)) {
                        return false;
                    }
                }

                //Update SeedIndices that we've already computed as equal to prevent double calculating
                thatSeedIndex.equalSeedPositions = new HashSet<>();
                thatSeedIndex.equalSeedPositions.add(this.position);
                return true;
            }
            return false;
        }
    }

    /**
     * Check if parameters are valid.
     * @param args parameters to consider
     * @return error message if parameters are valid: If so, returns the error message. If not, returns an empty string
     */
    protected static String validateParameters(String[] args) {
        if (args == null) {
            return "No Input Supplied.";
        }
        if (args.length != 4) {
            return "Exactly four inputs expected. Received " + args.length + ".";
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                return "Argument " + i + " is null.";
            }
        }
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return "Could not parse third argument (level).";
        }

        int length;
        try {
            length = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            return "Could not parse fourth argument (length).";
        }

        if (level < 0) {
            return "Level must be a nonnegative integer.";
        }

        if (length < 0) {
            return "Length must be a nonnegative integer.";
        }

        File inputFile = new File(args[0]);
        if (!inputFile.canRead()) {
            return "Could not read source file. Check file path, structure, and permissions.";
        }

        File outputFile = new File(args[1]);
        if (!outputFile.exists()) {
            return "";
        }
        if (!outputFile.canWrite()) {
            return "Could not write to output file. Check file path, structure, and permissions.";
        }

        StringBuilder trainingString = new StringBuilder();
        BufferedReader bfr;
        try {
            bfr = new BufferedReader(new FileReader(args[0]));
            while (bfr.ready()) {
                trainingString.append(bfr.readLine());
                if (trainingString.charAt(trainingString.length() - 1) != ' ') {
                    trainingString.append(" ");
                }
            }
            bfr.close();
        } catch (IOException e) {
            return "You weren't supposed to get here :(.";
        }

        if (level >= trainingString.length()) {
            return "Level must be strictly less than the length of the input file.";
        }

        return "";
    }

    public static void main(String[] args) {
        String errorMessage = validateParameters(args);
        if (errorMessage.length() > 0) {
            System.err.println(errorMessage);
            return;
        }

        TextProcessor rw;
        try {
            int level = Integer.parseInt(args[2]);
            rw = createProcessor(level);
        } catch (NumberFormatException e) {
            System.err.println("You weren't supposed to get here :(.");
            return;
        }
        try {
            rw.readText(args[0]);
        } catch (IOException e) {
            System.err.println("You weren't supposed to get here :(.");
            return;
        }
        try {
            int length = Integer.parseInt(args[3]);
            rw.writeText(args[1], Integer.parseInt(args[3]));
        } catch (IOException | NumberFormatException e) {
            System.err.println("You weren't supposed to get here :(.");
        }

    }
}
