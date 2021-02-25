package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {

    @Override
    /**measures the time of creating a maze */
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start = System.currentTimeMillis();
        Maze maze = generate(rows, columns);
        long end = System.currentTimeMillis();
        return end - start;
    }
}
