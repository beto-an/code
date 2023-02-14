package assignment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class RewarderSpecies implements Comparable<RewarderSpecies> {

    protected double[] multipliers;

    protected Double score;

    public static Random rng = new Random();

    public static final double mutationRate = 0.2;

    public static final double[] mutationDistance = {0.5, 0.5, 0.5, 0.5};

    public RewarderSpecies(double[] multipliers) {
        this.multipliers = multipliers;
    }

    public RewarderSpecies breed(RewarderSpecies other) {
        double[] multiplier = new double[multipliers.length];
        for (int i = 0; i < multipliers.length; i++) {
            if (rng.nextBoolean()) {
                multiplier[i] = multipliers[i];
            } else {
                multiplier[i] = other.multipliers[i];
            }
        }
        return new RewarderSpecies(multiplier);
    }

    public void mutate() {
        for (int i = 0; i < multipliers.length; i++) {
            if (rng.nextDouble() < mutationRate) {
                if (rng.nextBoolean()) {
                    multipliers[i] += mutationDistance[i];
                } else {
                    multipliers[i] -= mutationDistance[i];
                }
            }
        }
    }


    @Override
    public int compareTo(RewarderSpecies o) {
        return score.compareTo(o.score);
    }

    public String toString() {
        return Arrays.toString(multipliers);
    }
}
