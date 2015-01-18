package org.elasticcrawler.extractor;

/**
 * Created by baizz on 2015-1-18.
 */
public class HtmlExtractor implements Handler {
    @Override
    public void handle(Page page) {
        System.out.println(page.getHtml());
    }
}
