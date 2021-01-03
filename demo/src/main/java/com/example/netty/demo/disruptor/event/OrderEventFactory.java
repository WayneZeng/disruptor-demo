package com.example.netty.demo.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * @author: yanuo
 * @create: 20201210 1:38 PM
 */

public class OrderEventFactory implements EventFactory<com.example.netty.demo.disruptor.event.OrderEvent> {

    @Override
    public com.example.netty.demo.disruptor.event.OrderEvent newInstance() {
        return new com.example.netty.demo.disruptor.event.OrderEvent();
    }

}
