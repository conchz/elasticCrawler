package com.github.dolphineor.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dolphineor
 */
public class HtmlExtractor implements Extractor {

    private static Extractor extractor;


    @Override
    public void extract(String html) {
        Document doc = Jsoup.parse(html);
//        System.out.println(html);
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
