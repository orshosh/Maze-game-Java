package algorithms.mazeGenerators;

public class MyMazeGenerator extends AMazeGenerator {
    @Override
    /**crating a random maze according to DFS algorithm*/
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows,columns);
        maze.fullMaze();
        maze.DFS();

        return maze;
    }
}
