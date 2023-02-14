package assignment;

import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardState implements State {

    private TetrisBoard board;

    public BoardState(TetrisBoard b) {
        board = b;
    }

    @Override
    public List<Object> variableKeys() {
        return new ArrayList<>();
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public State copy() {
        return new BoardState(board);
    }

    public TetrisBoard getTetrisBoard() {
        return board;
    }

    public boolean terminal() {
        return board.getMaxHeight() > 20;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardState)) {
            return false;
        }
        BoardState that = (BoardState) obj;
        return that.board.equals(this.board);
    }
    
    public ArrayList<int[]> getInputs() {

        ArrayList<int[]> list = new ArrayList<>();
        ArrayList<BoardRow> grid = board.getGrid();

        int width = board.getWidth();
        for (BoardRow row : grid) {
            int[] vals = new int[width];
            for (int i = 0; i < width; i++) {
                if (row.getEntry(i) != null) vals[i] = 1;
            }
            list.add(vals);
        }
        list.add(Arrays.copyOf(board.getColHeight(), width));

        int[] holeArray = new int[width];
        Arrays.fill(holeArray, 20);
        for (int i = 0; i < width; i++) {
            boolean seenPiece = false;
            for (int j = board.getHeight() - 1; j >= 0; j--) {
                Piece.PieceType piece = board.getGrid(i, j);
                if (piece != null) seenPiece = true;
                else if (seenPiece) holeArray[i] = j;
            }
        }
        list.add(holeArray);

        int[] pieceType = new int[7];
        switch(board.getCurrentPiece().getType()) {
            case T:
                pieceType[0] = 1;
                break;
            case SQUARE:
                pieceType[1] = 1;
                break;
            case STICK:
                pieceType[2] = 1;
                break;
            case LEFT_L:
                pieceType[3] = 1;
                break;
            case RIGHT_L:
                pieceType[4] = 1;
                break;
            case LEFT_DOG:
                pieceType[5] = 1;
                break;
            case RIGHT_DOG:
                pieceType[6] = 1;
                break;
        }
        list.add(pieceType);

        return list;

    }

    public void cleanup() {
        board.cleanup();
        board = null;
    }
    
}
