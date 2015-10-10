package org.spider.extractor;

/**
 * <p>the extractor for html.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Extractor {

    void extract(Page page);

}
