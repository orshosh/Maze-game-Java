package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {
    @Override
    /**creating a random maze*/
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows,columns);
        int walls = (maze.getColumns() * maze.getRows()/3)*2;
        for(int counter = 0;counter<= walls;counter++){//rendom walls
            int row = (int)(Math.random()*maze.getRows());
            int column = (int)(Math.random()*maze.getColumns());
            maze.setValue(row,column,1);
        }
        maze.brakeWalls();

        return maze;
    }
}
