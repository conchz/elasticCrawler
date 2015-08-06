package com.github.dolphineor.extractor;

/**
 * the extractor for html.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Extractor {

    void extract(String html);

}
