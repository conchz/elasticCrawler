package org.spider.downloader;

import org.spider.scheduler.Task;

import java.io.IOException;
import java.io.Serializable;

/**
 * the basic download interface, we implement {@link HttpClientDownloader}
 * and {@link SeleniumDownloader}.
 * you can implement it by yourself if you need.
 *
 * @author dolphineor
 */
public interface Downloader extends Serializable {

    String download(Task task) throws IOException;

}
