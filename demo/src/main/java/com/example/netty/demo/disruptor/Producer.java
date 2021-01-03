package com.example.netty.demo.disruptor;

import com.example.netty.demo.constant.Constant;
import com.example.netty.demo.disruptor.event.OrderEvent;
import com.example.netty.demo.disruptor.event.OrderEventFactory;
import com.example.netty.demo.disruptor.handler.OrderEventHandler;
import com.example.netty.demo.disruptor.producer.OrderEventProducer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author: yanuo
 * @create: 20201210 2:25 PM
 */

public class Producer {

    public static void main(String[] args) {
        // 1 -
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        ExecutorService es = newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        /**
         * 1 EVENT producer factory:
         * ringbuffer size
         * executor 自定义线程池
         * produceType
         * WaitStrategy
         */
        Disruptor<OrderEvent> disruptor = new Disruptor<>(orderEventFactory,
                ringBufferSize,
                es,
                ProducerType.MULTI, // Single or Multi
                new BlockingWaitStrategy());

        // 2 add listener
        disruptor.handleEventsWith(new OrderEventHandler());

        // 3 start
        disruptor.start();

        // 4 produce: get container: RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i = 0; i < Constant.EVENT_NUM_OHM; i++) {
            bb.putLong(0, i);
            producer.sendData(bb);
        }
        disruptor.shutdown();
        es.shutdown();
    }
}
