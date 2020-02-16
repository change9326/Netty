package com.learn.rpc.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import jdk.nashorn.internal.runtime.FindProperty;

import java.io.File;
import java.net.URL;
import java.util.*;

public class RpcServer {

    // 用于存放指定包下包所有接口实现类的类名
    private List<String> classCache = new ArrayList<>();
    // 注册中心（注册表）
    // key为服务名称，即业务接口名
    // value为对应接口实现类的实例
    private Map<String, Object> registryMap = new HashMap<>();

    // 将服务名称与对应包中实现类的实例的映射关系写入到注册中心的过程，
    // 称为服务发布
    public void publish(String providerPackage) throws Exception {
        // 将指定包下的所有实现类名称写入到classCache集合
        getProviderClass(providerPackage);
        // 将服务提供者注册到注册表
        doRegister();
    }

    // 将指定包下的所有实现类名称写入到classCache集合
    public void getProviderClass(String providerPackage) {
        // 将包转化为对象资源
        // 将com.abc.rpc.service  -> \com\abc\rpc\service
        URL resource = this.getClass().getClassLoader()
                .getResource(providerPackage.replaceAll("\\.", "/"));

        // 将URL对象资源转化为File
        File dir = new File(resource.getFile());
        // 遍历dir目录中的所有文件，查找.class文件
        for (File file : dir.listFiles()) {
            if(file.isDirectory()) {
                // 若当前文件为目录，则递归
                getProviderClass(providerPackage + "." + file.getName());
            } else {
                // 将文件名中的.class扩展名去掉，即获取到简单类名
                String fileName = file.getName().replace(".class", "").trim();
                // 将实现类的全限定性类名写入到classCache中
                classCache.add(providerPackage + "." + fileName);
            }
        }

        // System.out.println("classCache = " + classCache);
    }

    // 将服务提供者注册到注册表
    private void doRegister() throws Exception {
        // 若没有提供者，则无需注册
        if(classCache.size() == 0) return;

        // 遍历classCache，获取到实现类所实现的接口名称，及创建该实现类对应实例
        for (String ClassName : classCache) {
            // 加载当前遍历的类
            Class<?> clazz = Class.forName(ClassName);
            registryMap.put(clazz.getInterfaces()[0].getName(), clazz.newInstance());
        }
    }

    public void start() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // InvokeMessage
                            // ClassResolvers.cacheDisabled(null)的作用是将客户端发送的调用信息类加载到内存
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new RpcServerHandler(registryMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器已启动");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
