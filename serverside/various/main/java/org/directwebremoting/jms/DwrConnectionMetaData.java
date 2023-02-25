package org.directwebremoting.jms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import jakarta.jms.ConnectionMetaData;
import jakarta.jms.JMSException;

import org.directwebremoting.util.VersionUtil;

/**
 * An implementation of {@link ConnectionMetaData} that uses DWR meta-data.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConnectionMetaData implements ConnectionMetaData
{
	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getJMSMajorVersion()
	 */
	public int getJMSMajorVersion() throws JMSException
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getJMSMinorVersion()
	 */
	public int getJMSMinorVersion() throws JMSException
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getJMSProviderName()
	 */
	public String getJMSProviderName() throws JMSException
	{
		return "DWR";
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getJMSVersion()
	 */
	public String getJMSVersion() throws JMSException
	{
		return "1.1";
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getJMSXPropertyNames()
	 */
	public Enumeration<String> getJMSXPropertyNames() throws JMSException
	{
		return Collections.enumeration(properties);
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getProviderMajorVersion()
	 */
	public int getProviderMajorVersion() throws JMSException
	{
        String version = getProviderVersion();
        String[] strings = version.split("\\.");
        return Integer.parseInt(strings[0]);
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getProviderMinorVersion()
	 */
	public int getProviderMinorVersion() throws JMSException
	{
        String version = getProviderVersion();
        String[] strings = version.split("\\.");
        return Integer.parseInt(strings[1]);
	}

	/* (non-Javadoc)
	 * @see jakarta.jms.ConnectionMetaData#getProviderVersion()
	 */
	public String getProviderVersion() throws JMSException
	{
		return VersionUtil.getLabel();
	}

	private Collection<String> properties = new ArrayList<String>();
}
