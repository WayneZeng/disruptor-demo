package com.example.netty.server.consumer;

import com.example.netty.common.disruptor.MessageConsumer;
import com.example.netty.common.entity.dto.ContentDTO;
import com.example.netty.common.entity.dto.ContentWrapper;

/**
 * @author: yanuo
 * @create: 20201230 10:25 AM
 */

public class ServerMessageConsumerImp extends MessageConsumer {

    public ServerMessageConsumerImp(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(ContentWrapper event) throws Exception {
        ContentDTO contentDTO = event.getContentDTO();
        System.out.println(">>> get:" + contentDTO);

        ContentDTO response = new ContentDTO();
        response.setId("responseId :" + contentDTO.getId());
        response.setName("responseName :" + contentDTO.getName());
        response.setMessage("responseMessage :" + contentDTO.getMessage());

        event.getCtx().writeAndFlush(response);
    }
}
