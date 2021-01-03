package com.example.netty.common.disruptor;

import com.example.netty.common.entity.dto.ContentWrapper;
import com.lmax.disruptor.WorkHandler;
import lombok.Data;

/**
 * @author: yanuo
 * @create: 20201230 12:05 AM
 */

@Data
public abstract class MessageConsumer implements WorkHandler<ContentWrapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public abstract void onEvent(ContentWrapper event) throws Exception;
}
