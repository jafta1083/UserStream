package com.userstream.mq;

import javax.jms.Message;
import java.util.function.Consumer;

public class ActiveTopicMqReceiver {

    public ActiveTopicMqReceiver init(String topic,Consumer<Message> text){

        return this;
    }

    public void run(){

    }

    public void close(){

    }
}
