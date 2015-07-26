package org.ec.scheduler;

/**
 * <p>this interface is used for saving task, {@link MemoryTaskQueue} is used by default.
 * Also, you can implement it by yourself if you need to use distributed task queue.
 * e.g. Redis
 *
 * @author dolphineor
 */
public interface TaskQueue {

    /**
     * take a task from task queue.
     *
     * @return task
     */
    Task take();

    /**
     * put a new task in task queue.
     *
     * @param task a new task
     */
    void offer(Task task);

}
