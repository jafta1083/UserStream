package com.userstream.mq;

import javax.jms.*;
import java.util.function.Consumer;

public class ActiveMqReceiver implements MQ, MessageListener, AutoCloseable {

    private Consumer<Message> handler;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private DestinationType destinationType;
    private String currentDestination;




    @Override
    public void close(){
        try {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }catch (JMSException e){
            throw new RuntimeException("Failed to close receiver: " + e);
        }
    }

    @Override
    public void onMessage(Message message){
        if (handler != null){
            handler.accept(message);
        }
    }
}
