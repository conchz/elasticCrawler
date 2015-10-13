package org.spider.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dolphineor
 */
public class HtmlExtractor implements Extractor<String> {

    private static Extractor extractor;


    @Override
    public String extract(String content) {
        Document doc = Jsoup.parse(content);

        return doc.outerHtml();
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
