package com.userstream.mq.test;

import com.userstream.mq.ActiveTopicMqSender;

public class NullTopicSender extends ActiveTopicMqSender {

    @Override
    public ActiveTopicMqSender init(String topic) {

        return this;
    }

    @Override
    public void init() {

    }

    @Override
    public void send(String text) {

    }

    @Override
    public void close() {
    }


}
