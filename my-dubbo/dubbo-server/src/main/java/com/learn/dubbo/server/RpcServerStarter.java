package com.learn.dubbo.server;

import com.learn.dubbo.registry.ZkRegistryCenter;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:26
 * @description
 */
public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        ZkRegistryCenter registryCenter = new ZkRegistryCenter();
        String serverAddress="127.0.0.1:8888";
        String providerPackage="com.learn.dubbo.provider";

        RpcServer server = new RpcServer();
        server.publish(registryCenter,serverAddress,providerPackage);
        // 启动服务器
        server.start();
    }
}
