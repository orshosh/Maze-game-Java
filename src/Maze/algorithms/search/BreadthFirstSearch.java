package algorithms.search;

import sun.misc.Queue;

import java.util.ArrayList;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    /**solve a maze according by BreadthFirstSearch algorithm*/


    @Override
    public Solution solve(ISearchable s) {
        if(s==null){
            return null;
        }
        Queue<AState> queue = new Queue<>();
        ArrayList <AState> visited = new ArrayList<>();
        queue.enqueue(s.getStartState());
        AState temp=s.getStartState();
        int counter =0;
        while(!queue.isEmpty()) {
            try {
                temp = queue.dequeue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (temp.equals(s.getGoalState())) {
                break;
            }
            if (!visited.contains(temp)) {
                visited.add(temp);
                counter++;
                for (AState adj : s.getAllPossibleStates(temp)) {
                    queue.enqueue(adj);
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

    public String getName(){
        return "BreadthFirstSearch";
    }
}
