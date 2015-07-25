package org.ec.scheduler;

import java.util.List;

/**
 * Created by dolphineor on 2015-1-18.
 */
@FunctionalInterface
public interface Scheduler {

    void schedule(List<Task> tasks);

}
