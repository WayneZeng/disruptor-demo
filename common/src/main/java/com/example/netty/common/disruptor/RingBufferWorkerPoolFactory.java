package com.example.netty.common.disruptor;

import com.example.netty.common.entity.dto.ContentWrapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @author: yanuo
 * @create: 20201229 10:11 PM
 */

public class RingBufferWorkerPoolFactory {

    private static class SingletonHolder {
        static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
    }

    private RingBufferWorkerPoolFactory() {

    }

    public static RingBufferWorkerPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();

    // todo: 什么时候销毁 MessageConsumer
    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    private RingBuffer<ContentWrapper> ringBuffer;

    private SequenceBarrier sequenceBarrier;

    private WorkerPool<ContentWrapper> workerPool;

    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        // create ringbuffer
        this.ringBuffer = RingBuffer.create(type,
                new EventFactory<ContentWrapper>() {
                    @Override
                    public ContentWrapper newInstance() {
                        return new ContentWrapper();
                    }
                }, bufferSize, waitStrategy);
        // get barrier
        this.sequenceBarrier = this.ringBuffer.newBarrier();

        // set workpool
        this.workerPool = new WorkerPool<ContentWrapper>(this.ringBuffer,
                this.sequenceBarrier, new EventExceptionHandler(),
                messageConsumers);

        // 消费者放入池
        for (MessageConsumer messageConsumer : messageConsumers) {
            consumers.put(messageConsumer.getConsumerId(), messageConsumer);
        }
        // add sequences
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());

        // start
        this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2));
    }

    public MessageProducer getMessageProducer(String produceId) {
        MessageProducer messageProducer = producers.get(produceId);
        if (messageProducer == null) {
            messageProducer = new MessageProducer(produceId, this.ringBuffer);
            producers.put(produceId, messageProducer);
        }
        return messageProducer;
    }

    static class EventExceptionHandler implements ExceptionHandler<ContentWrapper> {

        @Override
        public void handleEventException(Throwable ex, long sequence, ContentWrapper event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
