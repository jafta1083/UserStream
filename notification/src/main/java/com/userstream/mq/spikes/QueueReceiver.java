package com.userstream.mq.spikes;


import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import com.userstream.mq.MQ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueueReceiver implements Runnable{
    private static long NAP_TIME = 2000; //ms

    public static final String MQ_QUEUE_NAME = "username";

    public static void main( String[] args ){
        final QueueReceiver app = new QueueReceiver();
        app.run();
    }

    private boolean running = true;

    private Connection connection;

    @Override
    public void run(){
        setUpMessageListener();
        while( running ){
            // do other stuff...
            System.out.println( "Still doing other things..." );
            snooze();
        }

        // CAUTION: Notice that we OPEN the MQ `Connection` down in `setUpMessageListener()`,
        // but we CLOSE it here in the higher-level method that _called_ `setUpMessageListener()`.
        //
        // In general we consider this to be Bad Practise. A Code Smell. You should strive to keep
        // OPEN/CLOSE pairs (of anything: MQ connections, database connections, network connections,
        // files...) at the SAME level in your code, making it easier to see that the
        // OPENed object gets properly CLOSEd.
        //
        // FIXME: There are at least 3 ways to fix this "Unbalanced Open/Close" code smell.

        closeConnection();
        System.out.println( "Bye..." );
    }

    /**
     * Set up a MQ Session and hook a MessageListener into the MQ machinery. Do this right and,
     * whenever a new Message arrives on the Queue we want to watch, our MessageListener's
     * `onMessage()` method will get called so that we can do something useful with the message.
     */
    private void setUpMessageListener(){

        try{
            final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory( MQ.URL );
            connection = factory.createConnection( MQ.USER, MQ.PASSAWD);
            connection.start(); // START connection BEFORE creating consumer

            final Session session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
            final Destination queueId = session.createQueue( MQ_QUEUE_NAME );

            final MessageConsumer receiver = session.createConsumer( queueId );
            receiver.setMessageListener( new MessageListener() { //this anonymous inner-class could be replaced with a lambda
                                             @Override
                                             public void onMessage( Message m ){
                                                 if (m instanceof TextMessage) {
                                                     String body =  null;
                                                     try {
                                                         body = ((TextMessage) m).getText();
                                                     } catch (JMSException e) {
                                                         throw new RuntimeException(e);
                                                     }
                                                     if ("SHUTDOWN".equals(body)) {
                                                         System.exit(1);
                                                     } else {
                                                         System.out.println("Received message: " + body);
                                                     }
                                                 }
                                             }
                                         }
            );

        }catch (JMSException exception){
            throw new RuntimeException("Connection is not successful ");
        }
    }

    private void snooze(){
        try{
            Thread.sleep( NAP_TIME );
        }catch( InterruptedException eek ){
            // meh...
        }
    }

    private void closeConnection(){
        if( connection != null ) try{
            connection.close();
        }catch( JMSException ex ){
            // meh
        }
    }

}
