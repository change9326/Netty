package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yujiaqi
 * @date
 * @description
 * ChannelInboundHandlerAdapter：
 *   不会自动释放接收到的来自对方的msg 所占用的所有资源
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将来自客户端的数据显示在服务端控制台
        System.out.println(ctx.channel().remoteAddress()+","+msg);
        // 向客户端发送数据
        ctx.channel().writeAndFlush("from server: " + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
