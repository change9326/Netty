package com.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yujiaqi
 * @date 2020-05-20 11:04
 * @description
 */
public class NIOServer {


    public static void main(String[] args) throws IOException {

        // 创建serverSocketChannel->socketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 创建selector 对象
        Selector selector = Selector.open();

        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把ServerSocketChannel 注册到selector,关心的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的SelectionKey 的数量="+selector.keys().size());

        // 循环等待客户端的连接
        while (true){
            if(selector.select(1000)==0){
                // 没有事件发生
                continue;
            }

            /**
             * 如果返回值>0,就获取到相关的selectionKey集合;表示已经获取到关注的事件
             * selector.selectedKeys() 返回关注事件的集合
             * 通过 selectionKeys 反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历 Set<SelectionKey>, 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                // 获取到SelectionKey
                SelectionKey key = keyIterator.next();
                // 根据key 对应的通道发生的事件做相应处理

                if(key.isAcceptable()){
                    // 如果是 OP_ACCEPT, 有新的客户端连接

                    // 为该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个 socketChannel " + socketChannel.hashCode());
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将socketChannel 注册到selector;关注事件为 OP_READ， 同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size());


                }else if(key.isReadable()){
                    // 发生 OP_READ

                    //通过key 反向获取到对应channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();

                    //获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

                    // 将channel 数据读到buffer
                    socketChannel.read(byteBuffer);
                    System.out.println("form 客户端 " + new String(byteBuffer.array()));

                }
                //手动从集合中移动当前的selectionKey, 防止重复操作
                keyIterator.remove();
            }

        }
    }
}
