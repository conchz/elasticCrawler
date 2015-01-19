package org.elasticcrawler.downloader;

import org.elasticcrawler.core.Task;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 基于淘宝的定制型下载器(淘宝采用的是KISSY javascript框架, htmlUnit无法识别到)
 */
public class KissyDownloader implements Downloader {

    private static Downloader downloader = null;

    @Override
    public String download(Task task) throws IOException {
        return null;
    }

    @Override
    public String getName() {
        return KISSY_DOWNLOADER;
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (KissyDownloader.class) {
                if (downloader == null)
                    downloader = new KissyDownloader();
            }
        }
        return downloader;
    }

    /**
     * based on phantomjs
     */
    static final class JavascriptHelper {
    }
}
