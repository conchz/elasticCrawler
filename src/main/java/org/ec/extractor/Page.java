package org.ec.extractor;

/**
 * Created on 2015-08-29.
 *
 * @author dolphineor
 */
public class Page {

    private String url;

    private String content;

    private String charset;


    public Page(String url, String content, String charset) {
        this.url = url;
        this.content = content;
        this.charset = charset;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
