package org.directwebremoting.hibernate;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Setup an in-process HSQLDB instance
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DatabaseInitServletContextListener implements ServletContextListener
{
    /* (non-Javadoc)
     * @see jakarta.servlet.ServletContextListener#contextInitialized(jakarta.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent ev)
    {
    }

    /* (non-Javadoc)
     * @see jakarta.servlet.ServletContextListener#contextDestroyed(jakarta.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent ev)
    {
    }
}
