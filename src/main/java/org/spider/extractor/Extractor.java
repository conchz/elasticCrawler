package org.spider.extractor;

import java.io.Serializable;

/**
 * <p>the extractor for html.
 *
 * @author dolphineor
 */
public interface Extractor extends Serializable {

    void extract(Page page);

}
