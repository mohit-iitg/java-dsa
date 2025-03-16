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
    private Map<Node<T>, List<Edge>> adjList;


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
                Node<T> fromNode = this.indexToNode.get(edge.getFrom());
                if(!this.adjList.containsKey(fromNode)) {
                    List<Edge> arrList = new ArrayList<>();
                    arrList.add(edge);
                    this.adjList.put(fromNode, arrList);
                } else {
                    List<Edge> newList = this.adjList.get(fromNode);
                    newList.add(edge);
                    this.adjList.put(fromNode, newList);
                }
            }
        } else {
            for (Edge edge : edges) {
                if(!this.indexToNode.containsKey(edge.getFrom()) || !this.indexToNode.containsKey(edge.getTo())) {
                    continue;
                }
                List<Edge> fromList, toList;
                Node<T> fromNode = this.indexToNode.get(edge.getFrom());
                Node<T> toNode = this.indexToNode.get(edge.getTo());
                if(!this.adjList.containsKey(fromNode) || !this.adjList.containsKey(toNode)) {
                    if(!this.adjList.containsKey(fromNode)) {
                        fromList = new ArrayList<>();
                    } else {
                        fromList = this.adjList.get(fromNode);
                    }
                    if(!this.adjList.containsKey(toNode)) {
                        toList = new ArrayList<>();
                    } else {
                        toList = this.adjList.get(toNode);
                    }
                } else {
                    fromList = this.adjList.get(fromNode);
                    toList = this.adjList.get(toNode);
                }
                fromList.add(edge);
                this.adjList.put(fromNode, fromList);

                Edge newEdge = new Edge(edge.getTo(), edge.getFrom(), edge.getWt());
                toList.add(newEdge);
                this.adjList.put(toNode, toList);
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

        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
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
        List<Edge> list = this.adjList.get(node);
        if(list == null) {
            return;
        }
        for(Edge edge : list) {
            Node<T> neighbour = this.indexToNode.get(edge.getTo());
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
        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
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
            List<Edge> list = this.adjList.get(curr);
            if(list != null) {
                for(Edge edge : list) {
                    Node<T> neighbour = this.indexToNode.get(edge.getTo());
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
        // We calculate the weight of the vertices connected to the vertices which are in the MST
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
            for(Edge edge : this.adjList.get(this.nodes[minWtIdx])) {
                Node<T> neighbour = this.indexToNode.get(edge.getTo());
                if(!inMST[neighbour.getIndex()] && edge.getWt() < mstWt[neighbour.getIndex()]) {
                    mstWt[neighbour.getIndex()] = edge.getWt();
                    parent[neighbour.getIndex()] = minWtIdx;
                }
            }
        }
        System.out.print("Parent indices for MST: ");
        for (int parentIdx : parent) {
            System.out.printf("%d, ", parentIdx);
        }
        return mstValue;
    }
    // Assuming the graph is undirected, check whether the graph is connected
    private boolean isConnected() {
        Set<Node<T>> visited = new HashSet<>();
        this.dfsHelper(this.nodes[0], visited);
        if(visited.size() < this.nodesCount) {
            return false;
        }
        return true;
    }

    private boolean containsCycleDirected() {
        Set<Node<T>> visited = new HashSet<>();
        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
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
        List<Edge> edges = this.adjList.get(node);
        if(edges != null) {
            for(Edge edge : edges) {
                Node<T> neighbour = this.indexToNode.get(edge.getTo());
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
        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                containsCycle = dfsDetectCycleUndirected(entry.getKey(), null, visited);
            }
            if(containsCycle) break;
        }
        return containsCycle;
    }

    private boolean dfsDetectCycleUndirected(Node<T> node, Node<T> parent, Set<Node<T>> visited) {
        visited.add(node);

        List<Edge> edges = this.adjList.get(node);
        if(edges != null) {
            for(Edge edge : edges) {
                Node<T> neighbour = this.indexToNode.get(edge.getTo());
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
