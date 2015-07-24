package org.ec.extractor;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class Page {

    private String html;


    public Page(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
