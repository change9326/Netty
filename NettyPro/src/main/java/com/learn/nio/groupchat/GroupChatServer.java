package com.learn.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author yujiaqi
 * @date 2020-05-21 10:31
 * @description
 */
public class GroupChatServer {

    /**
     * 属性定义
     */
    private static final Integer PORT = 6667;
    private Selector selector;
    private ServerSocketChannel listenChannel;

    public  GroupChatServer(){
        try {
            selector=Selector.open();
            listenChannel=ServerSocketChannel.open();
            // 绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenChannel.configureBlocking(false);
            // 将listenChannel 注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间监听
     */
    public void eventLintener(){
            System.out.println("监听线程: " + Thread.currentThread().getName());
            try {
                while (true) {
                    int count = selector.select();
                    // 有事件监听到
                    if(count>0){

                        // 遍历得到selectionKey 集合
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()){
                            // 取出selectionkey
                            SelectionKey key = iterator.next();
                            //  监听到accept
                            if(key.isAcceptable()){
                                SocketChannel sc = listenChannel.accept();
                                // 设置非阻塞
                                sc.configureBlocking(false);
                                // 将该chanel 注册到selector
                                sc.register(selector,SelectionKey.OP_READ);
                                System.out.println(sc.getRemoteAddress() + " 上线");
                            }else if(key.isReadable()){
                                // 通道发送read事件，即通道是可读的状态
                                readData(key);
                            }
                            // 手动从集合中移动当前的selectionKey, 防止重复操作
                            iterator.remove();

                        }


                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }



    }

    private void readData(SelectionKey key) {

        SocketChannel channel=null;
        try {
            // 获取selectKey 关联的channel
            channel=(SocketChannel)key.channel();
            // 创建ByteBuffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 将channel 的数据读到buffer
            int read = channel.read(buffer);
            // 如果有数据读到
            if(read>0){
                // 把缓存区的数据转成字符串
                String msg = new String(buffer.array()).trim();
                // 输出该消息
                System.out.println("form 客户端: " + msg);

                //向其它的客户端转发消息(去掉自己)
                sendInfoToOtherClients(msg, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了..");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException e2) {
                e2.printStackTrace();;
            }
        }


    }

    /**
     * 转发消息给其它客户(通道)
     * @param msg
     * @param channel
     */
    private void sendInfoToOtherClients(String msg, SocketChannel channel) throws IOException {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
        //遍历 所有注册到selector 上的 SocketChannel,并排除 self
        for(SelectionKey key:selector.keys()){

            // 通过 key  取出对应的 SocketChannel
            SelectableChannel targetChannel = key.channel();
            // 排除自己
            if(targetChannel instanceof SocketChannel && targetChannel!=channel){
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将buffer 的数据写到channel
                dest.write(ByteBuffer.wrap(msg.getBytes()));
            }

        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.eventLintener();
    }
}
