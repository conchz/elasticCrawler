package org.elasticcrawler.extractor;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class HtmlExtractor implements Extractor {

    @Override
    public void extract(Page page) {
        System.out.println(page.getHtml());
    }

    @Override
    public String getName() {
        return DEFAULT_EXTRACTOR;
    }
}
