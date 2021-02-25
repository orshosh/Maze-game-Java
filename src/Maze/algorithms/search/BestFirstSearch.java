package algorithms.search;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class BestFirstSearch extends ASearchingAlgorithm {

    /**solve a maze according by BestFirstSearch algorithm*/

    @Override
    public Solution solve(ISearchable s) {
        if(s==null){
            return null;
        }
        PriorityQueue<AState> PQueue = new PriorityQueue<>();
        ArrayList<AState> visited = new ArrayList<>();
        AState temp=s.getStartState();
        PQueue.add(temp);
        int counter =0;
        while(!PQueue.isEmpty()) {
                temp = PQueue.poll();
            if (temp.equals(s.getGoalState())) {
                break;
            }
            if (!visited.contains(temp)) {
                visited.add(temp);
                counter++;
                for (AState adj : s.getAllPossibleStates(temp)) {
                    PQueue.add(adj);
                    adj.setCameFrom(temp);
                }
            }
        }
        setVisited(counter);
        ArrayList<AState> path = new ArrayList<>();
        while(!temp.equals(s.getStartState())){
            path.add(0,temp.getCameFrom());
            temp=temp.getCameFrom();
        }
        Solution sol = new Solution(path);
        return sol;
    }


    @Override
    public String getName() {
        return "BestFirstSearch";
    }



}
