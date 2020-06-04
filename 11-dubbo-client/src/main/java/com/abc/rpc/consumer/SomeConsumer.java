package com.abc.rpc.consumer;

import com.abc.rpc.client.RpcProxy;
import com.abc.rpc.service.SomeService;

public class SomeConsumer {
    public static void main(String[] args) {
        SomeService service = RpcProxy.create(SomeService.class);
        if(service != null) {
            System.out.println(service.hello("my-dubbo-2020"));
            System.out.println(service.hashCode());
        }
    }
}
