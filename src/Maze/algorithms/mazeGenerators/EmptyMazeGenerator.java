package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    @Override
    /** creats an empty maze*/
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows,columns);
        return maze;
    }
}
