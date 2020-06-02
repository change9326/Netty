package com.learn.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author yujiaqi
 * @date 2020-05-28 14:06
 * @description
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 * HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>{

    private static final String ICO="/favicon.ico";

    /**
     * 读取客户端数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println("对应的channel= "+ctx.channel()+" pipeline="+ctx.pipeline()+"通过pipeline 获取channel"+ctx.pipeline().channel());

        System.out.println("当前ctx 的handler="+ ctx.handler());

        if(msg instanceof HttpRequest){
            System.out.println("ctx 类型="+ctx.getClass());

            System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + " TestHttpServerHandler hash=" + this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            // 获取uri, 过滤指定的资源
            URI uri = new URI(httpRequest.uri()) ;
            if(ICO.equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }
            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
            // 构造一个http 请求即HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好 response返回
            ctx.writeAndFlush(response);

        }
    }
}
