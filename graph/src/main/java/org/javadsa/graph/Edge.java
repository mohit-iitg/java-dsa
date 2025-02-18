package org.javadsa.graph;

public class Edge {
    private int from;
    private int to;
    private int wt;

    public Edge(int from, int to, int wt) {
        this.from = from;
        this.to = to;
        this.wt = wt;
    }

    public int getWt() {
        return wt;
    }

    public void setWt(int wt) {
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
}