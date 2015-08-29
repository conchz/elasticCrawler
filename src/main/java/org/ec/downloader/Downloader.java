package org.ec.downloader;

import org.ec.scheduler.Task;

import java.io.IOException;

/**
 * the basic download interface, we implement {@link HttpClientDownloader}
 * and {@link SeleniumDownloader}.
 * you can implement it by yourself if you need.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Downloader {

    String download(Task task) throws IOException;

}
