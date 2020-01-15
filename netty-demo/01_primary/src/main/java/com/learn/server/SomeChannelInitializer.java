package com.learn.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author yujiaqi
 * @date
 * @description： 管道初始化器：当前类的实例在pipeline 初始化完毕后就会被GC
 *
 *
 */
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 从Channel中获取pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 将HttpServerCodec处理器放入到pipeline的最后
        // HttpServerCodec是什么？是HttpRequestDecoder与HttpResponseEncoder的复合体
        // HttpRequestDecoder：http请求解码器，将Channel中的ByteBuf数据解码为HttpRequest对象
        // HttpResponseEncoder：http响应编码器，将HttpResponse对象编码为将要在Channel中发送的ByteBuf数据
        pipeline.addLast(new HttpServerCodec());
        // 将自再定义的处理器放入到Pipeline的最后
        pipeline.addLast(new SomeServerHandler());
    }
}
