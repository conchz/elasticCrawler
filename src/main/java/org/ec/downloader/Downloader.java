package org.ec.downloader;

import org.ec.scheduler.Task;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-18.
 */
@FunctionalInterface
public interface Downloader {

    String download(Task task) throws IOException;

}
