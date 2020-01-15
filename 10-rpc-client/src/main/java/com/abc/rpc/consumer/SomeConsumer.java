package com.abc.rpc.consumer;

import com.abc.rpc.client.RpcProxy;
import com.abc.rpc.service.SomeService;

public class SomeConsumer {
    public static void main(String[] args) {
        SomeService service = RpcProxy.create(SomeService.class);
        System.out.println(service.hello("Tom"));
        System.out.println(service.hashCode());
    }
}
