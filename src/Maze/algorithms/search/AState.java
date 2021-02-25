package algorithms.search;

import java.io.Serializable;

public  abstract class AState implements Comparable<AState>, Serializable {
    private double cost;
    private AState cameFrom;

    @Override
    public int compareTo(AState o) {
        if (this.equals(o)) return 0;
        if(o.cost>this.cost)
            return 1;
        else
            return -1;
    }

    public AState getCameFrom() {
        return cameFrom;
    }

    public AState() {
        this.cost = 0;
        this.cameFrom = null;
    }

    public void setCameFrom(AState cameFrom) {
        this.cameFrom = cameFrom;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
