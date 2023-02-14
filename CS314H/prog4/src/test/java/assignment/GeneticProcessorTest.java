package assignment;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


//Test the GeneticProcessor class
class GeneticProcessorTest {


    //Some default multipliers to create RewarderSpecies with
    static final double[] badMultipliers = { 0, 0, 0, 0 };
    static final double[] goodMultipliers = { 1, 1, 1, 1 };


    //Test that the pickParent function chooses parents in the right proportion
    @Test
    void pickParent() {

        RewarderSpecies bad = new RewarderSpecies(badMultipliers);
        RewarderSpecies good = new RewarderSpecies(goodMultipliers);

        ArrayList<RewarderSpecies> population = new ArrayList<>();

        for (int i = 0; i < (100000) * (1 - GeneticProcessor.survivingPopulation); i++) {
            population.add(bad);
        }

        for (int i = 0; i < (100000) * GeneticProcessor.survivingPopulation; i++) {
            population.add(good);
        }

        int goodCount = 0;

        for (int i = 0; i < 100000; i++) {
            RewarderSpecies parent = GeneticProcessor.pickParent(population);
            if (parent == good) goodCount++;
        }

        assertEquals(100000 * GeneticProcessor.breedingSplit, goodCount, 500);

    }


    //
    @Test
    void evaluateSpecies() {

        RewarderSpecies bad = new RewarderSpecies(badMultipliers);
        bad.setScore(0.0);

        GeneticProcessor.evaluateSpecies(bad, 0);

        assertEquals(badMultipliers[0], Rewarder.holesMultiplier);
        assertEquals(badMultipliers[1], Rewarder.bumpinessMultiplier);
        assertEquals(badMultipliers[2], Rewarder.colHeightMultiplier);
        assertEquals(badMultipliers[3], Rewarder.rowsClearedMultiplier);

        assertTrue(0 < bad.getScore() && bad.getScore() < GeneticProcessor.maxTurns * 100);

        GeneticProcessor.evaluateSpecies(bad, 1);

        assertEquals(badMultipliers[0], Rewarder.holesMultiplier);
        assertEquals(badMultipliers[1], Rewarder.bumpinessMultiplier);
        assertEquals(badMultipliers[2], Rewarder.colHeightMultiplier);
        assertEquals(badMultipliers[3], Rewarder.rowsClearedMultiplier);

        assertTrue(0 < bad.getScore() && bad.getScore() < GeneticProcessor.maxTurns * 100);

        RewarderSpecies good = new RewarderSpecies(goodMultipliers);
        good.setScore(0.0);

        GeneticProcessor.evaluateSpecies(good, 0);

        assertEquals(goodMultipliers[0], Rewarder.holesMultiplier);
        assertEquals(goodMultipliers[1], Rewarder.bumpinessMultiplier);
        assertEquals(goodMultipliers[2], Rewarder.colHeightMultiplier);
        assertEquals(goodMultipliers[3], Rewarder.rowsClearedMultiplier);

        assertTrue(0 < good.getScore() && good.getScore() < GeneticProcessor.maxTurns * 100);

        GeneticProcessor.evaluateSpecies(good, 1);

        assertEquals(goodMultipliers[0], Rewarder.holesMultiplier);
        assertEquals(goodMultipliers[1], Rewarder.bumpinessMultiplier);
        assertEquals(goodMultipliers[2], Rewarder.colHeightMultiplier);
        assertEquals(goodMultipliers[3], Rewarder.rowsClearedMultiplier);

        assertTrue(0 < good.getScore() && good.getScore() < GeneticProcessor.maxTurns * 100);


    }


    //Make sure that the runSim method runs simulations properly and terminates
    @Test
    void runSim() {

        for (int i = 0; i < 5; i++) {

            assertTrue(GeneticProcessor.runSim(0) < GeneticProcessor.maxTurns * 100);
            assertTrue(GeneticProcessor.runSim(1) < GeneticProcessor.maxTurns * 100);

        }

    }


}