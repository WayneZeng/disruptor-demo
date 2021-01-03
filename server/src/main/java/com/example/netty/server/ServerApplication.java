package com.example.netty.server;

import com.example.netty.common.disruptor.MessageConsumer;
import com.example.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.example.netty.server.consumer.ServerMessageConsumerImp;
import com.example.netty.server.service.NettyServer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class  ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);

        // 1 - 启动ringbuffer
        MessageConsumer[] consumers = new MessageConsumer[10];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new ServerMessageConsumerImp("code:serverId:" + i);
            consumers[i] = messageConsumer;
        }

        RingBufferWorkerPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024 * 1024,
                new BlockingWaitStrategy(),
                consumers
        );

        new NettyServer();
    }

}
