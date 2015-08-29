package org.ec.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dolphineor on 2015-8-13.
 *
 * {@code Logs} lazy load
 */
public abstract class Logs {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

}
