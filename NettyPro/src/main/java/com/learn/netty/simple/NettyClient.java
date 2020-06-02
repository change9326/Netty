package com.learn.netty.simple;

import com.learn.netty.simple.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yujiaqi
 * @date 2020-05-28 10:50
 * @description
 */
public class NettyClient {

    public static void main(String[] args) {

        /**
         * 客户端只需要一个事件循环组即可
         */
        EventLoopGroup group = new NioEventLoopGroup();

        System.out.println("客户端ok 了....");
        try {
            // 创建客户端启动对象Bootstrap【注意客户端使用的不是 ServerBootstrap 而是 Bootstrap】
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(group)
                    // 设置客户端通道的实现类
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 加入自己的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            // 启动客户端并连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
