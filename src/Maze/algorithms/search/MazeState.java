package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.Objects;

public class MazeState extends AState{
    private Position state;


    public MazeState(Position state) {
        super();
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || getClass()!=obj.getClass())
            return false;
        MazeState st = (MazeState)obj;
        if(obj!=null && this.state.getColumnIndex()==st.state.getColumnIndex() &&
                this.state.getRowIndex()==st.state.getRowIndex())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return state.toString();
    }

    public Position getState() {
        return state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state);
    }
}

