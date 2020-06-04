package com.learn.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yujiaqi
 * @date 2020-06-03 13:56
 * @description
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

    private Object result;

    public Object getResult() {
        return result;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result=msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
