package com.learn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yujiaqi
 * @date
 * @description:服务器启动类
 */
public class HttpServer {

    public static void main(String[] args) {
        // 用于处理客户端连接请求,将请求发送给childGroup 中的eventLoop
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于处理客户端请求
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        // 用于启动ServerChannel
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                 // 指定eventLoopGroup
                .group(parentGroup,childGroup)
                // 指定使用NIO 进行通信
                .channel(NioServerSocketChannel.class)
                // 指定childGroup 中的eventLoop 所绑定的线程所要处理的处理器
                .childHandler(new SomeChannelInitializer());
        try {
            // 指定当前服务器所监听的端口号
            // bind() 方法的执行是异步的
            // sync() 方法会使bind()操作与后续的代码的执行由异步变为同步。
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            System.out.println("服务器已经启动，监听的端口号为：8888");

            //  关闭chanel
            // closeFuture() 的操作的是异步的。
            // 当Channel调用了close()方法并关闭成功后才会触发closeFuture()方法的执行
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 优雅关闭
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
