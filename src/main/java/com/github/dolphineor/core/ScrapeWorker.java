package com.github.dolphineor.core;

import com.github.dolphineor.scheduler.Task;
import com.github.dolphineor.scheduler.TaskQueue;

import java.io.IOException;

/**
 * <p>ScrapeWorker
 *
 * @author dolphineor
 */
public class ScrapeWorker implements Runnable {

    private final TaskQueue taskQueue;


    public ScrapeWorker(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }


    @Override
    public void run() {
        for (; ; ) {
            Task task = taskQueue.take();
            if (task == null) {
                break;
            }
            execute(task);
        }
    }

    protected void execute(Task task) {
        try {
            task.getExtractor().extract(task.getDownloader().download(task));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
