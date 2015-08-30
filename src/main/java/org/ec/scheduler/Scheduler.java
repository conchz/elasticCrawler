package org.ec.scheduler;

import java.util.List;

/**
 * Created on 2015-01-18.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Scheduler {

    void schedule(List<Task> tasks);

}
