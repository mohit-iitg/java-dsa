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
    private boolean isDirected;
    private Map<Node<T>, List<Node<T>>> adjList;


    public GraphAdjList(int nodesCount, Node<T>[] nodes, Edge[] edges, boolean isDirected) {
        this.nodesCount = nodesCount;
        this.nodes = nodes;
        this.edges = edges;
        this.adjList = new HashMap<>();
        this.indexToNode = new HashMap<>();
        this.isDirected = isDirected;
        for(Node<T> node : nodes){
            this.indexToNode.put(node.getIndex(), node);
        }
        if(this.isDirected) {
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
            }
        } else {
            for (Edge edge : edges) {
                if(!this.indexToNode.containsKey(edge.getFrom()) || !this.indexToNode.containsKey(edge.getTo())) {
                    continue;
                }
                List<Node<T>> fromList, toList;
                if(!this.adjList.containsKey(this.indexToNode.get(edge.getFrom())) || !this.adjList.containsKey(this.indexToNode.get(edge.getTo()))) {
                    if(!this.adjList.containsKey(this.indexToNode.get(edge.getFrom()))) {
                        fromList = new ArrayList<>();
                    } else {
                        fromList = this.adjList.get(this.indexToNode.get(edge.getFrom()));
                    }
                    if(!this.adjList.containsKey(this.indexToNode.get(edge.getTo()))) {
                        toList = new ArrayList<>();
                    } else {
                        toList = this.adjList.get(this.indexToNode.get(edge.getTo()));
                    }
                } else {
                    fromList = this.adjList.get(this.indexToNode.get(edge.getFrom()));
                    toList = this.adjList.get(this.indexToNode.get(edge.getTo()));
                }
                fromList.add(this.indexToNode.get(edge.getTo()));
                this.adjList.put(this.indexToNode.get(edge.getFrom()), fromList);

                toList.add(this.indexToNode.get(edge.getFrom()));
                this.adjList.put(this.indexToNode.get(edge.getTo()), toList);
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
        Set<Node<T>> visited = new HashSet<>();
        for(Map.Entry<Node<T>, List<Node<T>>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                System.out.print("Start: ");
                bfsHelper(entry.getKey(), visited);
                System.out.println();
            }
        }
    }

    private void bfsHelper(Node<T> node, Set<Node<T>> visited) {
        Deque<Node<T>> q = new ArrayDeque<>();
        q.add(node);
        visited.add(node);
        while(!q.isEmpty()) {
            Node<T> curr = q.remove();
            System.out.print(curr+" -> ");
            List<Node<T>> list = this.adjList.get(curr);
            if(list != null) {
                for(Node<T> neighbour : list) {
                    if(!visited.contains(neighbour)) {
                        q.add(neighbour);
                        visited.add(neighbour);
                    }
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
        Set<Node<T>> visited = new HashSet<>();
        for(Map.Entry<Node<T>, List<Node<T>>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                Set<Node<T>> path = new HashSet<>();
                if(dfsDetectCycleDirected(entry.getKey(), visited, path)) return true;
            }
        }
        return false;
    }

    private boolean dfsDetectCycleDirected(Node<T> node, Set<Node<T>> visited, Set<Node<T>> path) {
        if(path.contains(node)) {
            return true;
        }
        if(visited.contains(node)) {
            return false;
        }
        path.add(node);
        List<Node<T>> neighbours = this.adjList.get(node);
        if(neighbours != null) {
            for(Node<T> neighbour : neighbours) {
                if(dfsDetectCycleDirected(neighbour, visited, path)) return true;
            }
        }
        path.remove(node);
        visited.add(node);
        return false;
    }


    private boolean containsCycleUndirected() {
        boolean containsCycle = false;
        Set<Node<T>> visited = new HashSet<>();
        for(Map.Entry<Node<T>, List<Node<T>>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                containsCycle = dfsDetectCycleUndirected(entry.getKey(), null, visited);
            }
            if(containsCycle) break;
        }
        return containsCycle;
    }

    private boolean dfsDetectCycleUndirected(Node<T> node, Node<T> parent, Set<Node<T>> visited) {
        visited.add(node);

        List<Node<T>> neighbours = this.adjList.get(node);
        if(neighbours != null) {
            for(Node<T> neighbour : neighbours) {
                if(!visited.contains(neighbour)) {
                    if(dfsDetectCycleUndirected(neighbour, node, visited)) {
                        return true;
                    }
                } else if(!neighbour.equals(parent)) {
                    return true;
                }
            }
        }
        return false;
    }
}
