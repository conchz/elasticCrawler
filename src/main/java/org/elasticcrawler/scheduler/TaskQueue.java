package org.elasticcrawler.scheduler;

import org.elasticcrawler.core.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 任务队列
 */
public class TaskQueue {

    private final ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();


    public Task take() {
        return queue.poll();
    }

    public void offer(Task task) {
        queue.offer(task);
    }

}
