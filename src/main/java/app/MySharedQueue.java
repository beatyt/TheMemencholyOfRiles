package main.java.app;

import com.google.inject.Singleton;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Created by user on 2016-02-22.
 */
/*
http://www.javacodegeeks.com/2012/02/concurrency-pattern-producer-and.html
 */
@Singleton
public class MySharedQueue {
    public ArrayBlockingQueue queue = new ArrayBlockingQueue(100);
    public Boolean continueProducing = Boolean.TRUE;

    public void put(Object data) throws InterruptedException {
        this.queue.put(data);
    }

    public Object get() throws InterruptedException {
        return this.queue.poll(1, TimeUnit.SECONDS);
    }
}
