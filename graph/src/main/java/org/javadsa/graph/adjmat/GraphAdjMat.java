package org.javadsa.graph.adjmat;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;

public class GraphAdjMat<T> implements GraphIntf<T> {
    private int nodesCount;
    private Node<T>[] nodes;
    private Edge[] edges;

    private Edge[][] adjMat;

    public GraphAdjMat(int nodesCount, Node<T>[] nodes, Edge[] edges) {
        this.nodesCount = nodesCount;
        this.nodes = nodes;
        this.edges = edges;
        this.adjMat = new Edge[nodesCount][nodesCount];
        for (Edge edge : edges) {
            this.adjMat[edge.getFrom()][edge.getTo()] = edge;
        }
    }

    @Override
    public Node<T>[] getNodes() {
        return this.nodes;
    }

    @Override
    public Edge[] getEdges() {
        return this.edges;
    }

    @Override
    public void dfs() {
        boolean[] visited = new boolean[this.nodesCount];
        for(int i=0;i<nodesCount;i++) {
            for(int j=0;j<nodesCount;j++) {
                if(this.adjMat[i][j] != null && !visited[this.adjMat[i][j].getFrom()]) {
                    System.out.print("Start: ");
                    dfsHelper(this.adjMat[i][j].getFrom(), visited);
                    System.out.println();
                }
            }
        }
    }

    private void dfsHelper(int nodeIdx, boolean[] visited) {
        if(visited[nodeIdx]) {
            return;
        }
        visited[nodeIdx] = true;
        System.out.print(this.nodes[nodeIdx]+" -> ");
        for(int i=0;i<this.adjMat[nodeIdx].length;i++) {
            if(this.adjMat[nodeIdx][i] != null) {
                dfsHelper(this.adjMat[nodeIdx][i].getTo(), visited);
            }
        }
    }

    @Override
    public Node<T> dfsSearchByValue(T value) {
        return null;
    }

    @Override
    public void bfs() {

    }

    @Override
    public Node<T> bfsSearchByValue(T value) {
        return null;
    }
}
