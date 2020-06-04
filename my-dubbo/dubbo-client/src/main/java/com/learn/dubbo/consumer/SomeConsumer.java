package com.learn.rpc.consumer;

import com.learn.rpc.client.RpcProxy;
import com.learn.rpc.service.SomeService;

/**
 * @author yujiaqi
 * @date 2020-06-03 14:14
 * @description
 */
public class SomeConsumer {

    public static void main(String[] args) {
        SomeService service  = RpcProxy.create(SomeService.class);
        System.out.println(service.hello("Tom~喵喵"));
        System.out.println(service.hashCode());
    }
}
