package org.ec.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class HtmlExtractor implements Extractor {

    private static Extractor extractor;


    @Override
    public void extract(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        System.out.println(page.getHtml());
    }

    public static Extractor create() {
        // DCL
        if (extractor == null) {
            synchronized (HtmlExtractor.class) {
                if (extractor == null)
                    extractor = new HtmlExtractor();
            }
        }
        return extractor;
    }
}
