package com.userstream.mq.spikes;

import com.userstream.mq.MQ;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicReceiver implements Runnable {

    private static long NAP_TIME = 2000;

    public static final String MQ_TOPIC_NAME = "username";

    public static void main(String[] args) {
        final TopicReceiver app = new TopicReceiver();
        new Thread(app).start();
    }

    private boolean running = true;

    private Connection connection;

    @Override
    public void run() {
        setUpMessageListener();
        while (running) {
            System.out.println("Still doing stuff...");
            snooze();
        }
        closeConnection();
        System.out.println("Bye...");
    }

    private void setUpMessageListener() {
        try {
            final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MQ.URL);
            connection = factory.createConnection(MQ.USER, MQ.PASSAWD);

            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination dest = session.createTopic(MQ_TOPIC_NAME);

            final MessageConsumer receiver = session.createConsumer(dest);
            receiver.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message m) {
                    if (m instanceof TextMessage) {
                        try {
                            String body = ((TextMessage) m).getText();
                            if ("SHUTDOWN".equals(body)) {
                                running = false;
                            } else {
                                System.out.println("Received message: " + body);
                            }
                        } catch (JMSException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            connection.start();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private void snooze() {
        try {
            Thread.sleep(NAP_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}