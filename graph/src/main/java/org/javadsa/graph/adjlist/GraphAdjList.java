package org.javadsa.graph.adjlist;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;

import java.util.*;

public class GraphAdjList<T> implements GraphIntf<T> {
    private int nodesCount;
    private Node<T>[] nodes;
    private Edge[] edges;
    private Map<Integer, Node<T>> indexToNode;

    private Map<Node<T>, List<Node<T>>> adjList;


    public GraphAdjList(int nodesCount, Node<T>[] nodes, Edge[] edges) {
        this.nodesCount = nodesCount;
        this.nodes = nodes;
        this.edges = edges;
        this.adjList = new HashMap<>();
        this.indexToNode = new HashMap<>();
        for(Node<T> node : nodes){
            this.indexToNode.put(node.getIndex(), node);
        }
        for (Edge edge : edges) {
            if(!this.indexToNode.containsKey(edge.getFrom()) || !this.indexToNode.containsKey(edge.getTo())) {
                continue;
            }
            if(!this.adjList.containsKey(this.indexToNode.get(edge.getFrom()))) {
                List<Node<T>> arrList = new ArrayList<>();
                arrList.add(this.indexToNode.get(edge.getTo()));
                this.adjList.put(this.indexToNode.get(edge.getFrom()), arrList);
            } else {
                List<Node<T>> newList = this.adjList.get(this.indexToNode.get(edge.getFrom()));
                newList.add(this.indexToNode.get(edge.getTo()));
                this.adjList.put(this.indexToNode.get(edge.getFrom()), newList);
            }
//            System.out.println("Updated list of "+this.indexToNode.get(edge.getFrom())+" to: "+this.adjList.get(this.indexToNode.get(edge.getFrom())));
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
        Set<Node<T>> visited = new HashSet<>();

        for(Map.Entry<Node<T>, List<Node<T>>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                System.out.print("Start: ");
                dfsHelper(entry.getKey(), visited);
                System.out.println();
            }
        }
    }

    private void dfsHelper(Node<T> node, Set<Node<T>> visited) {
        if(visited.contains(node)) {
            return;
        }
        visited.add(node);
        System.out.print(node + " -> ");
        List<Node<T>> list = this.adjList.get(node);
        if(list == null) {
            return;
        }
        for(Node<T> neighbour : list) {
            dfsHelper(neighbour, visited);
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
