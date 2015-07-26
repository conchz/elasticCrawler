package org.ec.scheduler;

import java.util.List;

/**
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Scheduler {

    void schedule(List<Task> tasks);

}
