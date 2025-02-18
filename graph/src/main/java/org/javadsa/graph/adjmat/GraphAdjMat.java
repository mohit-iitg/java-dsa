package org.javadsa.graph.adjmat;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;

import java.util.ArrayDeque;
import java.util.Deque;

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
                if(this.adjMat[i][j] != null && !visited[i]) {
                    System.out.print("Start: ");
                    dfsHelper(i, visited);
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
                dfsHelper(i, visited);
            }
        }
    }

    @Override
    public Node<T> dfsSearchByValue(T value) {
        return null;
    }

    @Override
    public void bfs() {
        boolean[] visited = new boolean[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                if(this.adjMat[i][j] != null && !visited[i]) {
                    System.out.print("Start: ");
                    bfsHelper(i, visited);
                    System.out.println();
                }
            }
        }
    }

    private void bfsHelper(int nodeIdx, boolean[] visited) {
        Deque<Integer> q = new ArrayDeque<>();
        q.add(nodeIdx);
        while(!q.isEmpty()) {
            Integer currIdx = q.remove();
            visited[currIdx] = true;
            System.out.print(this.nodes[currIdx]+" -> ");
            for(int i=0;i<this.nodesCount;i++) {
                if(this.adjMat[currIdx][i] != null && !visited[i]) {
                    q.add(i);
                }
            }
        }
    }

    @Override
    public Node<T> bfsSearchByValue(T value) {
        return null;
    }
}
