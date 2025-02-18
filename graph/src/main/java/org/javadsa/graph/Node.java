package org.javadsa.graph;

public class Node<T> {
    private T value;
    private int wt;
    private int index;

    public Node(T val, int wt, int index) {
        this.value = val;
        this.wt = wt;
        this.index = index;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getWt() {
        return wt;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "{Val: "+this.value+", Wt: "+this.wt+", Idx: "+this.index+"}";
    }
}