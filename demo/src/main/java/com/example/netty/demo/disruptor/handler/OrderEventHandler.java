package com.example.netty.demo.disruptor.handler;

import com.example.netty.demo.constant.Constant;
import com.example.netty.demo.disruptor.event.OrderEvent;
import com.lmax.disruptor.EventHandler;

/**
 * consumer
 *
 * @author: yanuo
 * @create: 20201210 1:43 PM
 */

public class OrderEventHandler implements EventHandler<OrderEvent> {

    private final long startTime;


    public OrderEventHandler() {
        this.startTime = System.currentTimeMillis();

    }

    /**
     * @param event      数据
     * @param sequence
     * @param endOfBatch
     * @throws Exception
     */
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        long value = event.getValue();
        // System.out.println("value:" + value + "," + "startTime:" + this.startTime);
        if ((value + 1) >= Constant.EVENT_NUM_OHM) {
            System.out.println("distruptor cost time:" + (System.currentTimeMillis() - startTime));
        }

    }

}
