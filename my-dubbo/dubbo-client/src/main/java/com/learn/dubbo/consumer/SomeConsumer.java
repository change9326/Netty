package com.learn.dubbo.consumer;


import com.learn.dubbo.client.RpcProxy;
import com.learn.dubbo.service.SomeService;

/**
 * @author yujiaqi
 * @date 2020-06-03 14:14
 * @description
 */
public class SomeConsumer {

    public static void main(String[] args) {

        SomeService service = RpcProxy.create(SomeService.class);
        if(service != null) {
            System.out.println(service.hello("易点租"));
        }

    }
}
