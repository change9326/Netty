package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author yujiaqi
 * @date
 * @description
 * SimpleChannelInboundHandler��
 *   ���Զ��ͷŽ��յ��������ڶԷ���msg ��ռ�õ�������Դ
 */
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {
    /**
     * msg����Ϣ���������еķ���������һ�µ�
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "��" + msg);
        ctx.channel().writeAndFlush("from client��" + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    /**
     * ��Channel�������ᴥ���÷�����ִ��
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("from client��begin talking");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
