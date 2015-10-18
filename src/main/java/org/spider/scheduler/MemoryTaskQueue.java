package org.spider.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * {@code MemoryTaskQueue} maintains a task queue in memory.
 *
 * @author dolphineor
 */
public class MemoryTaskQueue implements TaskQueue {

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    private final Function<Task, Boolean> taskPutFunction = task -> {
        boolean isExists = queue.parallelStream()
                .anyMatch(t -> t.getUrl().equals(task.getUrl()) && t.getCharset().equals(task.getCharset()));
        if (!isExists) {
            queue.offer(task);
        }

        return !isExists;
    };


    public Task take() {
        return queue.poll();
    }

    public void offer(Task task) {
        taskPutFunction.apply(task);
    }
}
