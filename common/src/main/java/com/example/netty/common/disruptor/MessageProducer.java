package com.example.netty.common.disruptor;

import com.example.netty.common.entity.dto.ContentDTO;
import com.example.netty.common.entity.dto.ContentWrapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: yanuo
 * @create: 20201230 12:05 AM
 */

public class MessageProducer {

    private String producerId;

    private RingBuffer<ContentWrapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<ContentWrapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ContentDTO contentDTO, ChannelHandlerContext ctx) {
        long sequence = ringBuffer.next();
        try {
            ContentWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setContentDTO(contentDTO);
            wrapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
