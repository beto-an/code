package assignment;


//Helper class for calculating the reward of moving from one board to another
public class Rewarder {


    //Constants that determine how the score for a board is weighted on each factor
    //As calculated by genetic algorithm, the current weights here are rounded values found from the algorithm
    public static double holesMultiplier = -9.4;
    public static double bumpinessMultiplier = -0.51;
    public static double colHeightMultiplier = -0.5;
    public static double rowsClearedMultiplier = 1.8;


    //Calculate reward for changing from one board to another (positive is good, negative is bad)
    public static double reward(TetrisBoard prev, TetrisBoard next) {
        return valueOfState(next) - valueOfState(prev);
    }


    //Function for easily setting all four weights
    public static void changeWeights(double holesMultiplier, double bumpinessMultiplier, double colHeightMultiplier, double rowsClearedMultiplier) {
        Rewarder.holesMultiplier = holesMultiplier;
        Rewarder.bumpinessMultiplier = bumpinessMultiplier;
        Rewarder.colHeightMultiplier = colHeightMultiplier;
        Rewarder.rowsClearedMultiplier = rowsClearedMultiplier;
    }

    //Return the score evaluation for a single board
    public static double valueOfState(TetrisBoard board) {

        //If the game is over, return a very negative score
        if (board.getMaxHeight() > 20) {
            return -99999;
        }

        //Calculate the score based on four metrics: holes, bumpiness, column height, and rows cleared
        double overallValue = 0;
        overallValue += holesMultiplier * countHoles(board);
        overallValue += bumpinessMultiplier * calculateBumpiness(board);
        overallValue += colHeightMultiplier * calculateHeights(board);
        overallValue += rowsClearedMultiplier * board.getRowsCleared();

        return overallValue;

    }


    //Count the number of holes in the board, where a hole is any empty square where a filled square exists somewhere above it in the same column
    public static int countHoles(TetrisBoard board) {

        int holes = 0;

        //Loop through all columns and count the number of holes in each column
        for (int i = 0; i < board.getWidth(); i++) {

            boolean seenPiece = false;

            //Loop through each column for descending height to find holes
            for (int j = board.getHeight() - 1; j >= 0; j--) {
                Piece.PieceType piece = board.getGrid(i, j);
                if (piece != null) seenPiece = true;
                else if (seenPiece) holes++;
            }

        }

        return holes;

    }


    //Calculate a bumpiness score for the board
    public static double calculateBumpiness(TetrisBoard board) {

        double bumpiness = 0;

        //The score is the sum of squared differences of consecutive column heights
        int[] heights = board.getColHeight();
        for (int i = 0; i < board.getWidth() - 1; i++) {
            bumpiness += Math.abs(heights[i + 1] - heights[i]) * Math.abs(heights[i + 1] - heights[i]);
        }

        return bumpiness;

    }


    //Calculate the sum of all column heights in the board
    public static int calculateHeights(TetrisBoard board) {

        int heightSum = 0;
        for (int i = 0; i < board.getWidth(); i++) {
            heightSum += board.getColumnHeight(i);
        }
        return heightSum;

    }

}
