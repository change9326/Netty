package com.learn.nio;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yujiaqi
 * @date 2020-05-19 18:20
 * @description
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws IOException {

        // 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream("/Users/jiaqiyu/learn/Github/Netty/file01.txt");
        // 通过fileInputStream 获取对应的FileChannel -> 实际类型  FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将 通道的数据读入到Buffer
        fileChannel.read(byteBuffer);
        // 将byteBuffer 的 字节数据 转成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();

    }
}
