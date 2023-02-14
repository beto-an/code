package assignment;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RewarderSpeciesSelector {

    public static Random rng = new Random();

    public static final int iterations = 5;
    public static final int depth = 20;

    public static final double survivingPopulation = 0.1;

    public static final double dominatingPopulation = 0.01;

    public static final double breedingSplit = 0.9;

    public static ArrayList<RewarderSpecies> generateNewPopulation(int popSize) {
        ArrayList<RewarderSpecies> list = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            double[] multipliers = new double[4];
            for (int j = 0; j < 4; j++) {
                multipliers[j] = Math.random() * 20 - 10;
            }
            list.add(new RewarderSpecies(multipliers));
        }
        return list;
    }

    public static ArrayList<RewarderSpecies> advancePopulation(ArrayList<RewarderSpecies> population, int type) {
        ArrayList<RewarderSpecies> newPop = new ArrayList<>();
        int as = 0;
        double sum = 0;
        for (RewarderSpecies rs : population) {
            if (type == 0) rs.score = simpleEvaluation(rs);
            else if (type == 1) rs.score = holdEvaluation(rs);
            else rs.score = -1.0;
            //else rs.score = evaluateIndividual(rs);
            System.out.println("Evaluating Species " + (++as) + ": " + rs.score);
            sum += rs.score;
        }
        System.out.println("Average score of population: " + (sum / as));
        Collections.sort(population);
        System.out.println(population.get(population.size() - 1));

        for (int i = (int) (population.size() * (1 - dominatingPopulation)); i < population.size(); i++) {
            newPop.add(population.get(i));
        }

        while (population.size() > newPop.size()) {
            RewarderSpecies rs1;
            RewarderSpecies rs2;

            if (sample(breedingSplit)) {
                double location = rng.nextDouble() * survivingPopulation;
                rs1 = population.get((int) ((1-location) * population.size()));
            } else {
                double location = rng.nextDouble() * (1-survivingPopulation);
                rs1 = population.get((int) (location * population.size()));
            }

            if (sample(breedingSplit)) {
                double location = rng.nextDouble() * survivingPopulation;
                rs2 = population.get((int) ((1-location) * population.size()));
            } else {
                double location = rng.nextDouble() * (1-survivingPopulation);
                rs2 = population.get((int) (location * population.size()));
            }
            RewarderSpecies next = rs1.breed(rs2);
            next.mutate();
            newPop.add(next);
        }

        return newPop;
    }

    public static double simpleEvaluation(RewarderSpecies ind) {
        Rewarder.changeWeights(ind.multipliers[0], ind.multipliers[1], ind.multipliers[2], ind.multipliers[3]);
        double sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += SimpleModel.runSim();
        }
        return sum / iterations;
    }

    public static double holdEvaluation(RewarderSpecies ind) {
        Rewarder.changeWeights(ind.multipliers[0], ind.multipliers[1], ind.multipliers[2], ind.multipliers[3]);
        double sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += HoldModel.runSim();
        }
        return sum / iterations;
    }

    /*public static double evaluateIndividual(RewarderSpecies ind) {
        Rewarder.changeWeights(ind.multipliers[0], ind.multipliers[1], ind.multipliers[2], ind.multipliers[3]);
        int sum = 0;
        TetrisModel tm;
        for (int i = 0; i < iterations; i++) {
            tm = TetrisModel.initialize(depth);
            Policy p = tm.p;
            int j = 0;
            do {
                j++;
                tm.getNextState();
                if (tm.currentState.getTetrisBoard().maxHeight>20) {
                    sum += j;
                    break;
                }
            } while (true);
            tm.cleanup();
            tm = null;
        }
        return sum * 1.0 / iterations;

    }*/

    public static boolean sample(double p) {
        return rng.nextDouble() < p;
    }

    /*public static void main(String[] args) {
        ArrayList<RewarderSpecies> currentPop = generateNewPopulation(20);
        currentPop = advancePopulation(currentPop, false);
        for (RewarderSpecies rs : currentPop) {
            rs.score = evaluateIndividual(rs);
        }
        Collections.sort(currentPop);

        System.out.println(currentPop.get(currentPop.size() - 1));
    }*/

}
