package com.example.netty.common.entity.dto;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author: yanuo
 * @create: 20201230 12:25 AM
 */

@Data
public class ContentWrapper {
    private ContentDTO contentDTO;
    private ChannelHandlerContext ctx;

}
