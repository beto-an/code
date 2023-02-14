package assignment;

public class BoardRow {

    private Piece.PieceType[] row;
    private int width, counter;

    public BoardRow(int width) {
        this.width = width;
        row = new Piece.PieceType[width];
        counter = 0;
    }

    public void setEntry(int x, Piece.PieceType value) {
        if (x < 0 || x >= width) return;
        if (row[x] == null) counter++;
        row[x] = value;
    }

    public Piece.PieceType getEntry(int x) {
        if (x < 0 || x >= width) return null;
        return row[x];
    }

    public boolean isFilled() {
        return counter == width;
    }

    public int getNumFilled() {
        return counter;
    }

    @Override
    public boolean equals(Object other) {

        if(!(other instanceof BoardRow)) return false;
        BoardRow otherRow = (BoardRow) other;

        if (this.width != otherRow.width) return false;
        if (this.counter != otherRow.counter) return false;

        for (int i = 0; i < width; i++) {
            if (this.getEntry(i) != otherRow.getEntry(i)) {
                return false;
            }
        }
        return true;

    }

    public BoardRow clone() {
        BoardRow clone = new BoardRow(width);
        for (int i = 0; i < width; i++) {
            Piece.PieceType currPiece = getEntry(i);
            if (currPiece == null) continue;
            clone.setEntry(i, currPiece);
        }
        return clone;
    }

    public String toString() {
        StringBuilder res = new StringBuilder("|");
        for (int i = row.length - 1; i >= 0; i--) {
            Piece.PieceType p = row[i];
            if (p == null) res.append("..|");
            else res.append("##|");
        }
        return res.toString();
    }


    public void cleanup() {
        row = null;
    }
}
