package com.learn.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yujiaqi
 * @date 2020-05-19 18:06
 * @description
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {

        String str="hello,netty";
        // 创建一个输出流到channel
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/jiaqiyu/learn/Github/Netty/file01.txt");

        // 通过 fileOutputStream 获取 对应的 FileChannel
        // 这个 fileChannel 真实 类型是  FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建已被缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str 放入到ByteBuffer
        byteBuffer.put(str.getBytes());

        //对byteBuffer 进行flip
        byteBuffer.flip();

        //将byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
