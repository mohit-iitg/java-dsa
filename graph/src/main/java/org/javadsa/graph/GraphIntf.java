package org.javadsa.graph;

import java.util.List;

public interface GraphIntf<T> {
    public boolean isDirected();
    public Node<T>[] getNodes();
    public Edge[] getEdges();
    public void dfs();
    public Node<T> dfsSearchByValue(T value);
    public void bfs();
    public Node<T> bfsSearchByValue(T value);
    public boolean containsCycle();
//    // Using minimum weight edge adding to the set to get the MST
    public Double kruskalMst();
    // Using the edges currently available to select the smallest weight edge.
    public Double primMst();
//    // Single source shortest path to all the other nodes.
//    // Cannot work in case we have a negative weight cycle
    public Double[] dijkstras(Node<T> node);
    // Single source shortest path to all other nodes.
    // Can detect a negative weight cycle
    public Double[] bellmanFord(Node<T> node);
    // Check negative wt cycle
    public boolean containsNegativeCycle();
//    // All source shortest path algorithm
    public Double[][] floydWarshall();
//    // Detect all strongly connected components
//    public void tarjansScc();
//    // Detect all strongly connected components
//    public void kosarajuScc();
//    // Topological sorting
//    public Integer[] topologicalSort();
}