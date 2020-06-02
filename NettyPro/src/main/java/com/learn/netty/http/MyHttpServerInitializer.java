package com.learn.netty.http;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author yujiaqi
 * @date 2020-05-28 11:56
 * @description
 */
public class MyHttpServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();
        // HttpServerCodec 是netty 提供的处理http的 编-解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 添加自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new HttpServerHandler());
    }
}
