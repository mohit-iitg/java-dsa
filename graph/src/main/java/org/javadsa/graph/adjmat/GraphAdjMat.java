package org.javadsa.graph.adjmat;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;

import java.util.*;

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

    @Override
    public Double primMst() {
        // Assuming MST is not applicable for Directed graphs
        // For it to be applicable to directed graph, we need to have one node
        // such that all the other vertices are reachable from that to start algorithm from.
        if(this.isDirected) {
            System.out.println("MST only implemented for undirected graph");
            return Double.NEGATIVE_INFINITY;
        }
        // We need the graph to be connected first
        if(!this.isConnected()) {
            System.out.println("MST only implemented for connected graph");
            return Double.NEGATIVE_INFINITY;
        }
        // In Prim's algorithm, we start from a vertex and assume the weight of that vertex to be 0
        // We calculate the weight of the vertices connected to the veritces which are in the MST
        // We choose the vertex with the least weight
        Double[] mstWt = new Double[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            mstWt[i] = Double.POSITIVE_INFINITY;
        }
        boolean[] inMST = new boolean[this.nodesCount];
        int[] parent = new int[this.nodesCount];
        int inMSTCount = 0;
        mstWt[0] = 0.0; parent[0] = -1;
        Double mstValue = 0.0;
        while(inMSTCount < this.nodesCount) {
            // Select minimum wt vertex next
            int minWtIdx = -1;
            Double minWt = Double.POSITIVE_INFINITY;
            for(int i=0;i<this.nodesCount;i++) {
                if(!inMST[i] && mstWt[i] < minWt) {
                    minWtIdx = i;
                    minWt = mstWt[i];
                }
            }
            // Add index to MST
            inMST[minWtIdx] = true;
            inMSTCount++;
            mstValue+=mstWt[minWtIdx];
            // Relax weights based on added vertex
            for(int i=0;i<this.nodesCount;i++) {
                if(!inMST[i] && this.adjMat[minWtIdx][i] != null && this.adjMat[minWtIdx][i].getWt() < mstWt[i]) {
                    mstWt[i] = this.adjMat[minWtIdx][i].getWt();
                    parent[i] = minWtIdx;
                }
            }
        }
        System.out.print("Parent indices for MST: ");
        for (int parentIdx : parent) {
            System.out.printf("%d, ", parentIdx);
        }
        return mstValue;
    }

    @Override
    public Double[] bellmanFord(Node<T> node) {
        // The idea is basically to maintain the total cost of reaching
        // a node from the starting node by relaxing the cost on processing
        // each edge in the graph.
        Double[] cost = new Double[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            cost[i] = Double.POSITIVE_INFINITY;
        }
        // Starting with the node
        int index = node.getIndex();
        cost[index] = 0.0;
        for(int i=0;i<this.nodesCount;i++) {
            // Relax cost wih all the edges
            for(Edge edge : this.edges) {
                if(cost[edge.getFrom()] + edge.getWt() < cost[edge.getTo()]) {
                    cost[edge.getTo()] = cost[edge.getFrom()] + edge.getWt();
                }
                if(!isDirected) {
                    if(cost[edge.getTo()] + edge.getWt() < cost[edge.getFrom()]) {
                        cost[edge.getFrom()] = cost[edge.getTo()] + edge.getWt();
                    }
                }
            }
        }
        // Check one more time for negative cycles
        for(Edge edge : this.edges) {
            if(cost[edge.getFrom()] + edge.getWt() < cost[edge.getTo()]) {
                cost[0] = Double.NEGATIVE_INFINITY;
                break;
            }
            if(!isDirected) {
                if(cost[edge.getTo()] + edge.getWt() < cost[edge.getFrom()]) {
                    cost[0] = Double.NEGATIVE_INFINITY;
                    break;
                }
            }
        }
        return cost;
    }

    @Override
    public boolean containsNegativeCycle() {
        if(bellmanFord(this.nodes[0])[0] == Double.NEGATIVE_INFINITY) {
            return true;
        }
        return false;
    }

    // Assuming the graph is undirected, check whether the graph is connected
    private boolean isConnected() {
        boolean[] visited = new boolean[this.nodesCount];
        this.dfsHelper(0, visited);
        for(boolean isVisited : visited) {
            if(!isVisited) {
                return false;
            }
        }
        return true;
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
