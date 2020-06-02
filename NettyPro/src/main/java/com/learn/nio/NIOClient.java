package com.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author yujiaqi
 * @date 2020-05-20 11:39
 * @description
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 得到一个网路通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 设置服务端的ip:port
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if(!socketChannel.connect(socketAddress)){
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
            }
        }
        //...如果连接成功，就发送数据
        String str = "hello,netty~";
        // Wraps a byte array into a buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes("UTF-8"));
        // 发送数据，将 buffer 数据写入 channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
