package com.userstream.mq;

import com.userstream.mq.test.NullTopicReceiver;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

import javax.jms.*;

public class ActiveMqSender extends ActiveTopicMqSender implements MQ{
    private static final Logger logger = LogManager.getLogger(ActiveMqSender.class);
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private final DestinationType destinationType;
    private String currentDestinationName;

    /**
     *
     */

   public ActiveMqSender(){
       this(DestinationType.QUEUE);
   }

   public ActiveMqSender(String someString){
       this(DestinationType.QUEUE);
   }

    public ActiveMqSender(DestinationType destinationType) {
        this.destinationType = destinationType;

        new Broker();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(URL);
            connection = factory.createConnection(USER, PASSAWD);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }catch (JMSException jmx){
            logger.info("Failed to create ActiveMQ sender: ", jmx);
        }
    }
    
    
    public ActiveMqSender openOn(String destinationName){
       this.currentDestinationName = destinationName;
       try {
           Destination destination = destinationType == DestinationType.QUEUE
                   ? session.createQueue(destinationName)
                   : session.createTopic(destinationName);
           producer = session.createProducer(destination);
       }catch (JMSException jmx){
           logger.info("Failed to open destination: ", jmx);
       }
       return this;
    }

    @Override
    public void init(){

    }

    public NullTopicReceiver init(String topic, Consumer<Message> messageConsumer){
       return null;
    }

    public void run(){
    }
    public void send(String messageText){
       if (producer == null){
           logger.info("Must call openOn() before send()");
       }
       try {
           TextMessage message = session.createTextMessage(messageText);
           producer.send(message);
       }catch (JMSException jms){
           logger.warn("Failed to send message: ", jms);
       }
    }

    public void send(Message message){
       if (producer == null){
           logger.info("Must call openOn() before send()");
       }
       try {
           producer.send(message);
       } catch (JMSException jms) {
           logger.warn("Failed to send message: ", jms);
       }
    }



    @Override
    public void close() {

       try {
           if (producer!= null) producer.close();
           if (session != null) session.close();
           if (connection != null) connection.close();
       } catch (JMSException jms) {
           logger.warn("Failed to close sender: ", jms);
       }

    }
}
