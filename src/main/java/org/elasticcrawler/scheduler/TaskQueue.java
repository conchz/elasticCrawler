package org.elasticcrawler.scheduler;

import org.elasticcrawler.core.Site;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by baizz on 2015-1-17.
 */
public class TaskQueue {

    private final ConcurrentLinkedQueue<Site> queue = new ConcurrentLinkedQueue<>();

    private final ReentrantLock lock = new ReentrantLock();


    public Site take() {
        lock.lock();
        Site site;
        try {
            site = queue.poll();
        } finally {
            lock.unlock();
        }
        return site;
    }

}
