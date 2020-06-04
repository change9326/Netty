package com.learn.dubbo.client;

import com.learn.dubbo.discovery.ServiceDiscoveryImpl;
import com.learn.rpc.bean.InvokeMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yujiaqi
 * @date 2020-06-03 13:56
 * @description：在客户端创建一个动态代理类，用于动态代理服务端的提供者对象。
 */
public class RpcProxy {
    /**
     * 范型方法
     */
    public static <T> T create(Class<?> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 若调用的是Object中的方法，则直接进行本地调用
                        if(Object.class.equals(method.getDeclaringClass())) {
                            return method.invoke(this, args);
                        }
                        // 进行远程调用
                        return rpcInvoke(clazz, method, args);
                    }
                });
    }

    private static Object rpcInvoke(Class<?> clazz, Method method, Object[] args) throws Exception {

        RpcClientHandler handler = new RpcClientHandler();
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    // Nagle算法
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(handler);
                        }
                    });

            ServiceDiscoveryImpl serviceDiscovery = new ServiceDiscoveryImpl();
            String serverAddress = serviceDiscovery.discovery(clazz.getName());
            // 若zk 中不存在该服务，则返回null
            if(serverAddress == null){
                return null;
            }
            String ip = serverAddress.split(":")[0];
            String portStr = serverAddress.split(":")[1];

            ChannelFuture future = bootstrap.connect(ip, Integer.valueOf(portStr)).sync();

            // 创建并初始化调用信息
            InvokeMessage message = new InvokeMessage();
            message.setClassName(clazz.getName());
            message.setMethodName(method.getName());
            message.setParamTypes(method.getParameterTypes());
            message.setParamValues(args);
            // 将调用信息发送给Server
            future.channel().writeAndFlush(message).sync();
            future.channel().closeFuture().sync();
        } finally {
            loopGroup.shutdownGracefully();
        }
        return handler.getResult();
    }
}
