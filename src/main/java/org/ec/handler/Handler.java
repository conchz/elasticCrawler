package org.ec.handler;

/**
 * this interface is used for data persistence.
 *
 * @author dolphineor
 */
@FunctionalInterface
public interface Handler<R> {

    void handle(R r);

}
