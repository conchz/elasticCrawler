package org.ec.extractor;

/**
 * <p>the extractor for html.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Extractor {

    void extract(Page page);

}
