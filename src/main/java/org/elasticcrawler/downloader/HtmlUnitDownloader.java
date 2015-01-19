package org.elasticcrawler.downloader;

import org.elasticcrawler.core.Task;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 适用于下载经javascript渲染后的页面
 */
public class HtmlUnitDownloader implements Downloader {

    private static Downloader downloader = null;

    @Override
    public String download(Task task) throws IOException {
        return null;
    }

    @Override
    public String getName() {
        return HTML_UNIT_DOWNLOADER;
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (HtmlUnitDownloader.class) {
                if (downloader == null)
                    downloader = new HtmlUnitDownloader();
            }
        }
        return downloader;
    }
}
