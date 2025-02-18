package org.javadsa.graph;

import java.util.List;

public interface GraphIntf<T> {
    public Node<T>[] getNodes();
    public Edge[] getEdges();
    public void dfs();
    public Node<T> dfsSearchByValue(T value);
    public void bfs();
    public Node<T> bfsSearchByValue(T value);
}