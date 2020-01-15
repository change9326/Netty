package com.learn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yujiaqi
 * @date
 * @description:������������
 */
public class HttpServer {

    public static void main(String[] args) {
        // ���ڴ���ͻ�����������,�������͸�childGroup �е�eventLoop
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        // ���ڴ���ͻ�������
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        // ��������ServerChannel
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                 // ָ��eventLoopGroup
                .group(parentGroup,childGroup)
                // ָ��ʹ��NIO ����ͨ��
                .channel(NioServerSocketChannel.class)
                // ָ��childGroup �е�eventLoop ���󶨵��߳���Ҫ����Ĵ�����
                .childHandler(new SomeChannelInitializer());
        try {
            // ָ����ǰ�������������Ķ˿ں�
            // bind() ������ִ�����첽��
            // sync() ������ʹbind()����������Ĵ����ִ�����첽��Ϊͬ����
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            System.out.println("�������Ѿ������������Ķ˿ں�Ϊ��8888");

            //  �ر�chanel
            // closeFuture() �Ĳ��������첽�ġ�
            // ��Channel������close()�������رճɹ���Żᴥ��closeFuture()������ִ��
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // ���Źر�
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
