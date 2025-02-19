package org.javadsa.util.disjointset;

public class DisjointSet {
    private int[] parent;
    private int setSize;

    public DisjointSet(int setSize) {
        this.setSize = setSize;
        this.parent = new int[this.setSize];
        for(int i=0;i<this.setSize;i++) {
            this.parent[i] = -1;
        }
    }

    public int find(int idx) {
        int initIdx = idx;
        while(this.parent[idx] != -1) {
//            System.out.println("Curr idx: "+idx+" parent arr: "+this.parent);
            idx = this.parent[idx];
        }
        // to wrap around for better time complexity
        // each node should be directly mapped to the root
        if(idx != initIdx) {
            this.parent[initIdx] = idx;
        }
        return idx;
    }

    public void union(int idx1, int idx2) {
        int find1 = this.find(idx1);
        int find2 = this.find(idx2);
        if(find1 != find2) {
            this.parent[find1] = find2;
        }
    }

}
