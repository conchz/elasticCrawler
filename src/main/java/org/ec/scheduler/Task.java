package org.ec.scheduler;

import org.ec.downloader.Downloader;
import org.ec.extractor.Extractor;
import org.ec.handler.Handler;

/**
 * the detailed task object.
 *
 * @author dolphineor
 */
public class Task {

    private String url;

    private String charset;

    private Downloader downloader;

    private Extractor extractor;

    private Handler handler;


    public String getUrl() {
        return url;
    }

    public Task setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Task setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
