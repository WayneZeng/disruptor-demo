package com.example.netty.demo.disruptor.event;

import lombok.Data;

/**
 * @author: yanuo
 * @create: 20201210 1:32 PM
 */

@Data
public class OrderEvent {

    private volatile long value = 0L;
    private long p1, p2, p3, p4, p5, p6, p7;

    public OrderEvent(long value) {
        this.value = value;
    }

    public OrderEvent() {

    }

}
