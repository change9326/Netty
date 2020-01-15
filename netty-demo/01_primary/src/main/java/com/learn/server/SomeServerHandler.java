package com.learn.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author yujiaqi
 * @date 2020-01-13 08:53
 * @description:�Զ������˴�����
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *  ��Channel���������ڿͻ��˵�����ʱ�ͻᴥ���÷�����ִ��
     * @param ctx  �����Ķ���
     * @param msg   ���������ڿͻ��˵�����
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("����ʽ��" + request.method().name());
            System.out.println("����URI��" + request.uri());

            if("/favicon.ico".equals(request.uri())) {
                System.out.println("������/favicon.ico����");
                return;
            }

            // ����response����Ӧ��
            ByteBuf body = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // ������Ӧ����
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
            // ��ȡ��response��ͷ������г�ʼ��
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

            // ����Ӧ����д�뵽Channel
            // ctx.write(response);
            // ctx.flush();
            // ctx.writeAndFlush(response);
            ctx.writeAndFlush(response)
                    // ��Ӽ���������Ӧ�巢�������ֱ�ӽ�Channel�ر�
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     *  ��Channel�е������ڴ�������г����쳣ʱ�ᴥ���÷�����ִ��
     * @param ctx  ������
     * @param cause  �������쳣����
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // �ر�Chanel
        ctx.close();
    }
}
