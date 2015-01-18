package org.elasticcrawler.core;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class Task {

    private String url;

    private String charset;

    private String userAgent;

    private int retryTimes = 0;

    private boolean useGzip = true;


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

    public String getUserAgent() {
        return userAgent;
    }

    public Task setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Task setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public Task setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }
}
