package org.spider.core;

import org.spider.scheduler.Task;
import org.spider.util.Logs;
import rx.functions.Action1;

import java.io.IOException;

/**
 * Created on 2015-07-26.
 *
 * @author dolphineor
 */
public class ScrapeWorker extends Logs implements Action1<Task> {

    @Override
    public void call(Task task) {
        try {
            String content = task.getDownloader().download(task);
            task.getExtractor().extract(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
