package com.userstream.mq.test;

import com.userstream.mq.ActiveMqSender;

import javax.jms.Message;
import java.util.function.Consumer;

public class NullTopicReceiver extends ActiveMqSender {


    @Override
    public NullTopicReceiver init(String topic, Consumer<Message> text){
        return this;

    }

    @Override
    public void run(){
        return;
    }

    @Override
    public void init() {

    }

    @Override
    public void send(String message){

    }

    @Override
    public void close(){

    }
}
