package com.github.dolphineor.scheduler;

import java.util.List;

/**
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Scheduler {

    void schedule(List<Task> tasks);

}
