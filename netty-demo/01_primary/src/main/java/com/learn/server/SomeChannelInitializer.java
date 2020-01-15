package com.learn.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author yujiaqi
 * @date
 * @description�� �ܵ���ʼ��������ǰ���ʵ����pipeline ��ʼ����Ϻ�ͻᱻGC
 *
 *
 */
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // ��Channel�л�ȡpipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // ��HttpServerCodec���������뵽pipeline�����
        // HttpServerCodec��ʲô����HttpRequestDecoder��HttpResponseEncoder�ĸ�����
        // HttpRequestDecoder��http�������������Channel�е�ByteBuf���ݽ���ΪHttpRequest����
        // HttpResponseEncoder��http��Ӧ����������HttpResponse�������Ϊ��Ҫ��Channel�з��͵�ByteBuf����
        pipeline.addLast(new HttpServerCodec());
        // �����ٶ���Ĵ��������뵽Pipeline�����
        pipeline.addLast(new SomeServerHandler());
    }
}
