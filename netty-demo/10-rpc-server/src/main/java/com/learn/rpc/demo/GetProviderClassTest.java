package com.learn.rpc.demo;

public class GetProviderClassTest {
    public static void main(String[] args) {
        new RpcServer().getProviderClass("com.abc.rpc.service");
    }
}
