package org.javadsa.graph.adjmat;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class GraphAdjMat<T> implements GraphIntf<T> {
    private int nodesCount;
    private Node<T>[] nodes;
    private Edge[] edges;
    private boolean isDirected;
    private Edge[][] adjMat;

    public GraphAdjMat(int nodesCount, Node<T>[] nodes, Edge[] edges, boolean isDirected) {
        this.nodesCount = nodesCount;
        this.nodes = nodes;
        this.edges = edges;
        this.adjMat = new Edge[nodesCount][nodesCount];
        this.isDirected = isDirected;

        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                this.adjMat[i][j] = null;
            }
        }

        if(this.isDirected) {
            for (Edge edge : edges) {
                this.adjMat[edge.getFrom()][edge.getTo()] = edge;
            }
        } else {
            for (Edge edge : edges) {
                this.adjMat[edge.getFrom()][edge.getTo()] = edge;
                this.adjMat[edge.getTo()][edge.getFrom()] = edge;
            }
        }

    }

    @Override
    public boolean isDirected() {
        return this.isDirected;
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
        visited[nodeIdx] = true;
        while(!q.isEmpty()) {
            Integer currIdx = q.remove();
            System.out.print(this.nodes[currIdx]+" -> ");
            for(int i=0;i<this.nodesCount;i++) {
                if(this.adjMat[currIdx][i] != null && !visited[i]) {
                    q.add(i);
                    visited[i] = true;
                }
            }
        }
    }

    @Override
    public Node<T> bfsSearchByValue(T value) {
        return null;
    }

    @Override
    public boolean containsCycle() {
        if(this.isDirected) {
            return this.containsCycleDirected();
        } else {
            return this.containsCycleUndirected();
        }
    }

    private boolean containsCycleDirected() {
        boolean[] visited = new boolean[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                if(this.adjMat[i][j] != null && !visited[i]) {
                    Set<Integer> path = new HashSet<>();
                    if(dfsContainsCycleDirected(i, visited, path)) return true;
                }
            }
        }
        return false;
    }

    private boolean dfsContainsCycleDirected(int nodeIdx, boolean[] visited, Set<Integer> path) {
        if(path.contains(nodeIdx)) {
            return true;
        }
        if(visited[nodeIdx]) {
            return false;
        }
        path.add(nodeIdx);
        visited[nodeIdx] = true;
        for(int i=0;i<this.nodesCount;i++) {
            if(this.adjMat[nodeIdx][i] != null) {
                if(dfsContainsCycleDirected(i, visited, path)) return true;
            }
        }
        path.remove(nodeIdx);
        return false;
    }

    private boolean containsCycleUndirected() {
        boolean[] visited = new boolean[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                if(this.adjMat[i][j] != null && !visited[i]) {
                    if(dfsContainsCycleUndirected(i, -1, visited)) return true;
                }
            }
        }
        return false;
    }

    private boolean dfsContainsCycleUndirected(int nodeIdx, int parent, boolean[] visited) {
        visited[nodeIdx] = true;
        for(int i=0;i<this.nodesCount;i++) {
            if (this.adjMat[nodeIdx][i] != null) {
                if (!visited[i]) {
                    if (dfsContainsCycleUndirected(i, nodeIdx, visited)) return true;
                } else if (this.adjMat[nodeIdx][i] != null && visited[i] && i != parent) {
                    return true;
                }
            }
        }
        return false;
    }

}
