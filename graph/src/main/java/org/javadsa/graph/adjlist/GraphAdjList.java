package org.javadsa.graph.adjlist;

import org.javadsa.graph.Edge;
import org.javadsa.graph.GraphIntf;
import org.javadsa.graph.Node;
import org.javadsa.util.disjointset.DisjointSet;

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

            System.out.println("minCostIndex: "+minCostIndex);
            // Relax the edges wrt the new vertex
            List<Edge> edges = this.adjList.get(this.indexToNode.get(minCostIndex));
            if(edges != null) {
                for (Edge edge : edges) {
                    if (!included[edge.getTo()] && costs[edge.getFrom()] + edge.getWt() < costs[edge.getTo()]) {
                        costs[edge.getTo()] = costs[edge.getFrom()] + edge.getWt();
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

    private Double[][] floydWarshallSetup() {
        Double[][] dp = new Double[this.nodesCount][this.nodesCount];
        for(int i=0;i<this.nodesCount;i++) {
            for(int j=0;j<this.nodesCount;j++) {
                dp[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for(Node<T> node:this.nodes) {
            List<Edge> edges = this.adjList.get(node);
            if(edges != null) {
                for(Edge edge : edges) {
                    dp[edge.getFrom()][edge.getTo()] = edge.getWt();
                }
            }
        }
        return dp;
    }

    @Override
    public Double[][] floydWarshall() {
        // Setup
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

        Map<Node<T>, Integer> Ids = new HashMap<>();
        Map<Node<T>, Integer> LowLink = new HashMap<>();
        Stack<Node<T>> stk = new Stack<>();
        Set<Node<T>> onStack = new HashSet<>();
        Integer components = 0;
        Integer idCount = 0;
        for(Node<T> node : this.nodes) {
            if(!Ids.containsKey(node)) {
                components+=dfsTarjansScc(node, Ids, LowLink, stk, onStack, idCount);
            }
        }

        return components;
    }

    @Override
    public int kosarajuScc() {
        // If the graph is undirected, then all the connected components
        // are individually strongly connected.
        if(!this.isDirected) {
            return countNumberOfComponentsUndirected();
        }

        int components = 0;
        // 1. Use dfs to find the order of nodes.
        // In the current graph, use dfs traversal to find the order
        // in which the nodes are to be considered in the components
        // with edges reversed.
        Stack<Node<T>> stk = new Stack<>();
        dfsKosarajuFirstPass(stk);

        // 2. Find the transpose of the current graph
        Map<Node<T>, List<Edge>> transposeAdjList = new HashMap<>();
        for(Node<T> node : this.nodes) {
            transposeAdjList.put(node, new ArrayList<>());
        }
        for(Map.Entry<Node<T>, List<Edge>> mapEntry : this.adjList.entrySet()) {
            List<Edge> edges = mapEntry.getValue();
            if(edges!=null) {
                for(Edge edge : edges) {
                    Node<T> toNode = this.indexToNode.get(edge.getTo());
                    List<Edge> currList = transposeAdjList.get(toNode);
                    Edge newEdge = new Edge(edge.getTo(), edge.getFrom(), edge.getWt());
                    currList.add(newEdge);
                    transposeAdjList.put(toNode, currList);
                }
            }
        }

        // 3. Take the nodes in the order present in the stack with
        // the transpose
        Set<Node<T>> visited = new HashSet<>();
        while(!stk.isEmpty()) {
            Node<T> top = stk.peek();
            dfsKosarajuSecondPass(top, visited, transposeAdjList);
            components++;
            while(!stk.isEmpty() && visited.contains(stk.peek())) {
                stk.pop();
            }
        }
        return components;
    }

    @Override
    public Integer[] topologicalSort() {
        if(!this.isDirected) {
            System.out.println("Topological sort is applicable for directed graph");
            return null;
        }
        if(this.containsCycle()) {
            System.out.println("Topological sort is applicable for acyclic graph");
            return null;
        }
        Stack<Node<T>> stk = new Stack<>();
        Set<Node<T>> visited = new HashSet<>();

        for(Node<T> node:this.nodes) {
            if(!visited.contains(node)) {
                dfsTopologicalHelper(node, visited, stk);
            }
        }

        Integer[] result = new Integer[this.nodesCount];
        int i=0;
        while(!stk.isEmpty()) {
            result[i++] = stk.pop().getIndex();
        }
        return result;
    }

    private void dfsTopologicalHelper(Node<T> node, Set<Node<T>> visited, Stack<Node<T>> stk) {
        if(visited.contains(node)) {
            return;
        }
        visited.add(node);
        List<Edge> edges = this.adjList.get(node);
        if(edges!=null) {
            for(Edge edge:edges) {
                Node<T> toNode = this.indexToNode.get(edge.getTo());
                if(!visited.contains(toNode)) {
                    dfsTopologicalHelper(toNode, visited, stk);
                }
            }
        }

        stk.push(node);
    }

    private void dfsKosarajuSecondPass(Node<T> node, Set<Node<T>> visited, Map<Node<T>, List<Edge>> transposeAdjList) {
        if(visited.contains(node)) {
            return;
        }
        visited.add(node);
        List<Edge> list = transposeAdjList.get(node);
        if(list == null) {
            return;
        }
        for(Edge edge : list) {
            Node<T> neighbour = this.indexToNode.get(edge.getTo());
            dfsKosarajuSecondPass(neighbour, visited, transposeAdjList);
        }
    }

    private void dfsKosarajuFirstPass(Stack<Node<T>> stk) {
        Set<Node<T>> visited = new HashSet<>();
        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                dfsKosarajuHelper(entry.getKey(), visited, stk);
            }
        }
    }

    private void dfsKosarajuHelper(Node<T> node, Set<Node<T>> visited, Stack<Node<T>> stk) {
        if(visited.contains(node)) {
            return;
        }
        visited.add(node);
        List<Edge> list = this.adjList.get(node);
        if(list == null) {
            stk.push(node);
            return;
        }
        for(Edge edge : list) {
            Node<T> neighbour = this.indexToNode.get(edge.getTo());
            dfsKosarajuHelper(neighbour, visited, stk);
        }
        stk.push(node);
    }

    private int countNumberOfComponentsUndirected() {
        Set<Node<T>> visited = new HashSet<>();
        int components = 0;
        for(Map.Entry<Node<T>, List<Edge>> entry : this.adjList.entrySet()) {
            if(!visited.contains(entry.getKey())) {
                components++;
                dfsHelper(entry.getKey(), visited);
                System.out.println();
            }
        }
        return components;
    }

    private Integer dfsTarjansScc(Node<T> node, Map<Node<T>, Integer> ids, Map<Node<T>, Integer> lowLink, Stack<Node<T>> stk, Set<Node<T>> onStack, Integer idCount) {
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
        List<Edge> edges = this.adjList.get(node);
        if(edges != null) {
            for (Edge edge : edges) {
                Node<T> neighbour = this.indexToNode.get(edge.getTo());
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
        }
        // 6. If current node has same id and low link value, then it is starting of a component
        // We need to remove the nodes in the component till we have the last node.
        if(ids.get(node).equals(lowLink.get(node))) {
            for(Node<T> top = stk.peek();;top = stk.peek()) {
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
