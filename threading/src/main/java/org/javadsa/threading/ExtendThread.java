package org.javadsa.threading;

public class ExtendThread extends Thread {
    @Override
    public void run() {
        for(; ;) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
