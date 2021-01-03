package com.example.netty.demo.disruptor.consumer;

import com.example.netty.demo.disruptor.event.OrderEvent;
import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: yanuo
 * @create: 20201222 10:25 AM
 */

public class Consumer implements WorkHandler<OrderEvent> {

    private static AtomicInteger count = new AtomicInteger(0);
    private String comsumerId;
    private Random random = new Random();

    public Consumer(String comsumerId) {
        this.comsumerId = comsumerId;
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        System.out.println("当前消费者：" + this.comsumerId);
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }


}
