package assignment;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;


//This class that contains a multitude of genetic algorithm functions and facilitate training
public class GeneticProcessor {


    //Random number generator object
    private static Random rng = new Random();

    //How many simulated Tetris games that each species should run when calculating the score
    public static final int iterations = 5;

    //What top percent of the population is defined as the 'best'
    public static final double survivingPopulation = 0.1;

    //What top percent of the population is completely preserved to the next generation
    public static final double dominatingPopulation = 0.01;

    //What is the chance that a random parent is picked from the best
    public static final double breedingSplit = 0.9;

    //The maximum number of turns one simulation is allowed to run
    public static final int maxTurns = 200;

    //Helpful String constants for printing
    private static final String separator = " ";
    private static final String endOfLine = "\n";


    //Generate a new population of a certain size with random initial genes
    public static ArrayList<RewarderSpecies> generateNewPopulation(int popSize) {

        ArrayList<RewarderSpecies> list = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {

            //Create a new species with randomized multipliers
            double[] multipliers = new double[4];
            for (int j = 0; j < 4; j++) {
                multipliers[j] = Math.random() * 20 - 10;
            }
            list.add(new RewarderSpecies(multipliers));

        }

        return list;

    }


    //Take a current population and create a new population, preserving/breeding individuals to do so
    public static ArrayList<RewarderSpecies> advancePopulation(ArrayList<RewarderSpecies> population, int type) {

        ArrayList<RewarderSpecies> newPop = new ArrayList<>();

        //Evaluate the performance of every species
        int count = 0;
        double sum = 0;
        for (RewarderSpecies rs : population) {

            //Evaluate the current species
            evaluateSpecies(rs, type);
            System.out.println("Evaluating Species " + (++count) + ": " + rs.score);
            sum += rs.score;

        }

        //Print out average score and the best weights + sort the population based on score
        System.out.println("Average score of population: " + (sum / count));
        Collections.sort(population);
        System.out.println(population.get(population.size() - 1));

        //Directly add the very best of the population to the next generation
        for (int i = (int) (population.size() * (1 - dominatingPopulation)); i < population.size(); i++) {
            newPop.add(population.get(i));
        }

        //Fill up the rest of the next generation with new children
        while (population.size() > newPop.size()) {

            //The two parents
            RewarderSpecies parent1 = pickParent(population);
            RewarderSpecies parent2 = pickParent(population);

            //Breed the two species and attempt to mutate the child (may or may not be successful)
            RewarderSpecies next = parent1.breed(parent2);
            next.mutate();
            newPop.add(next);

        }

        return newPop;

    }


    //Helper method for selecting one parent for breeding
    public static RewarderSpecies pickParent(ArrayList<RewarderSpecies> population) {

        //Pick a parent according to the defined constants
        //With probability breedingSplit pick one of the best, otherwise pick a bad parent
        if (sample(breedingSplit)) {
            double location = rng.nextDouble() * survivingPopulation;
            return population.get((int) ((1-location) * population.size()));
        } else {
            double location = rng.nextDouble() * (1-survivingPopulation);
            return population.get((int) (location * population.size()));
        }

    }


    //Evaluate a species with a specific model
    public static void evaluateSpecies(RewarderSpecies individual, int type) {

        //Set the Rewarder weights
        Rewarder.changeWeights(individual.multipliers[0], individual.multipliers[1], individual.multipliers[2], individual.multipliers[3]);

        //Set the score to be the average score of 'iterations' number of runs
        double sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += runSim(type);
        }
        individual.setScore(sum / iterations);

    }


    //Set a random piece with the correct location on a given board
    public static void setRandomPiece(TetrisBoard board) {
        Piece piece = TetrisBoard.getRandomPiece();
        board.nextPiece(TetrisBoard.getRandomPiece(), new Point(board.getWidth() / 2 - piece.getWidth() / 2, board.getHeight() - 4));
    }


    //Run a Tetris game simulation of the specific type (allow or disallow holding pieces)
    public static int runSim(int type) {

        //Initialize the board with a random piece
        TetrisBoard tb = new TetrisBoard(10, 24);
        setRandomPiece(tb);

        //If holding is allowed (i.e. type == 1), then hold a piece
        if (type == 1) {
            Move.executeHold(tb);
            setRandomPiece(tb);
        }

        //Simulate the game to death or a certain number of turns and return a score
        int turns = 1;
        while (tb.getMaxHeight() <= 20 && turns < maxTurns) {
            tb = SimpleBrain.findBestNextBoard(tb);
            turns++;
        }
        return turns * 100 - tb.getMaxHeight();

    }


    //Read in a population from a file
    public static ArrayList<RewarderSpecies> readPopulation(String filename) throws IOException {

        //Read in the number of species
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        int n = Integer.parseInt(bfr.readLine());

        //Read in each species as four double multipliers
        ArrayList<RewarderSpecies> pop = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {

            double[] multipliers = new double[4];
            StringTokenizer st = new StringTokenizer(bfr.readLine());
            for (int j = 0; j < multipliers.length; j++) {
                multipliers[j] = Double.parseDouble(st.nextToken());
            }
            pop.add(new RewarderSpecies(multipliers));

        }

        bfr.close();
        return pop;

    }


    //Write a population to a file
    public static void writePopulation(String filename, ArrayList<RewarderSpecies> pop) throws IOException {

        //Write the number of species
        BufferedWriter bfw = new BufferedWriter(new FileWriter(filename));
        bfw.write(pop.size() + endOfLine);

        //Write each species as four double multipliers
        for (RewarderSpecies rewarderSpecies : pop) {

            //Use a StringBuilder to create the line corresponding to each species
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < rewarderSpecies.multipliers.length; j++) {
                sb.append(rewarderSpecies.multipliers[j]);
                sb.append(separator);
            }
            sb.append(endOfLine);
            bfw.write(sb.toString());

        }

        bfw.close();

    }


    //Return true with chance p, otherwise return false
    public static boolean sample(double p) {
        return rng.nextDouble() < p;
    }


    //Evolves the population stored in a certain file using GeneticProcessor functions
    public static void main(String[] args) throws IOException {

        //Read in the existing population or create a new population if needed
        ArrayList<RewarderSpecies> currentPop = GeneticProcessor.readPopulation("simpleWeights.txt");
        if (currentPop.size() == 0) currentPop = GeneticProcessor.generateNewPopulation(500);

        //Train for a large number of iterations, each time advancing one generation
        for (int i = 0; i < 100; i++) {

            System.out.println("Iteration " + i + ":");

            //Print the population each time so terminating the program before this loop terminates still preserves data
            GeneticProcessor.writePopulation("simpleWeights.txt", currentPop);
            currentPop = GeneticProcessor.advancePopulation(currentPop, 0);

        }

        //After all iterations, evaluate and sort all species again
        for (RewarderSpecies rs : currentPop) {
            GeneticProcessor.evaluateSpecies(rs, 0);
        }
        Collections.sort(currentPop);

        //Print out the best performing species' weights
        System.out.println(currentPop.get(currentPop.size() - 1));

    }
    

}
