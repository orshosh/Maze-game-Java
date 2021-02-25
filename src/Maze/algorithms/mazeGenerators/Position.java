package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    private int RowIndex;
    private  int ColumnIndex;

    public Position(int RowIndex, int ColumnIndex) {
        this.RowIndex = RowIndex;
        this.ColumnIndex = ColumnIndex;
    }

    public int getRowIndex() {
        return RowIndex;
    }

    public int getColumnIndex() {
        return ColumnIndex;
    }

    @Override
    public String toString() {
        return "{"+RowIndex+","+ColumnIndex+"}";
    }


    public void setPosition(int rowIndex , int columnIndex) {
        RowIndex = rowIndex;
        ColumnIndex = columnIndex;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getRowIndex() == position.getRowIndex() &&
                getColumnIndex() == position.getColumnIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowIndex(), getColumnIndex());
    }
}
