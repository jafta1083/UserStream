package com.userstream.mq;


public interface MQ {

    static final String URL = "tcp://localhost:61616";

    static final String USER = "admin";

    static final String PASSAWD = "admin";

    static final String PERSONAL_DETAILS_TOPIC = "personal_details";

    static final String ALERT_QUEUE = "alert";

    static final String TEST_TOPIC = "test_topic";

    static final String TEST_QUEUE = "test_queue";


    static final Broker EMBEDDED_BROKER = new Broker();

    void close();

    enum DestinationType{

        QUEUE,
        TOPIC
    }
}
