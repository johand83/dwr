package org.directwebremoting.jms;

import java.util.Date;
import java.util.UUID;

import jakarta.jms.CompletionListener;
import jakarta.jms.Destination;
import jakarta.jms.IllegalStateException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;

import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ServerContext;

/**
 * An implementation of {@link MessageProducer} that sends messages out over
 * Reverse Ajax to a client side hub
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrMessageProducer implements MessageProducer
{
    /**
     * @param destination
     */
    public DwrMessageProducer(Destination destination, DwrConnection connection)
    {
        this.destination = destination;
        ServerContext serverContext = connection.getServerContext();

        if (serverContext != null)
        {
            hub = HubFactory.get(serverContext);
        }
        else
        {
            hub = HubFactory.get();
        }
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#close()
     */
    public void close() throws JMSException
    {
        state = State.CLOSED;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getDeliveryMode()
     */
    public int getDeliveryMode() throws JMSException
    {
        return deliveryMode;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getDestination()
     */
    public Destination getDestination() throws JMSException
    {
        return destination;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getDisableMessageID()
     */
    public boolean getDisableMessageID() throws JMSException
    {
        return disableMessageID;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getDisableMessageTimestamp()
     */
    public boolean getDisableMessageTimestamp() throws JMSException
    {
        return disableMessageTimestamp;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getPriority()
     */
    public int getPriority() throws JMSException
    {
        return priority;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#getTimeToLive()
     */
    public long getTimeToLive() throws JMSException
    {
        return timeToLive;
    }

    @Override
    public void setDeliveryDelay(long l) throws JMSException
    {

    }

    @Override
    public long getDeliveryDelay() throws JMSException
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message)
     */
    public void send(Message message) throws JMSException
    {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination, jakarta.jms.Message)
     */
    public void send(Destination realDestination, Message message) throws JMSException
    {
        send(realDestination, message, deliveryMode, priority, timeToLive);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message, int, int, long)
     */
    public void send(Message message, int realDeliveryMode, int realPriority, long realTimeToLive) throws JMSException
    {
        send(destination, message, realDeliveryMode, realPriority, realTimeToLive);
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination, jakarta.jms.Message, int, int, long)
     */
    public void send(Destination realDestination, Message message, int realDeliveryMode, int realPriority, long realTimeToLive) throws JMSException
    {
        if (state == State.CLOSED)
        {
            throw new IllegalStateException("DwrMessageProducer has been closed");
        }

        if (!disableMessageID)
        {
            message.setJMSMessageID(UUID.randomUUID().toString());
        }

        if (!disableMessageTimestamp)
        {
            message.setJMSTimestamp(new Date().getTime());
        }

        if (realDestination instanceof DwrTopic)
        {
            DwrTopic dwrtopic = (DwrTopic) realDestination;
            String topicName = dwrtopic.getTopicName();

            hub.publish(topicName, message);
        }
        else
        {
            throw new IllegalStateException("Unsuported Destination type (" + realDestination.getClass().getCanonicalName() + "). Only Topics are currently supported.");
        }
    }

    @Override
    public void send(Message message, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Destination destination, Message message, CompletionListener completionListener) throws JMSException
    {

    }

    @Override
    public void send(Destination destination, Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException
    {

    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#setDeliveryMode(int)
     */
    public void setDeliveryMode(int deliveryMode) throws JMSException
    {
        this.deliveryMode = deliveryMode;
        throw Unsupported.noManualAcknowledgment();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#setDisableMessageID(boolean)
     */
    public void setDisableMessageID(boolean disableMessageID) throws JMSException
    {
        this.disableMessageID = disableMessageID;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#setDisableMessageTimestamp(boolean)
     */
    public void setDisableMessageTimestamp(boolean disableMessageTimestamp) throws JMSException
    {
        this.disableMessageTimestamp = disableMessageTimestamp;
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#setPriority(int)
     */
    public void setPriority(int priority) throws JMSException
    {
        this.priority = priority;
        throw Unsupported.noMessagePriority();
    }

    /* (non-Javadoc)
     * @see jakarta.jms.MessageProducer#setTimeToLive(long)
     */
    public void setTimeToLive(long timeToLive) throws JMSException
    {
        this.timeToLive = timeToLive;
        throw Unsupported.noMessageExpiry();
    }

    /**
     * The DWR hub that does the real work
     */
    private Hub hub;

    /**
     * The default queue or topic
     */
    private final Destination destination;

    /**
     * For queues: are messages persistent
     */
    private int deliveryMode;

    /**
     * Have we been closed?
     */
    private State state;

    /**
     * Do we skip assigning message IDs?
     */
    private boolean disableMessageID;

    /**
     * Do we skip assigning timestamps
     */
    private boolean disableMessageTimestamp;

    /**
     * What is the default priority for messages we produce
     */
    private int priority;

    /**
     * How long before the system can delete messages that we produce
     */
    private long timeToLive;
}
