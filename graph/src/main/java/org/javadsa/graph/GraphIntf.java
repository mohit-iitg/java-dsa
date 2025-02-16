package org.javadsa.graph;

import java.util.List;

public interface GraphIntf<T> {
    T addNode(T node);
    void addNodeWithEdges(T node, List<T> edges);
    T addNode();
    void addEdge(T from, T to);
    void printGraph();
    void dfs();
//    void dfsSearchNode(T node);
}