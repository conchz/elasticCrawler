package org.ec.handler;

/**
 * Created by dolphineor on 2015-1-18.
 */
@FunctionalInterface
public interface Handler {

    String DEFAULT_HANDLER = "DEFAULT_HANDLER";


    void handle(Result result);


    default String getName() {
        return null;
    }
}
