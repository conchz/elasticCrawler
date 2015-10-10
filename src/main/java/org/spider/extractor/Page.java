package org.spider.extractor;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
public class Page {

    /**
     * 抓取的URL
     */
    private final String url;

    /**
     * 抓取的网页文本内容
     */
    private final String content;

    /**
     * 抓取的网页字符集
     */
    private final String charset;


    public Page(String url, String content, String charset) {
        this.url = url;
        this.content = content;
        this.charset = charset;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getCharset() {
        return charset;
    }
}
