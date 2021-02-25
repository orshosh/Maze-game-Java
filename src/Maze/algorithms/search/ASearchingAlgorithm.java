package algorithms.search;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{
    private int visited;

/**constructor*/
    public ASearchingAlgorithm() {
        this.visited = 0;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return visited;
    }



    public void setVisited(int visited) {
        this.visited = visited;
    }




}
