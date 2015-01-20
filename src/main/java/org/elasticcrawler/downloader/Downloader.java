package org.elasticcrawler.downloader;

import org.elasticcrawler.core.Task;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-18.
 */
@FunctionalInterface
public interface Downloader {

    public static final String DEFAULT_DOWNLOADER = "HTTP_CLIENT_DOWNLOADER";

    public static final String AJAX_DOWNLOADER = "AJAX_DOWNLOADER";


    String download(Task task) throws IOException;

    default String getName() {
        return null;
    }

}
