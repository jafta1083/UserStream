package com.userstream.mq;

import org.apache.activemq.broker.BrokerService;

public class Broker implements MQ{

    private  static BrokerService broker;

    static {
        try {
            if (URL.startsWith("vm://")){
                broker = new BrokerService();
                broker.setBrokerName("localhost");
                broker.setPersistent(false);
                broker.setUseJmx(false);

                try {
                    broker.addConnector("tcp://localhost:61616");
                }catch (Exception tcpError){
                    if (tcpError.getMessage() != null && tcpError.getMessage().contains("Address already in use")){
                        System.out.println("TCP port 61616 already in use, continuing with vm:// only");
                    }else {
                        throw tcpError;
                    }
                }
                broker.start();
                System.out.println("Embedded ActiveMQ broker started on vm://localhost");
              }else {
                System.out.println("Using external ActiveMQ broker at: " + URL);
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to start embedded broker: " + e);
        }
    }


    public Broker(){

    }

    @Override
    public void close(){

    }

}
