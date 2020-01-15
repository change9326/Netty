package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author yujiaqi
 * @date
 * @description
 */
public class SomeServer {

    public static void main(String[] args) {
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(parentGroup,childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // StringDecoder���ַ�������������Channel�е�ByteBuf���ݽ���ΪString
                        pipeline.addLast(new StringDecoder());
                        // StringEncoder���ַ�������������String����Ϊ��Ҫ���͵�Channel�е�ByteBuf
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new SomeServerHandler());
                    }
                });
        try {
            ChannelFuture future=bootstrap.bind(8888).sync();
            System.out.println("�������Ѿ�����");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
