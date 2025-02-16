package org.javadsa.graph;

import org.javadsa.graph.adjlist.GraphAdjList;
import org.javadsa.graph.adjlist.Node;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        System.out.println( "Hello World!" );
        GraphAdjList<Node<Integer>> graph = new GraphAdjList<>();
        Node<Integer> node0 = graph.addNode(new Node<>(0));
        Node<Integer> node1 = graph.addNode(new Node<>(1));
        Node<Integer> node2 = graph.addNode(new Node<>(2));
        Node<Integer> node3 = graph.addNode(new Node<>(3));
        Node<Integer> node4 = graph.addNode(new Node<>(4));
        Node<Integer> node5 = graph.addNode(new Node<>(5));
        graph.addEdge(node0, node1);
        graph.addEdge(node2, node1);
        graph.addEdge(node5, node3);
        graph.addEdge(node3, node0);
        graph.addEdge(node5, node1);
        graph.addEdge(node4, node0);
        graph.addEdge(node0, node4);
        graph.addEdge(node3, node2);
        graph.printGraph();

        graph.dfs();
    }
}
