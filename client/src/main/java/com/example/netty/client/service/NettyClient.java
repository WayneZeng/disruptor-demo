package com.example.netty.client.service;

import com.example.netty.client.handler.ClientHandler;
import com.example.netty.common.entity.dto.ContentDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: yanuo
 * @create: 20201229 2:52 PM
 */

public class NettyClient {

    public static final String HOST = "127.0.0.1";

    public static final int PORT = 8765;

    private Channel channel;  // 扩展池化，不同端口不同服务，ConcurrentHashMap<String:, Channel>

    private EventLoopGroup workGroup = new NioEventLoopGroup(); // 创建work ，不需要boss，因为不需要客户端处理连接

    public NettyClient() {
        this.connect(HOST, PORT);
    }

    private void connect(String host, int port) {

        //  注意server是： ServerBootstrap serverBootstrap = new ServerBootstrap();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //与服务端不同,这里禁止对类加载器进行缓存
                            ch.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture connect = bootstrap.connect(host, port).sync();
            System.out.println("client connected..." + host + ":" + port);

            // 数据发送
            // todo: 池化
            this.channel = connect.channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendData() {

        System.out.println("start send data...");
        for (int i = 0; i < 10; i++) {
            ContentDTO data = new ContentDTO();
            data.setId("id:" + i);
            data.setName("name:" + i);
            data.setMessage("message:" + i);

            this.channel.writeAndFlush(data);
        }
    }

    public void close() throws InterruptedException {
        this.channel.closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("client exit...");

    }

}
