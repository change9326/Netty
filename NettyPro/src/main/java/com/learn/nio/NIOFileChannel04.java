package com.learn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author yujiaqi
 * @date 2020-05-19 19:51
 * @description
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("a.png");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("b.png");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        //使用transferForm完成拷贝
        fileChannel02.transferFrom(fileChannel01,0,fileChannel01.size());

        //关闭相关通道和流
        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
