package com.example.netty.client.consumer;

import com.example.netty.common.disruptor.MessageConsumer;
import com.example.netty.common.entity.dto.ContentDTO;
import com.example.netty.common.entity.dto.ContentWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author: yanuo
 * @create: 20201230 10:41 AM
 */

public class ClientMessageConsumer extends MessageConsumer {

    public ClientMessageConsumer(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(ContentWrapper event) throws Exception {
        ContentDTO response = event.getContentDTO();
        ChannelHandlerContext ctx = event.getCtx();

        try {
            System.out.println("response:" + response);
        } finally {
            ReferenceCountUtil.release(response);
        }
    }
}
