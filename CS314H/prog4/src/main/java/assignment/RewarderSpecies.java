package assignment;

import java.util.Arrays;
import java.util.Random;


//The species class for the genetic algorithm to evolve
public class RewarderSpecies implements Comparable<RewarderSpecies> {


    //The genes for this species, which is the four multipliers for the Rewarder
    protected double[] multipliers;

    //A double score that measures how well the current species did
    protected Double score;

    //Random number generator object
    private static Random rng = new Random();

    //Static double value for how often a species mutates
    public static final double mutationRate = 0.2;

    //Static list of values for how much each gene mutates each time
    public static final double[] mutationDistance = { 0.03, 0.03, 0.03, 0.03 };


    //Simple constructor that sets the initial weights
    public RewarderSpecies(double[] multipliers) {
        this.multipliers = multipliers;
    }


    //Breed the current species with another species, and return the child
    public RewarderSpecies breed(RewarderSpecies other) {

        //Create a new set of multipliers
        double[] multiplier = new double[multipliers.length];

        //Each gene has a 50 percent chance to be copied from either parent
        for (int i = 0; i < multipliers.length; i++) {
            if (rng.nextBoolean()) {
                multiplier[i] = multipliers[i];
            } else {
                multiplier[i] = other.multipliers[i];
            }
        }

        return new RewarderSpecies(multiplier);

    }


    //Using the static constants, potentially mutate one or more genes in the current species
    public void mutate() {

        for (int i = 0; i < multipliers.length; i++) {

            //With mutationRate chance, mutate the current gene in a random direction with the set distance
            if (rng.nextDouble() < mutationRate) {
                if (rng.nextBoolean()) {
                    multipliers[i] += mutationDistance[i];
                } else {
                    multipliers[i] -= mutationDistance[i];
                }
            }

        }

    }


    //Compare two RewarderSpecies based on score; allows sorting based on how well the species perform
    @Override
    public int compareTo(RewarderSpecies o) {
        return score.compareTo(o.score);
    }


    //Handy String representation of each species' multipliers
    public String toString() {
        return Arrays.toString(multipliers);
    }


    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public double[] getMultipliers() {
        return multipliers;
    }


}
