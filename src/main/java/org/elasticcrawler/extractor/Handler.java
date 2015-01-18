package org.elasticcrawler.extractor;

import org.elasticcrawler.extractor.Page;

/**
 * Created by baizz on 2015-1-18.
 */
@FunctionalInterface
public interface Handler {

    void handle(Page page);
}
