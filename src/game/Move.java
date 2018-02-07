package game;

import java.util.Objects;

public class Move {
    private int row;
    private int column;

    public Move(int row, int column) {
        if (row < 0 || row > 7 || column < 0 || column > 7) {
            throw new IllegalArgumentException("Invalid row/column");
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row &&
                column == move.column;
    }

    @Override
    public int hashCode() {

        return Objects.hash(row, column);
    }
}
