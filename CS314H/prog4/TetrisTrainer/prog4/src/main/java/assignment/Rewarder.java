package assignment;

public class Rewarder {

    public static double holesMultiplier = -9.4;
    public static double bumpinessMultiplier = -0.51;
    public static double colHeightMultiplier = 0.07;
    public static double rowsClearedMultiplier = 1.5;

    //Positive reward is good, negative reward is bad
    public static double reward(TetrisBoard prev, TetrisBoard next) {
        return valueOfState(next) - valueOfState(prev);
    }

    public static void changeWeights(double holesMultiplier, double bumpinessMultiplier, double colHeightMultiplier, double rowsClearedMultiplier) {
        Rewarder.holesMultiplier = holesMultiplier;
        Rewarder.bumpinessMultiplier = bumpinessMultiplier;
        Rewarder.colHeightMultiplier = colHeightMultiplier;
        Rewarder.rowsClearedMultiplier = rowsClearedMultiplier;
    }

    //More positive value is better, more negative value is worse
    public static double valueOfState(TetrisBoard board) {
        if (board.getMaxHeight() > 20) {
            return -99999;
        }
        double overallValue = 0;

        overallValue += holesMultiplier * countHoles(board);
        overallValue += bumpinessMultiplier * calculateBumpiness(board);
        overallValue += colHeightMultiplier * calculateHeights(board); // * board.getMaxHeight()/4;
        overallValue += rowsClearedMultiplier * board.getRowsCleared(); // * board.getMaxHeight()/4;

        return overallValue;

    }

    public static int countHoles(TetrisBoard board) {

        int holes = 0;

        for (int i = 0; i < board.getWidth(); i++) {
            boolean seenPiece = false;
            for (int j = board.getHeight() - 1; j >= 0; j--) {
                Piece.PieceType piece = board.getGrid(i, j);
                if (piece != null) seenPiece = true;
                else if (seenPiece) holes++;
            }
        }

        return holes;

    }

    public static double calculateBumpiness(TetrisBoard board) {

        double bumpiness = 0;

        int[] heights = board.getColHeight();
        for (int i = 0; i < board.getWidth() - 1; i++) {
            bumpiness += Math.abs(heights[i + 1] - heights[i] - 1) * Math.abs(heights[i + 1] - heights[i] - 1);
        }

        return bumpiness;

    }

    public static int calculateHeights(TetrisBoard board) {
        int heightSum = 0;
        for (int i = 0; i < 10; i++) {
            heightSum += board.getColumnHeight(i);
        }
        return heightSum;
    }

}
