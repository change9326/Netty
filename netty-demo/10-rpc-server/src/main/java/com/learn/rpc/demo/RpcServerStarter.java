package com.learn.rpc.demo;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcServer server = new RpcServer();
        // 将指定包下的提供者发布到服务器
        server.publish("com.abc.rpc.service");
        // 启动服务器
        server.start();
    }
}