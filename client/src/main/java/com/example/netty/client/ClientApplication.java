package com.example.netty.client;

import com.example.netty.client.consumer.ClientMessageConsumer;
import com.example.netty.client.service.NettyClient;
import com.example.netty.common.disruptor.MessageConsumer;
import com.example.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);

        // 1 - 启动ringbuffer
        MessageConsumer[] consumers = new MessageConsumer[10];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new ClientMessageConsumer("code:clientId:" + i);
            consumers[i] = messageConsumer;
        }

        RingBufferWorkerPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024 * 1024,
                new BlockingWaitStrategy(),
                consumers
        );

        new NettyClient().sendData();
    }

}
