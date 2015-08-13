package com.github.dolphineor.core;

import com.github.dolphineor.scheduler.Task;
import com.github.dolphineor.util.Logs;
import rx.functions.Action1;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-8-13.
 */
public class ScrapeWorker$ extends Logs implements Action1<Task> {

    @Override
    public void call(Task task) {
        try {
            task.getExtractor().extract(task.getDownloader().download(task));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
