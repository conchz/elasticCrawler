package org.elasticcrawler.scheduler;

import org.elasticcrawler.core.Task;

import java.util.List;

/**
 * Created by dolphineor on 2015-1-18.
 */
public interface Scheduler {

    void schedule(List<Task> tasks);

    void start();

    void shutdown();

}
