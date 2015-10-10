package org.spider.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dolphineor
 */
public class HtmlExtractor implements Extractor {

    private static Extractor extractor;


    @Override
    public void extract(Page page) {
        Document doc = Jsoup.parse(page.getContent());
    }

    public static Extractor create() {
        if (extractor == null) {
            synchronized (HtmlExtractor.class) {
                if (extractor == null)
                    extractor = new HtmlExtractor();
            }
        }
        return extractor;
    }
}
