package com.github.dolphineor.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.StampedLock;

/**
 * {@code MemoryTaskQueue} maintains a task queue in memory.
 *
 * @author dolphineor
 */
public class MemoryTaskQueue implements TaskQueue {

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    private final StampedLock lock = new StampedLock();


    public Task take() {
        return queue.poll();
    }

    public void offer(Task task) {
        long stamp = lock.writeLock();
        try {
            boolean isContain = queue.parallelStream().anyMatch(t -> t.getUrl().equals(task.getUrl()) && t.getCharset().equals(task.getCharset()));
            if (!isContain) {
                queue.offer(task);
            }
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
