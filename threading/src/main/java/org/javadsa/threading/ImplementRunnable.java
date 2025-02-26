package org.javadsa.threading;

public class ImplementRunnable implements Runnable{
    @Override
    public void run() {
        for(; ;) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
