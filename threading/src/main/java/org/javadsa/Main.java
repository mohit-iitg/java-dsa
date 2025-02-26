package org.javadsa;

import org.javadsa.threading.ExtendThread;
import org.javadsa.threading.ImplementRunnable;

public class Main {
    public static void main(String[] args) {
        // Threading using extends Thread
        /*
        ExtendThread world = new ExtendThread();
        world.start();
        */

        // Threading using implements Runnable
        ImplementRunnable world = new ImplementRunnable();
        Thread t1 = new Thread(world);
        t1.start();

        for (; ;) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}