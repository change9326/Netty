package com.learn.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yujiaqi
 * @date 2020-05-28 11:44
 * @description
 */
public class HttpServer {
    public static void main(String[] args)  {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置两个线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用使用NioServerSocketChannel 作为服务器通道的实现
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyHttpServerInitializer());
            System.out.println("ok~~~~");

            // 绑定端口并启动服务器
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            // 对关闭通道进行监听
            System.out.println("服务器启动成功。监听的端口号为：8888");
            /**
             * 在这里面future.channel().closeFuture().sync();这个语句的主要目的是，如果缺失上述代码，
             * 则main方法所在的线程，即主线程会在执行完bind().sync()方法后，会进入finally 代码块，
             * 之前的启动的nettyserver也会随之关闭掉，整个程序都结束了。
             *
             * 让线程进入wait状态，也就是main线程暂时不会执行到finally里面，nettyserver也持续运行，如果监听到关闭事件，可以优雅的关闭通道和nettyserver，
             */
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
