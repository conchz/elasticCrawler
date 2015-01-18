package org.elasticcrawler.scheduler;

import org.elasticcrawler.core.Task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 任务队列
 */
public class TaskQueue {

    private final ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();

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
