package com.learn.dubbo.registry;

/**
 * @author yujiaqi
 * @date 2020-06-04 10:25
 * @description
 */
public class RegistryTest {
    public static void main(String[] args) throws Exception {
        RegistryCenter registryCenter = new ZkRegistryCenter();
        registryCenter.register("com.learn.dubbo.provider.SomeService", "localhost:8888");
        System.in.read();
    }
}
