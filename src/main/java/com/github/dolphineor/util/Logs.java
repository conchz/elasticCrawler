package com.github.dolphineor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2015-08-13.
 * <p>
 * {@code Logs} lazy load
 */
public abstract class Logs {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

}
