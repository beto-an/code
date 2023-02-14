package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleModel {

    public static int runSim() {
        TetrisBoard tb = new TetrisBoard(10, 24);
        Piece piece = TetrisBoard.getRandomPiece();
        tb.nextPiece(TetrisBoard.getRandomPiece(), new Point(tb.getWidth() / 2 - piece.getWidth() / 2, tb.getHeight() - 4));
        int turns = 1;
        while (tb.getMaxHeight() <= 20 && turns < 3000) {
            ArrayList<TetrisBoard> boards = tb.generateAllStates();
            double maxScore = Integer.MIN_VALUE;
            TetrisBoard nextBest = null;
            for (TetrisBoard newTB : boards) {
                double score = Rewarder.reward(tb, newTB);
                if (score > maxScore) {
                    maxScore = score;
                    nextBest = newTB;
                }
            }
            tb = nextBest;
            turns++;
        }
        return turns * 100 - tb.getMaxHeight();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<RewarderSpecies> currentPop = RewarderSpeciesProcessor.readSpecies("simpleWeights.txt");
        if (currentPop.size() == 0) currentPop = RewarderSpeciesSelector.generateNewPopulation(500);
        for (int i = 0; i < 100; i++) {
            System.out.println("Iteration " + i + ":");
            RewarderSpeciesProcessor.writeSpecies("simpleWeights.txt", currentPop);
            currentPop = RewarderSpeciesSelector.advancePopulation(currentPop, 0);
        }
        for (RewarderSpecies rs : currentPop) {
            rs.score = RewarderSpeciesSelector.simpleEvaluation(rs);
        }
        Collections.sort(currentPop);

        System.out.println(currentPop.get(currentPop.size() - 1));
    }

}
