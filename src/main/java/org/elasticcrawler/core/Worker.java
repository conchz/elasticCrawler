package org.elasticcrawler.core;

import org.elasticcrawler.extractor.Page;
import org.elasticcrawler.scheduler.TaskQueue;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class Worker implements Runnable {

    private final TaskQueue taskQueue;


    public Worker(TaskQueue taskQueue) {
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
            Page page = new Page(task.getDownloader().download(task));
            task.getExtractor().extract(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
