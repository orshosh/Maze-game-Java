package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.ArrayList;

public class SearchableMaze implements ISearchable {
    private Maze maze;

    public SearchableMaze(Maze maze) {
        if(maze != null) {
            this.maze = maze;
        }else {
            this.maze = null;
        }
    }

    @Override
    public AState getStartState() {
        AState start = new MazeState(maze.getStartPosition());
        return start;
    }

    @Override
    public AState getGoalState() {
        AState goal = new MazeState(maze.getGoalPosition());
        return goal;
    }
    /**returns all the valid adjacent sells*/
    @Override
    public ArrayList<AState> getAllPossibleStates(AState s) {
        if(s==null)
            return null;
        MazeState ms=(MazeState)s;
        if(ms==null)
            return  null;
        ArrayList<AState>adj = new ArrayList<>();
        int row=ms.getState().getRowIndex();
        int col = ms.getState().getColumnIndex();
        adj=stateCheckerUpDown(adj,row-1,col);
        adj=stateCheckerRightLeft(adj,row,col+1);
        adj=stateCheckerUpDown(adj,row+1,col);
        adj=stateCheckerRightLeft(adj,row,col-1);

        return adj;
    }

    /**check all the sells above and under in specific sell*/
    private ArrayList<AState> stateCheckerUpDown(ArrayList adj,int row, int col){
        if(maze.checkAdj(row,col)) {
            Position p1 = new Position(row , col);
            MazeState m1 = new MazeState(p1);
            m1.setCost(10);
            adj.add(m1);
            if(maze.checkAdj(row,col+1)) {
                Position p2 = new Position(row, col+1);
                MazeState m2 = new MazeState(p2);
                if(!adj.contains(m2)) {
                    m2.setCost(15);
                    adj.add(m2);
                }
            }
            if(maze.checkAdj(row,col-1)) {
                Position p2 = new Position(row, col-1);
                MazeState m2 = new MazeState(p2);
                if(!adj.contains(m2)) {
                    m2.setCost(15);
                    adj.add(m2);
                }
            }

        }
        return adj;
    }

    /**check all the sells left and right in specific sell*/
    private ArrayList<AState> stateCheckerRightLeft(ArrayList adj,int row, int col){
        if(maze.checkAdj(row,col)) {
            Position p1 = new Position(row , col);
            MazeState m1 = new MazeState(p1);
            m1.setCost(10);
            adj.add(m1);
            if(maze.checkAdj(row-1,col)) {
                Position p2 = new Position(row-1, col);
                MazeState m2 = new MazeState(p2);
                if(!adj.contains(m2)) {
                    m2.setCost(15);
                    adj.add(m2);
                }
            }
            if(maze.checkAdj(row+1,col)) {
                Position p2 = new Position(row+1, col);
                MazeState m2 = new MazeState(p2);
                if(!adj.contains(m2)) {
                    m2.setCost(15);
                    adj.add(m2);
                }
            }

        }
        return adj;
    }


}
