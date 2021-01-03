package com.example.netty.demo;

import com.example.netty.demo.constant.Constant;
import com.example.netty.demo.disruptor.event.OrderEvent;
import com.example.netty.demo.disruptor.event.OrderEventFactory;
import com.example.netty.demo.disruptor.handler.OrderEventHandler;
import com.example.netty.demo.disruptor.producer.OrderEventProducer;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: yanuo
 * @create: 20201208 4:35 PM
 */

public class DisrupterTest {
    public static void main(String[] args) throws InterruptedException {
        final long startTime = System.currentTimeMillis();

        EventFactory<OrderEvent> factory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<OrderEvent> disruptor =
                new Disruptor<OrderEvent>(factory, ringBufferSize, (Executor) Executors.newFixedThreadPool(1), ProducerType.SINGLE, new YieldingWaitStrategy());
        //设置一个消费者
        System.out.println("init time:" + (System.currentTimeMillis() - startTime));
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        //单生产者，生产3条数据
        disruptor.handleEventsWith(new OrderEventHandler());
        disruptor.start();
        for (long i = 0; i < Constant.EVENT_NUM_OHM; i++) {
            //bb.putLong(0, i);
            producer.sendData(i);
        }

        Thread.sleep(1000);
        System.out.println(">>>>end");
        disruptor.shutdown();
    }
}
