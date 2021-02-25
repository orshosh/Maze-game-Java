package algorithms.search;

import java.util.ArrayList;

public interface ISearchingAlgorithm {
    Solution solve(ISearchable s);
    int getNumberOfNodesEvaluated();
    String getName();
}

