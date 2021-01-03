package com.example.netty.demo.disruptor.consumer;

import com.example.netty.demo.disruptor.event.OrderEvent;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * @author: yanuo
 * @create: 20201222 10:22 AM
 */

public class ConsumerTest {


    public static void main(String[] args) {
        RingBuffer<OrderEvent> ringBuffer =
                RingBuffer.create(ProducerType.MULTI,
                        new EventFactory<OrderEvent>() {
                            @Override
                            public OrderEvent newInstance() {
                                return null;
                            }
                        },
                        1024 * 1024,
                        new YieldingWaitStrategy());
        //
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 构建多消费者
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C" + i);
        }

        //
        WorkerPool<OrderEvent> workerPool = new WorkerPool<OrderEvent>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers
        );

        // 设置多消费者 sequence序号，消费者的进度要加入到ringbuffer中，ringbuffer
        // producer找到最小进度，然后判定是否要覆盖投递
        // todo:
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 启动
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    }

    static class EventExceptionHandler implements ExceptionHandler<OrderEvent> {

        @Override
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
