package com.learn.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yujiaqi
 * @date 2020-06-02 10:36
 * @description
 */
public class MyServer {

    /**
     * 相对server端
     * - 事件的运动方向从客户端到服务端，是入栈操作【head->tail】
     * - 事件的运动方向从服务端到客户端，是出栈操作【tail->head】
     */
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 入站的handler进行解码 MyByteToLongDecoder @1
                            pipeline.addLast(new MyByteToLongDecoder());
                            //出站的handler进行编码
                            pipeline.addLast(new MyLongToByteEncoder());
                            // 自定义的handler 处理业务逻辑 @2
                            pipeline.addLast(new MyServerHandler());

                        }
                    });
            System.out.println("服务端 is ready....");
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
