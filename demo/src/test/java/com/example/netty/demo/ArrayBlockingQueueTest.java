package com.example.netty.demo;


import com.example.netty.demo.constant.Constant;
import com.example.netty.demo.disruptor.event.OrderEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * @author: yanuo
 * @create: 20201201 8:04 PM
 */

public class ArrayBlockingQueueTest {

    public static void main(String[] args) {
        final ArrayBlockingQueue<OrderEvent> queue = new ArrayBlockingQueue<>(1000 * 10000);
        final long startTime = System.currentTimeMillis();

        Runnable produceRunnable = new Runnable() {

            @Override
            public void run() {
                long i = 0;
                while (i < Constant.EVENT_NUM_OHM) {
                    try {
                        queue.put(new OrderEvent(i));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        };

        Executors.newFixedThreadPool(1).submit(produceRunnable);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int k = -1;
                while ((k + 1) < Constant.EVENT_NUM_OHM) {
                    try {
                        OrderEvent take = queue.take();
                        long value = take.getValue();
                        if ((value + 1) >= Constant.EVENT_NUM_OHM) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    k++;
                }
                long endTime = System.currentTimeMillis();
                System.out.println(">>>>blocking queue cost time:" + (endTime - startTime));
            }
        }).start();
    }
}
