package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static assignment.RewarderSpeciesSelector.advancePopulation;
//import static assignment.RewarderSpeciesSelector.evaluateIndividual;

public class RewarderSpeciesProcessor {


    public static ArrayList<RewarderSpecies> readSpecies(String filename) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        int n = Integer.parseInt(bfr.readLine());
        ArrayList<RewarderSpecies> pop = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double[] multipliers = new double[4];
            StringTokenizer st = new StringTokenizer(bfr.readLine());
            for (int j = 0; j < multipliers.length; j++) {
                multipliers[j] = Double.parseDouble(st.nextToken());
            }
            pop.add(new RewarderSpecies(multipliers));
        }
        return pop;
    }

    public static void writeSpecies(String filename, ArrayList<RewarderSpecies> pop) throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter(filename));
        String separator = " ";
        String endOfLine = "\n";
        bfw.write(pop.size() + endOfLine);
        for (RewarderSpecies rewarderSpecies : pop) {
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

    public static void initRandomPop(String filename, int size) throws IOException {
        writeSpecies(filename, RewarderSpeciesSelector.generateNewPopulation(size));
    }

    public static void main(String[] args) throws IOException {
        //initRandomPop("weights.txt", 40);
        ArrayList<RewarderSpecies> currentPop = readSpecies("weights.txt");
        currentPop = advancePopulation(currentPop, -1);

        System.out.println(currentPop.get(currentPop.size() - 1));

        writeSpecies("weights.txt", currentPop);
    }

}
