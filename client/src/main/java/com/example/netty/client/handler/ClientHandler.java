package com.example.netty.client.handler;

import com.example.netty.common.disruptor.MessageProducer;
import com.example.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.example.netty.common.entity.dto.ContentDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author: yanuo
 * @create: 20201229 3:04 PM
 */

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //

            ContentDTO response = (ContentDTO) msg;
            System.out.println("Client recieve:" + response);

            String producerId = "code:sessionId:002";
            MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);

            messageProducer.sendData(response, ctx);

    }
}
