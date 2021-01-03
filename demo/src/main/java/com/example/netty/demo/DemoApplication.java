package com.example.netty.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.netty.demo.server.NettyServer;
import java.net.InetSocketAddress;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		//启动服务端
		NettyServer nettyServer = new NettyServer();
		nettyServer.start(new InetSocketAddress("127.0.0.1", 8090));
	}
}
