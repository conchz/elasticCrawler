package org.spider.core;

import org.spider.extractor.Page;
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
            Page page = new Page(task.getUrl(), task.getDownloader().download(task), task.getCharset());
            task.getExtractor().extract(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
