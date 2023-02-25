package org.directwebremoting.jms;

import java.io.Serializable;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link Session} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSession implements Session
{
    /**
     * @see jakarta.jms.Connection#createSession(boolean, int)
     * @param transacted See {@link Session#getTransacted()}
     * @param acknowledgeMode See {@link Session#getAcknowledgeMode()}
     * @param connection 
     */
    public DwrSession(DwrConnection connection, boolean transacted, int acknowledgeMode)
    {
        if (transacted)
        {
            throw Unsupported.noTransactions();
        }

        if (acknowledgeMode != AUTO_ACKNOWLEDGE)
        {
            throw Unsupported.noManualAcknowledgment();
        }

        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        this.connection = connection;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createMessage()
     */
    public DwrMessage createMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createMapMessage()
     */
    public DwrMessage createMapMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createTextMessage()
     */
    public DwrMessage createTextMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createTextMessage(java.lang.String)
     */
    public DwrMessage createTextMessage(String text) throws JMSException
    {
        return new DwrMessage(text);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createStreamMessage()
     */
    public StreamMessage createStreamMessage() throws JMSException
    {
        throw Unsupported.noBinaryMessages();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createBytesMessage()
     */
    public BytesMessage createBytesMessage() throws JMSException
    {
        throw Unsupported.noBinaryMessages();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createObjectMessage()
     */
    public DwrMessage createObjectMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createObjectMessage(java.io.Serializable)
     */
    public DwrMessage createObjectMessage(Serializable object) throws JMSException
    {
        return new DwrMessage(object);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue)
     */
    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue, java.lang.String)
     */
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination)
     */
    public DwrMessageConsumer createConsumer(Destination destination) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination, java.lang.String)
     */
    public DwrMessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination, messageSelector);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination, java.lang.String, boolean)
     */
    public DwrMessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination, messageSelector, noLocal);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String s) throws JMSException
    {
        return null;
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String s, String s1) throws JMSException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic, java.lang.String)
     */
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic, java.lang.String, java.lang.String, boolean)
     */
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String s) throws JMSException
    {
        return null;
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String s, String s1, boolean b) throws JMSException
    {
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String s) throws JMSException
    {
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String s, String s1) throws JMSException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createProducer(jakarta.jms.Destination)
     */
    public DwrMessageProducer createProducer(Destination destination) throws JMSException
    {
        return new DwrMessageProducer(destination, connection);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createQueue(java.lang.String)
     */
    public Queue createQueue(String queueName) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createTemporaryQueue()
     */
    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createTopic(java.lang.String)
     */
    public DwrTopic createTopic(String topicName) throws JMSException
    {
        return new DwrTopic(topicName);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#createTemporaryTopic()
     */
    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        throw Unsupported.noTemporaryTopic();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#getAcknowledgeMode()
     */
    public int getAcknowledgeMode() throws JMSException
    {
        return acknowledgeMode;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#setMessageListener(jakarta.jms.MessageListener)
     */
    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        this.messageListener = messageListener;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#getMessageListener()
     */
    public MessageListener getMessageListener() throws JMSException
    {
        return messageListener;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#getTransacted()
     */
    public boolean getTransacted() throws JMSException
    {
        return transacted;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#commit()
     */
    public void commit() throws JMSException
    {
        if (!shownTransactionWarning)
        {
            log.warn("DWR's JMS support is not transactional");
            shownTransactionWarning = true;
        }
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#recover()
     */
    public void recover() throws JMSException
    {
        throw Unsupported.noManualAcknowledgment();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#rollback()
     */
    public void rollback() throws JMSException
    {
        if (!shownTransactionWarning)
        {
            log.warn("DWR's JMS support is not transactional");
            shownTransactionWarning = true;
        }
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#run()
     */
    public void run()
    {
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#close()
     */
    public void close() throws JMSException
    {
    }

    /* (non-Javadoc)
     * @see jakarta.jms.Session#unsubscribe(java.lang.String)
     */
    public void unsubscribe(String name) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    /**
     * @see Unsupported#noManualAcknowledgment()
     */
    private int acknowledgeMode;

    /**
     * @see Unsupported#noTransactions()
     */
    private boolean transacted;

    /**
     * The current message destination
     */
    private MessageListener messageListener;

    /**
     * The connection through which this session was created
     */
    private DwrConnection connection;

    /**
     * We only want to show the not-transactional warning once.
     */
    private boolean shownTransactionWarning = false;

    /**
     * The log stream
     */
    private static final Logger log = LoggerFactory.getLogger(DwrSession.class);
}
