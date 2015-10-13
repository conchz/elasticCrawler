package org.spider.downloader;

import org.spider.scheduler.Task;

import java.io.IOException;
import java.io.Serializable;

/**
 * <p>the basic download interface, we implement {@link HttpClientDownloader}
 * and {@link SeleniumDownloader}. You can implement it by yourself if you need.</p>
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Downloader extends Serializable {

    String download(Task task) throws IOException;

}
