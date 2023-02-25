package org.directwebremoting.jms;

import jakarta.jms.Destination;
import jakarta.jms.IllegalStateException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ServerContext;
import org.directwebremoting.event.MessageEvent;

/**
 * A {@link MessageConsumer} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrMessageConsumer implements MessageConsumer
{
    /**
     * @see jakarta.jms.Session#createConsumer(Destination)
     */
    public DwrMessageConsumer(DwrConnection connection, Destination destination) throws JMSException
    {
        this(connection, destination, null, false);
    }

    /**
     * @throws JMSException
     * @see jakarta.jms.Session#createConsumer(Destination, String)
     */
    public DwrMessageConsumer(DwrConnection connection, Destination destination, String messageSelector) throws JMSException
    {
        this(connection, destination, messageSelector, false);
    }

    /**
     * @see jakarta.jms.Session#createConsumer(Destination, String, boolean)
     */
    public DwrMessageConsumer(DwrConnection connection, Destination destination, String messageSelector, boolean noLocal) throws JMSException
    {
        this.destination = destination;
        setMessageSelector(messageSelector);
        this.noLocal = noLocal;

        ServerContext serverContext = connection.getServerContext();

        if (serverContext != null)
        {
            hub = HubFactory.get(serverContext);
        }
        else
        {
            hub = HubFactory.get();
        }

        if (destination instanceof DwrTopic)
        {
            DwrTopic topic = (DwrTopic) destination;
            String topicName = topic.getTopicName();

            hub.subscribe(topicName, new MessageListenerMessageListener());
        }
        else
        {
            throw new IllegalStateException("Unsuported Destination type (" + destination.getClass().getCanonicalName() + "). Only Topics are currently supported.");
        }
    }

    /**
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    public final class MessageListenerMessageListener implements org.directwebremoting.event.MessageListener
    {
        public void onMessage(MessageEvent message)
        {
            if (messageListener != null)
            {
                messageListener.onMessage(new DwrMessage(message.getHub(), message));
            }
        }
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#close()
     */
    public void close() throws JMSException
    {
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#getMessageListener()
     */
    public MessageListener getMessageListener() throws JMSException
    {
        return messageListener;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#setMessageListener(jakarta.jms.MessageListener)
     */
    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        this.messageListener = messageListener;
    }

    /**
     * @param messageSelector the messageSelector to set
     */
    public void setMessageSelector(String messageSelector)
    {
        this.messageSelector = messageSelector;

        if (messageSelector != null && messageSelector.length() != 0)
        {
            throw Unsupported.noMessageSelectors();
        }
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#getMessageSelector()
     */
    public String getMessageSelector() throws JMSException
    {
        return messageSelector;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#receive()
     */
    public Message receive() throws JMSException
    {
        MessageListener old = getMessageListener();

        BlockingMessageListener listener = new BlockingMessageListener();

        setMessageListener(listener);
        Message message = listener.receive(0);
        setMessageListener(old);

        return message;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#receive(long)
     */
    public Message receive(long timeout) throws JMSException
    {
        MessageListener old = getMessageListener();

        BlockingMessageListener listener = new BlockingMessageListener();

        setMessageListener(listener);
        Message message = listener.receive(timeout);
        setMessageListener(old);

        return message;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageConsumer#receiveNoWait()
     */
    public Message receiveNoWait() throws JMSException
    {
        log.warn("MessageConsumer.receiveNoWait() probably doesn't do what you need in a queue context");

        MessageListener old = getMessageListener();

        BlockingMessageListener listener = new BlockingMessageListener();

        setMessageListener(listener);
        Message message = listener.receive(1);
        setMessageListener(old);

        return message;
    }

    /**
     * The Hub for this instance of DWR
     */
    protected Hub hub;

    /**
     * A way to filter messages based on message properties
     */
    protected String messageSelector;

    /**
     * The queue or topic that this message is destined for
     */
    protected Destination destination;

    /**
     * Do we get messages that are sent locally?
     */
    protected boolean noLocal;

    /**
     * What we do with inbound messages
     */
    protected MessageListener messageListener;

    /**
     * The log stream
     */
    private static final Logger log = LoggerFactory.getLogger(DwrMessageConsumer.class);
}
