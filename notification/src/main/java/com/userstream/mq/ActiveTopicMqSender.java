package com.userstream.mq;

public abstract class ActiveTopicMqSender {


    public ActiveTopicMqSender init(String topic){
        return this;
    }

    public abstract void init();

    public void send(String text){
        return ;
    }

    public  void close(){
        return;
    }
}
