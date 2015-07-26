package org.ec;

import org.ec.core.ScrapeWorker;
import org.ec.scheduler.Task;

/**
 * Created by dolphineor on 2015-1-17.
 */
public interface WorkerListener {

    void beforeExecute(ScrapeWorker worker, Task task);

    void afterExecute(ScrapeWorker worker, Task task);
}
