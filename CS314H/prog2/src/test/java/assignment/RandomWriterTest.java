package assignment;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.testng.Assert.*;

public class RandomWriterTest {

    @Test
    public void testValidateParameters() {

        String[][] args = {
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\test_books\\CatInTheHat.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdoNot.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "55", "hi im here"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHats.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15.5", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "Heyy", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "55.5"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "fiftyfive"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "0", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "0"},
                null,
                {null, "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", null, "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", null, "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", null},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "-1", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\MuchAdo.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "-1"},
                {"C:\\C S 314H\\prog2\\test_books\\CatInTheHat.txt", "C:\\C S 314H\\prog2\\output\\Unwriteable.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\BabyBook.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "16", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\BabyBook.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "15", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\BabyBook.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "14", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\BabyBook.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "13", "55"},
                {"C:\\C S 314H\\prog2\\test_books\\BabyBook.txt", "C:\\C S 314H\\prog2\\output\\CatInTheHat.txt", "12", "55"},
        };

        String[] expectedErrors = {
                "",
                "Could not read source file. Check file path, structure, and permissions.",
                "Exactly four inputs expected. Received 3.",
                "Exactly four inputs expected. Received 5.",
                "",
                "Could not parse third argument (level).",
                "Could not parse third argument (level).",
                "Could not parse fourth argument (length).",
                "Could not parse fourth argument (length).",
                "",
                "",
                "No Input Supplied.",
                "Argument 0 is null.",
                "Argument 1 is null.",
                "Argument 2 is null.",
                "Argument 3 is null.",
                "Level must be a nonnegative integer.",
                "Length must be a nonnegative integer.",
                "Could not write to output file. Check file path, structure, and permissions.",
                "Level must be strictly less than the length of the input file.",
                "Level must be strictly less than the length of the input file.",
                "Level must be strictly less than the length of the input file.",
                "",
                "",
        };

        for (int i = 0; i < expectedErrors.length; i++) {
            assertEquals(RandomWriter.validateParameters(args[i]), expectedErrors[i], "position " + i);
        }
    }


    @Test
    public void testWriteText() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(1);
        RandomWriter rw = (RandomWriter) tw;


