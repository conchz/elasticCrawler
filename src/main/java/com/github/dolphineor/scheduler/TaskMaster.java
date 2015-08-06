package com.github.dolphineor.scheduler;

import com.github.dolphineor.core.ScrapeWorker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 *
 * @author dolphineor
 */
public class TaskMaster implements Scheduler {

    private final StampedLock lock = new StampedLock();

    private final TaskQueue taskQueue = new MemoryTaskQueue();


    private int workerThreadNum = 2;

    private ExecutorService workerExecutor;


    private void addTask(List<Task> tasks) {
        long stamp = lock.writeLock();
        try {
            tasks.stream().forEach(taskQueue::offer);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    private void init() {
        workerExecutor = Executors.newFixedThreadPool(workerThreadNum);
    }


    public TaskMaster setWorkerThread(int workerThreadNum) {
        this.workerThreadNum = workerThreadNum;
        return this;
    }

    public static TaskMaster build() {
        return new TaskMaster();
    }

    private void sleep(long time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void schedule(List<Task> tasks) {
        addTask(tasks);
    }

    public void start() {
        init();

        for (int i = 0; i < workerThreadNum; i++) {
            workerExecutor.execute(new ScrapeWorker(taskQueue));
        }

    }

    public void shutdown() {
        workerExecutor.shutdown();
    }
}
