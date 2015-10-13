package org.spider.extractor;

import java.io.Serializable;

/**
 * <p>the extractor for html.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Extractor<T> extends Serializable {

    T extract(String content);

}
