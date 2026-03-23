package com.userstream.mq.spikes;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Arrays;

public class TopicSender implements Runnable {

    private static long NAP_TIME = 2000;

    public static final String MQ_URL = "tcp://localhost:61616";
    public static final String MQ_USER = "admin";
    public static final String MQ_PASSWD = "admin";
    public static final String MQ_TOPIC_NAME = "username";

    public static void main(String[] args) {
        final TopicSender app = new TopicSender();
        app.cmdLineMsgs = args;
        new Thread(app).start();
    }

    private String[] cmdLineMsgs;

    private Connection connection;

    private Session session;

    @Override
    public void run() {
        try {
            final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MQ_URL);
            connection = factory.createConnection(MQ_USER, MQ_PASSWD);
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            sendMessages(cmdLineMsgs == null || cmdLineMsgs.length == 0
                    ? new String[]{"{ \"stage\":17 }"}
                    : cmdLineMsgs);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
            System.out.println("Bye...");
        }
    }

    private void sendMessages(String[] messages) throws JMSException {
        Destination destination = session.createTopic(MQ_TOPIC_NAME);
        MessageProducer producer = session.createProducer(destination);

        for (String msg : messages) {
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
            System.out.println("Sent: " + msg);
        }
    }

    private void closeResources() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException ignored) {
        }
        session = null;
        connection = null;
    }
}