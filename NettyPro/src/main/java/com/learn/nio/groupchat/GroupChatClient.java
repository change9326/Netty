package com.learn.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author yujiaqi
 * @date 2020-05-21 10:31
 * @description
 */
public class GroupChatClient {

    /**
     * 定义属性
     */
    private static final String SERVER_HOST="127.0.0.1";

    private static final int SERVER_PORT=6667;

    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;


    public GroupChatClient() {
        try {
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_HOST,SERVER_PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);

            // 将该serverSocketChannel 注册到selector 关注的事件为OP_READ
            socketChannel.register(selector, SelectionKey.OP_READ);

            //得到userName
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送消息
     */
    public void sendInfo(String info){

        info = userName + " 说：" + info;
        ByteBuffer byteBuffer = ByteBuffer.wrap(info.getBytes());
        try {
            // 将buffer 数据写到channel
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取从服务器端回复的消息
     */
    public void readInfo(){

        try {
            int select = selector.select();
            // 有可用的通道
            if(select>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        // 得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();

                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 将channel 数据读取到byteBuffer
                        int read = sc.read(byteBuffer);
                        if(read>0){
                            // 如果有数据读到
                            String msg = new String(byteBuffer.array());
                            System.out.println(msg.trim());
                        }
                    }
                    // 删除当前的selectionKey, 防止重复操作
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //启动我们客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程, 每个3秒，读取从服务器发送数据
        new Thread(() -> {
            while (true){
                chatClient.readInfo();
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
