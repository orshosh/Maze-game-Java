package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;

public class DepthFirstSearch extends ASearchingAlgorithm {

    /**solve a maze according by DepthFirstSearch algorithm*/

    @Override
    public Solution solve(ISearchable s) {
        if(s==null)
            return null;
        ArrayList<AState> list = new ArrayList<>();
        HashSet<AState>visited = new HashSet<>();
        AState start = s.getStartState();
        AState goal = s.getGoalState();
        int counter=0;
        AState temp=s.getStartState();
        list.add(s.getStartState());
        while(!list.isEmpty()){
            temp = list.remove(0);
            visited.add(temp);
            counter++;
            if(temp.equals(goal))
                break;
            ArrayList<AState> nei = s.getAllPossibleStates(temp);
            while(!nei.isEmpty()){
                AState adj = nei.remove(0);
                if(!visited.contains(adj)){
                    list.add(0,adj);
                    adj.setCameFrom(temp);
            }
            }
        }
        setVisited(counter);
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
        return "DepthFirstSearch";
    }
}
