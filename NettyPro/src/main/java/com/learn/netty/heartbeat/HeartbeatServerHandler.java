package com.learn.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author yujiaqi
 * @date 2020-06-01 10:39
 * @description
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if(evt instanceof IdleStateEvent){
           IdleStateEvent event = (IdleStateEvent) evt;
           String eventType = null;
           switch (event.state()){
               case READER_IDLE:
                   eventType = "读空闲";
                   break;
               case WRITER_IDLE:
                   eventType = "写空闲";
                   break;
               case ALL_IDLE:
                   eventType = "读写空闲";
                   break;
           }
           System.out.println(ctx.channel().remoteAddress() + "--超时时间--" + eventType);
           System.out.println("服务器做相应处理..");
       }
    }
}
