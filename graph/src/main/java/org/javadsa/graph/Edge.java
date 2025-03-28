package org.javadsa.graph;

import java.util.Comparator;
import java.util.Objects;

public class Edge implements Comparable {
    private int from;
    private int to;
    private Double wt;

    public Edge(int from, int to, Double wt) {
        this.from = from;
        this.to = to;
        this.wt = wt;
    }

    public Double getWt() {
        return wt;
    }

    public void setWt(Double wt) {
        this.wt = wt;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Edge edge)) {
            return -1;
        }
        if(this.getWt() < edge.getWt()) {
            return -1;
        } else if(Objects.equals(this.getWt(), edge.getWt())) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return String.format("Edge(from=%d, to=%d, wt=%.1f)", this.from, this.to, this.wt);
    }
}