package com.example.netty.demo.disruptor.producer;

import com.example.netty.demo.disruptor.event.OrderEvent;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author: yanuo
 * @create: 20201210 5:01 PM
 */

public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer bb) {
        // 1 get availiable sequence id in ringbuffer
        long sequence = ringBuffer.next();

        try {
            // 2 find empty Event in ringbuffer according to the sequence
            OrderEvent orderEvent = ringBuffer.get(sequence);

            // 3
            orderEvent.setValue(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }

    }

    public void sendData(long bb) {
        // 1 get availiable sequence id in ringbuffer
        long sequence = ringBuffer.next();

        try {
            // 2 find empty Event in ringbuffer according to the sequence
            OrderEvent orderEvent = ringBuffer.get(sequence);

            // 3
            orderEvent.setValue(bb);
        } finally {
            ringBuffer.publish(sequence);
        }

    }
}
