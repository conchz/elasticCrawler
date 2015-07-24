package org.ec.extractor;

/**
 * Created by dolphineor on 2015-1-19.
 */
@FunctionalInterface
public interface Extractor {

    public static final String DEFAULT_EXTRACTOR = "DEFAULT_EXTRACTOR";


    void extract(Page page);


    default String getName() {
        return null;
    }
}
