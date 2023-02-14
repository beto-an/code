package assignment;


//Helper class for representing one row in the Tetris board
public class BoardRow {


    //Store pieces in the row, number of filled squares in row, width of row
    private Piece.PieceType[] row;
    private int width, counter;


    //Initialize a row with a certain width
    public BoardRow(int width) {
        this.width = width;

        if (width < 0) row = null;
        else row = new Piece.PieceType[width];

        counter = 0;
    }


    //Set the ith entry in the row, update number of filled squares in row
    public void setEntry(int x, Piece.PieceType value) {
        if (x < 0 || x >= width) return;
        if (row[x] == null) counter++;
        row[x] = value;
    }


    //Return the PieceType of the ith entry in the row
    public Piece.PieceType getEntry(int x) {
        if (x < 0 || x >= width) return null;
        return row[x];
    }


    //If the number of filled entries is at least equal to the width, the row is filled
    public boolean isFilled() {
        return counter >= width;
    }


    //Precomputed number of filled entries
    public int getNumFilled() {
        return counter;
    }


    //Compare equality of two rows by comparing instance variables
    @Override
    public boolean equals(Object other) {

        //Confirm object is of type BoardRow
        if(!(other instanceof BoardRow)) return false;
        BoardRow otherRow = (BoardRow) other;

        //Confirm top level info is same
        if (this.width != otherRow.width) return false;
        if (this.counter != otherRow.counter) return false;

        //Compare all entries in the row
        for (int i = 0; i < width; i++) {
            if (this.getEntry(i) != otherRow.getEntry(i)) {
                return false;
            }
        }
        return true;

    }


    //Create a deep clone of the current BoardRow
    public BoardRow clone() {

        BoardRow clone = new BoardRow(width);
        for (int i = 0; i < width; i++) {
            Piece.PieceType currPiece = getEntry(i);
            if (currPiece == null) continue;
            clone.setEntry(i, currPiece);
        }
        return clone;

    }


    //Return a string representation of the BoardRow, inverted
    public String toString() {

        StringBuilder res = new StringBuilder("|");
        for (int i = row.length - 1; i >= 0; i--) {
            Piece.PieceType p = row[i];
            if (p == null) res.append("..|");
            else res.append("##|");
        }
        return res.toString();

    }


    //Effectively delete the elements in the row
    public void cleanup() {
        row = null;
    }


}
