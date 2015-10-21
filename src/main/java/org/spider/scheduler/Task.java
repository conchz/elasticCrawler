package org.spider.scheduler;

import org.spider.downloader.Downloader;
import org.spider.extractor.Extractor;

import java.io.Serializable;

/**
 * the detailed task object.
 *
 * @author dolphineor
 */
public class Task implements Serializable {

    private String url;

    private String charset;

    /**
     * 爬取深度
     */
    private int layer;

    /**
     * -1: 爬取失败
     * 0: 未进行爬取
     * 1: 已成功爬取
     */
    private int status;

    private Downloader downloader;

    private Extractor extractor;


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

    public int getLayer() {
        return layer;
    }

    public Task setLayer(int layer) {
        this.layer = layer;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Task setStatus(int status) {
        this.status = status;
        return this;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public Task setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public Task setExtractor(Extractor extractor) {
        this.extractor = extractor;
        return this;
    }

}
