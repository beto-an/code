package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//Deprecated
public class HoldModel {

    public static int runSim() {
        TetrisBoard tb = new TetrisBoard(10, 24);
        Piece piece = TetrisBoard.getRandomPiece();
        tb.nextPiece(TetrisBoard.getRandomPiece(), new Point(tb.getWidth() / 2 - piece.getWidth() / 2, tb.getHeight() - 4));
        int turns = 1;
        while (tb.getMaxHeight() <= 20 && turns < 2000) {
            ArrayList<TetrisBoard> boards = tb.generateAllStates();
            double maxScore = Integer.MIN_VALUE;
            TetrisBoard nextBest = null;
            boolean isHoldingPiece = false;
            for (TetrisBoard newTB : boards) {
                if (newTB.getHeldPiece() != null) isHoldingPiece = true;
                double score = Rewarder.reward(tb, newTB);
                if (score > maxScore) {
                    maxScore = score;
                    nextBest = newTB;
                }
            }

            if (!isHoldingPiece) {
                TetrisBoard newTB = tb.clone();
                newTB.setHeldPiece(newTB.getCurrentPiece());
                newTB.setCurrentPiece(TetrisBoard.getRandomPiece());
                newTB.setLowerLeftPos(new Point(newTB.getWidth() / 2 - newTB.getCurrentPiece().getWidth() / 2, 20));
                tb = newTB;
            } else {
                tb = nextBest;
            }
            turns++;
        }
        return turns * 100 - tb.getMaxHeight();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<RewarderSpecies> currentPop = GeneticProcessor.readPopulation("holdWeights.txt");
        if (currentPop.size() == 0) currentPop = GeneticProcessor.generateNewPopulation(100);
        for (int i = 0; i < 100; i++) {
            System.out.println("Iteration " + i + ":");
            GeneticProcessor.writePopulation("holdWeights.txt", currentPop);
            currentPop = GeneticProcessor.advancePopulation(currentPop, 1);
        }
        for (RewarderSpecies rs : currentPop) {
            GeneticProcessor.evaluateSpecies(rs, 1);
        }
        Collections.sort(currentPop);

        System.out.println(currentPop.get(currentPop.size() - 1));
    }

}
