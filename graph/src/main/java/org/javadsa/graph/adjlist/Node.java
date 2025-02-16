package org.javadsa.graph.adjlist;

public class Node<T> implements GraphNode<T>{
    private T val;

    public Node(T value) {
        this.val = value;
    }

    @Override
    public void setValue(T value) {
        this.val = value;
    }

    @Override
    public T getValue() {
        return val;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Node)) {
            return false;
        }
        Node<T> node = (Node<T>)obj;
        return node.getValue().equals(this.getValue());
    }

    @Override
    public String toString() {
        return '('+val.toString()+')';
    }

}
