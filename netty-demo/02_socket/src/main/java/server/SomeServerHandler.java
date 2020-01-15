package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yujiaqi
 * @date
 * @description
 * ChannelInboundHandlerAdapter��
 *   �����Զ��ͷŽ��յ������ԶԷ���msg ��ռ�õ�������Դ
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // �����Կͻ��˵�������ʾ�ڷ���˿���̨
        System.out.println(ctx.channel().remoteAddress()+","+msg);
        // ��ͻ��˷�������
        ctx.channel().writeAndFlush("from server: " + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
