package com.learn.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:26
 * @description
 */
public class RpcServer {

    /**
     * 用于存放指定包下所有接口实现类的类名
     */
    private List<String> classCache=new ArrayList<>();

    /**
     * 注册中心(注册表)
     * key 为服务名称，即业务接口名
     * value 为对应接口实现类的实例
     */
    private Map<String,Object> registryMap=new HashMap<>();

    /**
     * 服务发布：将服务名称与对应包中实现类的实例的映射关系写入到注册中心的过程
     * @param providerPackage
     */
    public void publish(String providerPackage) throws Exception {

        // 将指定包下的所有实现类名称写入到classCache集合
        getProviderClass(providerPackage);
        // 将服务提供者注册到注册表
        doRegister();
    }

    /**
     * 将服务提供者注册到注册表
     */
    private void doRegister() throws Exception {
        // 若没有提供者，则无需注册
        if(classCache.isEmpty()){return;}
        // 遍历classCache，获取到实现类所实现的接口名称，及创建该实现类对应实例
        for(String className : classCache){
            // 加载当前遍历的类
            Class<?> clazz = Class.forName(className);
            registryMap.put(clazz.getInterfaces()[0].getName(),clazz.newInstance());
        }
    }

    /**
     * 将指定包下的所有实现类名称写入到classCache集合
     * @param providerPackage
     */
    private void getProviderClass(String providerPackage) {

        // 将包转化为对象资源(将com.abc.rpc.service  -> \com\abc\rpc\service)
        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));
        // 将URL对象资源转化为File
        File dir = new File(resource.getFile());
        // 遍历dir目录中的所有文件，查找.class文件
        for(File file: dir.listFiles()){
            if(file.isDirectory()){
                // 若当前为目录，则递归
                getProviderClass(providerPackage+"."+file.getName());
            }else {
                // 将文件名中的.class扩展名去掉，即获取到简单类名
                String fileName = file.getName().replace(".class", "").trim();
                // 将实现类的全限定类名写入到classCache 中
                classCache.add(providerPackage+"."+fileName);
            }
        }
        System.out.printf("classCache = " + classCache);
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // ClassResolvers.cacheDisabled(null)的作用是将客户端发送的调用信息类加载到内存
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new RpcServerHandler(registryMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器已启动");
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
