package org.ec.scheduler;

import org.ec.core.Worker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class TaskMaster implements Scheduler {

    private final StampedLock lock = new StampedLock();

    private TaskQueue taskQueue = new TaskQueue();

//    private WorkerQueue workerQueue = new WorkerQueue();

    private int workerThreadNum = 1;

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

    private void sleep(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void schedule(List<Task> tasks) {
        addTask(tasks);
    }

    @Override
    public void start() {
        init();

        for (int i = 0; i < workerThreadNum; i++) {
            workerExecutor.execute(new Worker(taskQueue));
        }

    }

    @Override
    public void shutdown() {
        workerExecutor.shutdown();
    }
}
