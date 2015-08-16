package com.github.dolphineor.core;

import com.github.dolphineor.scheduler.Task;
import com.github.dolphineor.util.Logs;
import rx.functions.Action1;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author dolphineor
 */
public class ScrapeWorker extends Logs implements Action1<Task> {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void call(Task task) {
        try {
            logger.info(atomicInteger.incrementAndGet() + "");
            task.getExtractor().extract(task.getDownloader().download(task));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
