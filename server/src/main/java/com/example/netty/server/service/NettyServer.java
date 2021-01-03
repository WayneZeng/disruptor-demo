package com.example.netty.server.service;

import com.example.netty.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: yanuo
 * @create: 20201228 8:48 PM
 */

public class NettyServer {

    public NettyServer() {
        // 1 两个线程组，处理请求 + work组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 2
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 缓存区动态配置
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    // 缓冲池
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader()))); // todo
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });

            // bind ip and port, sync 同步等待
            ChannelFuture sync = serverBootstrap.bind(8765).sync();
            System.out.println("server startup...");
            sync.channel().closeFuture().sync(); // 异步关闭

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("server shutdown...");
        }

    }

}
