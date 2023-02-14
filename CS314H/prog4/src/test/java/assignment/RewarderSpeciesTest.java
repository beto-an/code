package assignment;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


//Test the RewarderSpecies class
class RewarderSpeciesTest {


    //Some default multipliers to create RewarderSpecies with
    static final double[] defaultMultipliers1 = { 0, 0, 0, 0 };
    static final double[] defaultMultipliers2 = { 1, 1, 1, 1 };


    //Check if two double values are equivalent, barring very small floating point error
    public boolean roughlyEqual(double num1, double num2) {
        return Math.abs(num1 - num2) < 0.00001;
    }


    //Test that the breed function actually creates a child that inherits genes from two parents
    @Test
    void breed() {

        RewarderSpecies parent1 = new RewarderSpecies(Arrays.copyOf(defaultMultipliers1, 4));
        RewarderSpecies parent2 = new RewarderSpecies(Arrays.copyOf(defaultMultipliers2, 4));

        for (int i = 0; i < 1000; i++) {

            //Check that each gene comes from one of the parents
            RewarderSpecies child = parent1.breed(parent2);
            int p1GeneCount = 0;
            int p2GeneCount = 0;
            for (int j = 0; j < 4; j++) {
                if (roughlyEqual(defaultMultipliers1[j], child.getMultipliers()[j])) p1GeneCount++;
                if (roughlyEqual(defaultMultipliers2[j], child.getMultipliers()[j])) p2GeneCount++;
            }
            assertEquals(4, p1GeneCount + p2GeneCount);

        }

    }


    //Test that the mutate function properly changes multipliers with the correct mutation rate
    @Test
    void mutate() {

        RewarderSpecies[] listOfSpecies = new RewarderSpecies[100000];
        int count = 0;

        for (int i = 0; i < 100000; i++) {

            listOfSpecies[i] = new RewarderSpecies(Arrays.copyOf(defaultMultipliers1, 4));
            listOfSpecies[i].mutate();

            for (int j = 0; j < 4; j++) {

                double initMult = defaultMultipliers1[j];
                double speciesMult = listOfSpecies[i].getMultipliers()[j];

                //Assert mutation distance is correct
                assertTrue(roughlyEqual(initMult, speciesMult) || roughlyEqual(Math.abs(initMult - speciesMult), RewarderSpecies.mutationDistance[j]));
                if (!roughlyEqual(initMult, speciesMult)) count++;

            }

        }

        //Assert mutation rate is correct
        assertEquals(RewarderSpecies.mutationRate, count / 400000.0, 0.01);

    }


    //Test that the compareTo function compares scores correctly
    @Test
    void compareTo() {

        RewarderSpecies rs1 = new RewarderSpecies(Arrays.copyOf(defaultMultipliers1, 4));
        RewarderSpecies rs2 = new RewarderSpecies(Arrays.copyOf(defaultMultipliers2, 4));

        rs1.setScore(0.0);
        rs2.setScore(21873.0);
        assertTrue(rs1.compareTo(rs2) < 0);
        assertTrue(rs2.compareTo(rs1) > 0);

        rs1.setScore(23.0);
        rs2.setScore(-81.0);
        assertTrue(rs1.compareTo(rs2) > 0);
        assertTrue(rs2.compareTo(rs1) < 0);

        rs1.setScore(-7.0);
        rs2.setScore(-2.0);
        assertTrue(rs1.compareTo(rs2) < 0);
        assertTrue(rs2.compareTo(rs1) > 0);

        rs1.setScore(123.0);
        rs2.setScore(-26.0);
        assertTrue(rs1.compareTo(rs2) > 0);
        assertTrue(rs2.compareTo(rs1) < 0);

    }


}