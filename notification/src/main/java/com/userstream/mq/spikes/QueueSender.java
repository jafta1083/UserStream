package com.userstream.mq.spikes;

import com.userstream.mq.ActiveMqReceiver;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;

import java.util.Arrays;

public class QueueSender implements Runnable {
    private static final Logger logger = LogManager.getLogger(QueueSender.class);

    private String[] cmdLineMsgs = new String[]{};
    private Connection connection;
    private Session session;


    private static long NAP_TIME = 2000l;

    public static final String MQ_URL = "tcp://localhost:61616";

    public static final String MQ_USER = "admin";

    public static final String MQ_PASSWD = "admin";

    public static final String MQ_QUEUE_NAME = "username";

    public static void main(String[] args){
        final QueueSender app = new QueueSender();
        app.run();
    }

    @Override
    public void run(){
        try{
            final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MQ_URL);
            connection = factory.createConnection(MQ_USER, MQ_PASSWD);
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sendAllMessages(cmdLineMsgs.length == 0
                ? new String[]{ "{ \"stage\":17 }" }
                : cmdLineMsgs);

        }catch (JMSException jms){
            logger.warn("The queue name is not working",jms);
        }finally {
            closeResources();
        }
        logger.info("Bye...");
    }



    private void sendAllMessages( String[] messages ) throws JMSException {
        Destination destination = session.createQueue(MQ_QUEUE_NAME);
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage(Arrays.toString(messages));

        producer.send(message);
        System.out.println("Message Sent!");
    }

    private void closeResources(){
        try{
            if( session != null ) session.close();
            if( connection != null ) connection.close();
        }catch( JMSException ex ){
            // wut?
        }
        session = null;
        connection = null;
    }

}