        final int LENGTH = 1000;
        final int ITERATIONS = 9999;
        rw.readText("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");
        for (int i = 0; i < ITERATIONS; i++) {
            rw.writeText("C:\\C S 314H\\prog2\\output\\BabyBook.txt", LENGTH);
            Assert.assertEquals(rw.getOutputString().length(), LENGTH);
        }
    }

    @Test
    public void testComputePowers() {
        {
            TextProcessor tw = RandomWriter.createProcessor(2);
            RandomWriter rw = (RandomWriter) tw;

            final int[] expectedPowers = {1, 31};
            final int[] actualPowers = rw.getPowers();

            for (int i = 0; i < expectedPowers.length; i++) {
                Assert.assertEquals(actualPowers[i], expectedPowers[i], "Difference in element " + i);
            }
        }
        {
            TextProcessor tw = RandomWriter.createProcessor(4);
            RandomWriter rw = (RandomWriter) tw;

            final int[] expectedPowers = {1, 31, 961, 29791};
            final int[] actualPowers = rw.getPowers();

            for (int i = 0; i < expectedPowers.length; i++) {
                Assert.assertEquals(actualPowers[i], expectedPowers[i], "Difference in element " + i);
            }
        }
        {
            TextProcessor tw = RandomWriter.createProcessor(9);
            RandomWriter rw = (RandomWriter) tw;

            final int[] expectedPowers = {1, 31, 961, 29791, 923521, 28629151, 887503681, 1742810335, -1807454463};  // tests overflow
            final int[] actualPowers = rw.getPowers();

            for (int i = 0; i < expectedPowers.length; i++) {
                Assert.assertEquals(actualPowers[i], expectedPowers[i], "Difference in element " + i);
            }
        }
        {
            TextProcessor tw = RandomWriter.createProcessor(1);
            RandomWriter rw = (RandomWriter) tw;

            final int[] expectedPowers = {1};
            final int[] actualPowers = rw.getPowers();

            for (int i = 0; i < expectedPowers.length; i++) {
                Assert.assertEquals(actualPowers[i], expectedPowers[i], "Difference in element " + i);
            }
        }

    }

    @Test
    public void testReadInFile() throws IOException {
        {
            TextProcessor tw = RandomWriter.createProcessor(10);
            RandomWriter rw = (RandomWriter) tw;

            final StringBuilder expectedString = new StringBuilder("Any random text belongs here it should be read in as Any random text belongs here it should be read in as ");

            rw.readInFile("C:\\C S 314H\\prog2\\test_books\\FileInputTester.txt");

            for (int i = 0; i < expectedString.length(); i++) {
                Assert.assertEquals(rw.getTrainingString().charAt(i), expectedString.charAt(i), "Difference in character " + i);
            }
        }
    }

    @Test
    public void testInstantiateConstants() throws IOException {
        {
            TextProcessor tw = RandomWriter.createProcessor(10);
            RandomWriter rw = (RandomWriter) tw;

            final int expectedIndexRange = 96;
            final int[] expectedHashes = new int[97];

            rw.readInFile("C:\\C S 314H\\prog2\\test_books\\FileInputTester.txt");
            rw.instantiateConstants();

            final int actualIndexRange = rw.getIndexRange();
            final int[] actualHashes = rw.getHashes();

            Assert.assertEquals(actualIndexRange, expectedIndexRange);
            for (int i = 0; i < expectedHashes.length; i++) {
                Assert.assertEquals(actualHashes[i], expectedHashes[i], "Difference in array position " + i);
            }

        }
    }

    @Test
    public void testConstructHashMap() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(1);
        RandomWriter rw = (RandomWriter) tw;

        final int[] expectedHashes = {97, 98, 97, 99, 97, 100, 98, 99, 98, 100, 99, 100, 97};

        rw.readText("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");

        final HashMap<RandomWriter.SeedIndex, ArrayList<Integer>> expectedSeedNextChar = new HashMap<>();
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(3);
        a.add(5);

        ArrayList<Integer> b = new ArrayList<>();
        b.add(2);
        b.add(7);
        b.add(9);

        ArrayList<Integer> c = new ArrayList<>();
        c.add(4);
        c.add(8);
        c.add(11);

        ArrayList<Integer> d = new ArrayList<>();
        d.add(6);
        d.add(10);
        d.add(12);

        expectedSeedNextChar.put(rw.new SeedIndex(0), a);
        expectedSeedNextChar.put(rw.new SeedIndex(1), b);
        expectedSeedNextChar.put(rw.new SeedIndex(3), c);
        expectedSeedNextChar.put(rw.new SeedIndex(5), d);


        final int[] actualHashes = rw.getHashes();
        final HashMap<RandomWriter.SeedIndex, ArrayList<Integer>> actualSeedNextChar = rw.getSeedNextChar();


        for (int i = 0; i < expectedHashes.length; i++) {
            Assert.assertEquals(actualHashes[i], expectedHashes[i], "Difference in array position " + i);
        }

        for (RandomWriter.SeedIndex key : expectedSeedNextChar.keySet()) {
            ArrayList<Integer> expectedNextChar = expectedSeedNextChar.get(key);
            ArrayList<Integer> actualNextChar = actualSeedNextChar.get(key);
            for (int i = 0; i < expectedNextChar.size(); i++) {
                Assert.assertEquals(actualNextChar.get(i), expectedNextChar.get(i));
            }
        }
    }

    @Test
    public void testComputeHash() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(2);
        RandomWriter rw = (RandomWriter) tw;

        final int[] expectedHashes = {3105, 3135, 3106, 3166, 3107, 3198, 3137, 3167, 3138, 3199, 3169, 3197};

        rw.readInFile("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");
        rw.instantiateConstants();

        for (int i = 0; i < expectedHashes.length; i++) {
            rw.computeHash(i);
            Assert.assertEquals(rw.getHashes()[i], expectedHashes[i], "Difference in array position " + i);
        }
    }

    @Test
    public void testInitializeRandomSeed() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(1);
        RandomWriter rw = (RandomWriter) tw;
        rw.readText("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");


        int[] counts = new int[5];
        final int N = 9999999;
        for (int i = 0; i < N; i++) {
            RandomWriter.SeedIndex seed = rw.initializeRandomSeed();
            if (rw.getTrainingString().charAt(seed.getPosition()) - 'a' >= 0) {
                counts[rw.getTrainingString().charAt(seed.getPosition()) - 'a']++;
            } else {
                counts[4]++;
            }

            Assert.assertEquals(rw.getOutputString().length(), i + 1);
            Assert.assertEquals(rw.getOutputString().charAt(i), rw.getTrainingString().charAt(seed.getPosition()));
        }
        Assert.assertEquals(counts[0] * 1.0 / N, 4.0 / 13, 0.001);
        Assert.assertEquals(counts[1] * 1.0 / N, 3.0 / 13, 0.001);
        Assert.assertEquals(counts[2] * 1.0 / N, 3.0 / 13, 0.001);
        Assert.assertEquals(counts[3] * 1.0 / N, 3.0 / 13, 0.001);
        Assert.assertEquals(counts[4] * 1.0 / N, 0, 0.001);
    }

    public void testAppendNextChar() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(1);
        RandomWriter rw = (RandomWriter) tw;

        rw.readText("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");

        final int N = 99999;
        int[] counts = new int[5];
        final RandomWriter.SeedIndex firstChar = rw.new SeedIndex(0);
        for (int i = 0; i < N; i++) {
            int nextCharPosition = rw.appendNextChar(firstChar);
            StringBuilder outputString = rw.getOutputString();
            Assert.assertEquals(rw.getTrainingString().charAt(nextCharPosition), outputString.charAt(outputString.length() - 1));
            if (rw.getTrainingString().charAt(nextCharPosition) - 'a' >= 0) {
                counts[rw.getTrainingString().charAt(nextCharPosition) - 'a']++;
            } else {
                counts[4]++;
            }
        }
        Assert.assertEquals(counts[0] * 1.0 / N, 0);
        Assert.assertEquals(counts[1] * 1.0 / N, 1.0 / 4, 0.01);
        Assert.assertEquals(counts[2] * 1.0 / N, 1.0 / 4, 0.01);
        Assert.assertEquals(counts[3] * 1.0 / N, 1.0 / 4, 0.01);
        Assert.assertEquals(counts[4] * 1.0 / N, 1.0 / 4, 0.01);

    }

    public void testIsLastSeed() throws IOException {
        TextProcessor tw = RandomWriter.createProcessor(4);
        RandomWriter rw = (RandomWriter) tw;

        rw.readText("C:\\C S 314H\\prog2\\test_books\\BabyBook.txt");

        boolean[] expectedIsLastSeed = {false, false, false, false, false, false, false, false, false, false, true};

        for (int i = 0; i < expectedIsLastSeed.length; i++) {
            Assert.assertEquals(rw.isLastSeed(i), expectedIsLastSeed[i]);
        }


    }

    public static void main(String[] args) {
        RandomWriterTest tester = new RandomWriterTest();

        tester.testComputePowers();
        tester.testValidateParameters();
        try {
            tester.testWriteText();
            tester.testReadInFile();
            tester.testInstantiateConstants();
            tester.testConstructHashMap();
            tester.testComputeHash();
            tester.testInitializeRandomSeed();
            tester.testAppendNextChar();
            tester.testIsLastSeed();
        } catch (IOException e) {
            System.err.println("IOException: fix IO");
            System.exit(-1);
        }
    }
}