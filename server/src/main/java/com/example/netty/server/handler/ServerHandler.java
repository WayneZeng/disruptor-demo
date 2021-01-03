package com.example.netty.server.handler;

import com.example.netty.common.disruptor.MessageProducer;
import com.example.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.example.netty.common.entity.dto.ContentDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: yanuo
 * @create: 20201229 1:44 PM
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContentDTO request = (ContentDTO) msg;

        String producerId = "sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);

        messageProducer.sendData(request, ctx);
    }
}
