package org.elasticcrawler;

import org.elasticcrawler.scheduler.Task;
import org.elasticcrawler.core.Worker;

/**
 * Created by dolphineor on 2015-1-17.
 */
public interface WorkerListener {

    void beforeExecute(Worker worker, Task task);

    void afterExecute(Worker worker, Task task);
}
