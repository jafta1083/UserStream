package com.userstream.mq;


import org.apache.activemq.ActiveMQConnectionFactory;
import com.userstream.mq.test.NullTopicReceiver;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.jms.*;
import java.util.function.Consumer;

public class ActiveMqReceiver implements MQ, MessageListener, AutoCloseable {
    private static final Logger logger = LogManager.getLogger(ActiveMqReceiver.class);

    private Consumer<Message> handler;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private DestinationType destinationType;
    private String currentDestination;


    private ActiveMqReceiver(){
        this(DestinationType.QUEUE);
    }

    public ActiveMqReceiver(String someString){
        this(DestinationType.QUEUE);
    }
    public ActiveMqReceiver(DestinationType destinationType){
        this.destinationType = destinationType;

        new Broker();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(URL);
            factory.setTrustAllPackages(true);
            connection = factory.createConnection(USER,PASSAWD);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
        } catch (JMSException jms) {
            logger.info("Failed to create ActiveMQ receiver: ", jms);
        }
    }

    public ActiveMqReceiver listenOn(String destination, Consumer<Message> handler){
        if (destinationType == DestinationType.QUEUE){
            logger.error("Cannot use listenOn() with QUEUE - use receive() instead");
        }

        this.handler = handler;
        try{
            Destination destination1 = session.createTopic(destination);
            consumer = session.createConsumer(destination1);
            consumer.setMessageListener(this);
        }catch (JMSException jms){
            logger.info("Failed to set up listener: ",  jms);
        }
        return this;
    }

    public ActiveMqReceiver openOn(String destination){
        if (destinationType == DestinationType.TOPIC) {
            logger.warn("Cannot use openOn() with TOPIC");
        }

        try {
            if(consumer != null){
                consumer.close();
            }
            Destination dest = session.createQueue(destination);
            consumer = session.createConsumer(dest);
            currentDestination = destination;

            Thread.sleep(200);
        }catch (JMSException jms){
            logger.error("Failed to open queue: " , jms);
        }catch (InterruptedException ind){
            Thread.currentThread().interrupt();
            logger.error("Interrupted while setting up receiver", ind);
        }
        return this;
    }

   public Message receive(String destination) {
       try{

           if (destinationType == DestinationType.TOPIC) {
               openOn(destination);
           }
           Message message = consumer.receive(1000);

           if (message == null) {
               message = session.createTextMessage("TypingMessage...");
           }
           return message;
       }catch(JMSException jms){
           logger.warn("Failed to receive message: ", jms);

       }
       return null;
   }


    @Override
    public void close(){
        try {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }catch (JMSException jms){
            logger.warn("Failed to close receiver: ", jms);
        }
    }

    @Override
    public void onMessage(Message message){
        if (handler != null){
            handler.accept(message);
        }
    }
}
