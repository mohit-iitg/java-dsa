package org.javadsa.graph;

import org.javadsa.graph.adjlist.GraphAdjList;
import org.javadsa.graph.adjmat.GraphAdjMat;

public class App 
{
    public static void main( String[] args ) {

        System.out.println( "Hello World!" );
        int nodesCount = 6;
        Node<Integer>[] nodes = new Node[nodesCount];
        Node<Integer> node0 = new Node<>(2, 1, 0);
        Node<Integer> node1 = new Node<>(3, 1, 1);
        Node<Integer> node2 = new Node<>(5, 1, 2);
        Node<Integer> node3 = new Node<>(6, 1, 3);
        Node<Integer> node4 = new Node<>(4, 1, 4);
        Node<Integer> node5 = new Node<>(7, 1, 5);
        nodes[0] = node0; nodes[1] = node1; nodes[2] = node2; nodes[3] = node3; nodes[4] = node4; nodes[5] = node5;

        Edge[] edges = new Edge[8];
        Edge edge0 = new Edge(0, 1, 10.0);
        Edge edge1 = new Edge(1, 4, 1.0);
        Edge edge2 = new Edge(4, 3, 2.0);
        Edge edge3 = new Edge(2, 5, 5.0);
        Edge edge4 = new Edge(2, 0, 10.0);
        Edge edge5 = new Edge(3, 0, 10.0);
        Edge edge6 = new Edge(0, 5, 8.0);
        Edge edge7 = new Edge(3, 1, 10.0);
        edges[0] = edge0; edges[1] = edge1; edges[2] = edge2; edges[3] = edge3;
        edges[4] = edge4; edges[5] = edge5; edges[6] = edge6; edges[7] = edge7;

        System.out.println("Adjacency list graph: ");
        GraphIntf<Integer> graphList = new GraphAdjList<>(nodesCount, nodes, edges, false);
        System.out.println("DFS: ");
        graphList.dfs();
        System.out.println("BFS: ");
        graphList.bfs();

        System.out.println("The graph contains cycle: "+graphList.containsCycle());

        System.out.println("Prim's MST has value: "+graphList.primMst());
        System.out.println("Kruskals's MST has value: "+graphList.kruskalMst());
        Double[] costsList = graphList.bellmanFord(node0);
        System.out.println("Shortest path from node 0: ");
        for(Double cost : costsList) {
            System.out.printf("%.1f, ", cost);
        }
        System.out.println();
        System.out.println("Contains negative wt cycle "+graphList.containsNegativeCycle());


        System.out.println("Adjacency matrix graph: ");
        GraphIntf<Integer> graphMat = new GraphAdjMat<>(nodesCount, nodes, edges, false);
        System.out.println("DFS: ");
        graphMat.dfs();
        System.out.println("BFS: ");
        graphMat.bfs();

        System.out.println("The graph contains cycle: "+graphMat.containsCycle());

        System.out.println("Prim's MST has value: "+graphMat.primMst());
        System.out.println("Kruskals's MST has value: "+graphMat.kruskalMst());
        Double[] costsMat = graphList.bellmanFord(node0);
        System.out.println("Shortest path from node 0: ");
        for(Double cost : costsMat) {
            System.out.printf("%.1f, ", cost);
        }
        System.out.println();
        System.out.println("Contains negative wt cycle "+graphMat.containsNegativeCycle());
    }
}
