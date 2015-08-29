package org.ec.extractor;

/**
 * the extractor for html.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Extractor {

    void extract(String content);

}
