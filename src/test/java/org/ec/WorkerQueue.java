package org.ec;

import org.ec.core.Worker;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by dolphineor on 2015-1-19.
 */
public class WorkerQueue {

    private final ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();

    private final StampedLock lock = new StampedLock();

    private AtomicInteger workerNum = new AtomicInteger(0);


    public void addWorker(Worker worker) {
        long stamp = lock.writeLock();
        try {
            workers.offer(worker);
            workerNum.incrementAndGet();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public int getWorkerNum() {
        long stamp = lock.readLock();
        int num = 0;
        try {
            num = workerNum.get();
        } finally {
            lock.unlockRead(stamp);
        }
        return num;
    }

    public ConcurrentLinkedQueue<Worker> getWorkers() {
        return workers;
    }
}
