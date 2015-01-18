package org.elasticcrawler.downloader;

import org.elasticcrawler.core.Task;

import java.io.IOException;

/**
 * Created by baizz on 2015-1-18.
 */
@FunctionalInterface
public interface Downloader {

    String download(Task task) throws IOException;
}
