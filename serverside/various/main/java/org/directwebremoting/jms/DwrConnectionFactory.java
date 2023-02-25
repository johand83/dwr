package org.directwebremoting.jms;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link ConnectionFactory} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConnectionFactory implements ConnectionFactory
{
	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionFactory#createConnection()
	 */
	public DwrConnection createConnection() throws JMSException
	{
		return new DwrConnection();
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionFactory#createConnection(java.lang.String, java.lang.String)
	 */
	public DwrConnection createConnection(String username, String password) throws JMSException
	{
	    log.debug("DwrConnectionFactory.createConnection(username, password) is provided for convenience only. No passwords are used by DWR");
		return new DwrConnection();
	}

	@Override
	public JMSContext createContext()
	{
		return null;
	}

	@Override
	public JMSContext createContext(String s, String s1)
	{
		return null;
	}

	@Override
	public JMSContext createContext(String s, String s1, int i)
	{
		return null;
	}

	@Override
	public JMSContext createContext(int i)
	{
		return null;
	}

	/**
     * The log stream
     */
    private static final Logger log = LoggerFactory.getLogger(DwrConnectionFactory.class);
}
