package org.elasticcrawler.core;

/**
 * Created by dolphineor on 2015-1-17.
 */
@FunctionalInterface
public interface Worker {

    void execute(Task task);
}
