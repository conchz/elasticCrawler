package org.ec;

import org.ec.scheduler.Task;
import org.ec.core.Worker;

/**
 * Created by dolphineor on 2015-1-17.
 */
public interface WorkerListener {

    void beforeExecute(Worker worker, Task task);

    void afterExecute(Worker worker, Task task);
}
