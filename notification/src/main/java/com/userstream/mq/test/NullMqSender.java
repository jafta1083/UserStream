package com.userstream.mq.test;

import com.userstream.mq.ActiveTopicMqSender;

public class NullMqSender extends NullTopicSender {

    @Override
    public void init(){

    }

    @Override
    public void send(String message){

    }

    @Override
    public void close(){

    }
}
