package com.learn.rpc.server;

import com.learn.rpc.bean.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:26
 * @description
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<InvokeMessage> {

    private Map<String, Object> registryMap;


    public RpcServerHandler(Map<String, Object> registryMap) {
         this.registryMap=registryMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvokeMessage msg) throws Exception {
         Object result="没有指定的提供者";
         // 判断注册表中是否有指定的服务
         if(registryMap.containsKey(msg.getClassName())){
             // 获取提供着实例
             Object privider = registryMap.get(msg.getClassName());
             // 进行方法调用
             result=privider.getClass().getMethod(msg.getMethodName(),msg.getParamTypes())
                     .invoke(privider,msg.getParamValues());
         }
         // 将调用结果发送给客户端
         ctx.writeAndFlush(result);
         ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
