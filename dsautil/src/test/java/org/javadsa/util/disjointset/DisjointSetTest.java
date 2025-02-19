package org.javadsa.util.disjointset;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisjointSetTest {
    private DisjointSet dj;
    @Before
    public void setUp() {
        dj = new DisjointSet(6);
        dj.union(0,5);
        dj.union(1,5);
        dj.union(2,3);
    }

    @Test
    public void testDisjointSet_find() {
        assertEquals(5, dj.find(1));
    }

    @Test
    public void testDisjointSet_union() {
        dj.union(1,2);
        assertEquals(dj.find(5), dj.find(2));
    }
}
