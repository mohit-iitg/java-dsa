package org.javadsa.graph.adjmat;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;
import org.javadsa.util.disjointset.DisjointSet;

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
    public Double kruskalMst() {
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

        // In Kruskal's MST Algorithm, we need to sort the edges based on the weight
        // Keep on selecting the edges with the smallest weight unless the edge
        // is in between the nodes in same disjoint set
        Double mstValue = 0.0;
        DisjointSet ds = new DisjointSet(this.nodesCount);
        Arrays.sort(this.edges);
//        System.out.println("Sorted list of edges: ");
//        for(Edge edge : this.edges) {
//            System.out.printf("%f, ", edge.getWt());
//        }
        for(int i=0;i<this.edges.length;i++) {
            int from = this.edges[i].getFrom();
            int to = this.edges[i].getTo();
            if(ds.find(from) != ds.find(to)) {
                ds.union(from, to);
                mstValue+=this.edges[i].getWt();
            }
        }

        return mstValue;
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
    public Double[] dijkstras(Node<T> node) {
        Double[] costs = new Double[this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            costs[i] = Double.POSITIVE_INFINITY;
        }
        // If the graph contains a negative wt cycle, distance cannot be found
        if(containsNegativeCycle()) {
            costs[0] = Double.NEGATIVE_INFINITY;
            return costs;
        }

        // The algorithm is similar to Prim's algorithm, we consider the
        // overall cost of the complete path for each node
        int index = node.getIndex();
        boolean[] included = new boolean[this.nodesCount];
        costs[index] = 0.0;

        for(int i=1;i<this.nodesCount;i++) {
            // Select the node with the smallest cost
            int minCostIndex = -1;
            Double minCost = Double.POSITIVE_INFINITY;
            for(int j=0;j<this.nodesCount;j++) {
                if(!included[j] && minCost > costs[j]) {
                    minCostIndex = j;
                    minCost = costs[j];
                }
            }
            included[minCostIndex] = true;

            // Relax the edges wrt the new vertex
            Edge[] edges = this.adjMat[minCostIndex];
            for(Edge edge : edges) {
                if(edge != null) {
                    if (!included[edge.getTo()] && costs[edge.getFrom()] + edge.getWt() < costs[edge.getTo()]) {
                        costs[edge.getTo()] = costs[edge.getFrom()] + edge.getWt();
                    }
                    if(!isDirected) {
                        if (!included[edge.getFrom()] && costs[edge.getTo()] + edge.getWt() < costs[edge.getFrom()]) {
                            costs[edge.getFrom()] = costs[edge.getTo()] + edge.getWt();
                        }
                    }
                }
            }
        }

        return costs;
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

    private Double[][] floydWarshallSetup() {
        Double[][] dp = new Double[this.nodesCount][this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                if(this.adjMat[i][j] != null) {
                    dp[i][j] = this.adjMat[i][j].getWt();
                } else {
                    dp[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        return dp;
    }

    private void checkNegativeCycles(Double[][] dp) {
        // Consider nodes from 0, 1, ..., k-1
        for(int k=0;k<this.nodesCount;k++) {
            // Relax the distance from i to j via 0, 1, ..., k-1
            for(int i=0;i<this.nodesCount;i++) {
                for(int j=0;j<this.nodesCount;j++) {
                    if(dp[i][j] > dp[i][k] + dp[k][j]) {
                        dp[i][j] = Double.NEGATIVE_INFINITY;
                    }
                }
            }
        }
    }

    @Override
    public Double[][] floydWarshall() {
        // setup phase
        Double[][] dp = floydWarshallSetup();

        // Consider nodes from 0, 1, ..., k-1
        for(int k=0;k<this.nodesCount;k++) {
            // Relax the distance from i to j via 0, 1, ..., k-1
            for(int i=0;i<this.nodesCount;i++) {
                for(int j=0;j<this.nodesCount;j++) {
                    if(dp[i][j] > dp[i][k] + dp[k][j]) {
                        dp[i][j] = dp[i][k] + dp[k][j];
                    }
                }
            }
        }

        // Check for negative cycles
        checkNegativeCycles(dp);
        return dp;
    }

    @Override
    public int tarjansScc() {
        // If the graph is undirected, then all the connected components
        // are individually strongly connected.
        if(!this.isDirected) {
            return countNumberOfComponentsUndirected();
        }

        // Low link value: For a node in a directed graph, low link value is
        // the lowest id of node which can be visited by the node.
        // It could be possible that during a dfs the ids generated by the
        // dfs algorithm could be in an order that different component have
        // low link value from other component. We need to maintain a stack
        // for the nodes currently in consideration.

        Map<Integer, Integer> Ids = new HashMap<>();
        Map<Integer, Integer> LowLink = new HashMap<>();
        Stack<Integer> stk = new Stack<>();
        Set<Integer> onStack = new HashSet<>();
        Integer components = 0;
        Integer idCount = 0;
        for(int i=0;i<this.nodesCount;i++) {
            if(!Ids.containsKey(i)) {
                components+=dfsTarjansScc(i, Ids, LowLink, stk, onStack, idCount);
            }
        }

        return components;
    }

    private int countNumberOfComponentsUndirected() {
        boolean[] visited = new boolean[this.nodesCount];
        int components = 0;
        for(int i=0;i<nodesCount;i++) {
            for(int j=0;j<nodesCount;j++) {
                if(this.adjMat[i][j] != null && !visited[i]) {
                    components++;
                    dfsHelper(i, visited);
                    System.out.println();
                }
            }
        }
        return components;
    }

    private Integer dfsTarjansScc(Integer node, Map<Integer, Integer> ids, Map<Integer, Integer> lowLink, Stack<Integer> stk, Set<Integer> onStack, Integer idCount) {
        Integer component = 0;
        // For directed graph, we need to apply dfs with a few considerations.
        // 1. For a node, assign a new id
        ids.put(node, idCount);
        // 2. Assign the same id as its low link value. So the start would have the same id and low link value.
        lowLink.put(node, idCount);
        // 3. Add the node to stack.
        onStack.add(node);
        stk.add(node);
        // 4. Increase the id count.
        idCount++;
        // 5. For the neighbours of the node
        Edge[] edges = this.adjMat[node];
        for (Edge edge : edges) {
            if(edge == null) {
                continue;
            }
            Integer neighbour = edge.getTo();
            // 5a. if node is not visited, then we would give it a new id
            if (!ids.containsKey(neighbour)) {
                component = dfsTarjansScc(neighbour, ids, lowLink, stk, onStack, idCount);
            }
            // 5b. on backtracking, if we find that the neighbour was already on the stack
            // it could have a lower link value, so we resolve that with current node.
            if (onStack.contains(neighbour)) {
                lowLink.put(node, Math.min(lowLink.get(node), lowLink.get(neighbour)));
            }
        }
        // 6. If current node has same id and low link value, then it is starting of a component
        // We need to remove the nodes in the component till we have the last node.
        if(ids.get(node).equals(lowLink.get(node))) {
            for(Integer top = stk.peek();;top = stk.peek()) {
                stk.pop();
                onStack.remove(top);
                if(ids.get(top).equals(lowLink.get(top))) {
                    // 6a. Increase the component count as this is the last node
                    // to be removed from stack for this component
                    component++;
                    break;
                }
            }
        }
        return component;
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
