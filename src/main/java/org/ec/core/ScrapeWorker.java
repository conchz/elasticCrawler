package org.ec.core;

import org.ec.extractor.Page;
import org.ec.scheduler.Task;
import org.ec.util.Logs;
import rx.functions.Action1;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2015-07-26.
 *
 * @author dolphineor
 */
public class ScrapeWorker extends Logs implements Action1<Task> {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void call(Task task) {
        try {
            logger.info(atomicInteger.incrementAndGet() + "");
            Page page = new Page(task.getUrl(), task.getDownloader().download(task), task.getCharset());
            task.getExtractor().extract(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
