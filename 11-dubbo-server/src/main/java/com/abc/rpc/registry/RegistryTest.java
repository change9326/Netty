package com.abc.rpc.registry;

public class RegistryTest {
    public static void main(String[] args) throws Exception {
        RegistryCenter registryCenter = new ZKRegistryCenter();
        registryCenter.register("com.abc.rpc.service.SomeService", "localhost:8888");
        System.in.read();
    }
}
