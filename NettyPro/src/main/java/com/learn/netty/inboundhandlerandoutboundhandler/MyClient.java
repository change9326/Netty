package com.learn.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yujiaqi
 * @date 2020-06-02 10:36
 * @description
 */
public class MyClient {

    /**
     * 针对客户端来说
     *   当事件的运动方向是客户端到服务端，称之为出栈【tail->head】
     *   当事件的运动方向是服务端到客户端，，称之为入栈【head->tail】
     */
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个出站的handler 对数据进行一个编码 @2
                            pipeline.addLast(new MyLongToByteEncoder());
                            //这时一个入站的解码器(入站handler )
                            pipeline.addLast(new MyByteToLongDecoder());
                            //加入一个自定义的handler ， 处理业务 @1
                            pipeline.addLast(new MyClientHandler());

                        }
                    });
            System.out.println("客户端 is ready......");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
