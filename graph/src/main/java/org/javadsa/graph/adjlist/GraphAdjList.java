package org.javadsa.graph.adjlist;

import org.javadsa.graph.GraphIntf;

import java.util.*;

public class GraphAdjList<T> implements GraphIntf<T> {
    private Map<T, List<T>> adjList;

    public GraphAdjList() {
        this.adjList = new HashMap<>();
    }

    private void printList(List<T> list) {
        for(T node : list) {
            System.out.print(node + " -> ");
        }
    }

    @Override
    public T addNode(T node) {
        this.adjList.put(node, new ArrayList<>());
        return node;
    }

    @Override
    public void addNodeWithEdges(T node, List<T> edges) {
        if(edges.isEmpty()) {
            this.addNode(node);
        }
        this.adjList.put(node, edges);
    }

    @Override
    public T addNode() {
        // NOT IMPLEMENTED FOR ADJ LIST GRAPH
        return null;
    }

    @Override
    public void addEdge(T from, T to) {
        if(this.adjList.containsKey(from)) {
            List<T> neighbours = this.adjList.get(from);
            neighbours.add(to);
        } else {
            this.addNodeWithEdges(from, Collections.singletonList(to));
        }
    }

    @Override
    public void printGraph() {
        for(Map.Entry<T, List<T>> entry : this.adjList.entrySet()) {
            System.out.print(entry.getKey() + " -> ");
            this.printList(entry.getValue());
            System.out.println();
        }
    }

    @Override
    public void dfs() {
        Set<T> visited = new HashSet<>();
        for(Map.Entry<T, List<T>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                System.out.print("\nStarting: ");
                dfsHelper(entry.getKey(), visited);
            }
        }
    }

    @Override
    public boolean dfsSearchNode(T node) {
        Set<T> visited = new HashSet<>();
        boolean foundNode = false;
        for(Map.Entry<T, List<T>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                foundNode |= dfsSearchHelper(entry.getKey(), visited, node);
            }
        }
        return foundNode;
    }

    private boolean dfsSearchHelper(T initNode, Set<T> visited, T targetNode) {
        if(visited.contains(initNode)) {
            return false;
        }
        visited.add(initNode);
        if(targetNode.equals(initNode)) {
            return true;
        }
        boolean foundNode = false;
        for(T node : this.adjList.get(initNode)) {
            foundNode |= dfsSearchHelper(node, visited, targetNode);
        }
        return foundNode;
    }

    private void dfsHelper(T initNode, Set<T> visited) {
        if(visited.contains(initNode)) {
            return;
        }
        System.out.print(initNode + " -> ");
        visited.add(initNode);
        for(T node : this.adjList.get(initNode)) {
            dfsHelper(node, visited);
        }
    }
}