package org.directwebremoting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of standard log destinations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Loggers
{
    /**
     * Log destination for method access and exceptions caught outside of DWR's code base.
     */
    public static final Logger ACCESS = LoggerFactory.getLogger("org.directwebremoting.log.accessLog");

    /**
     * Log destination for startup messages
     */
    public static final Logger STARTUP = LoggerFactory.getLogger("org.directwebremoting.log.startup");

    /**
     * Log destination for session change messages
     */
    public static final Logger SESSION = LoggerFactory.getLogger("org.directwebremoting.log.session");

    /**
     * Log destination for script debug messages
     */
    public static final Logger SCRIPTS = LoggerFactory.getLogger("org.directwebremoting.log.scripts");
}
