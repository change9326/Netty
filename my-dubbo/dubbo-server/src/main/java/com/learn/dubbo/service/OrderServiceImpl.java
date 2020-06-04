package com.learn.dubbo.service;

/**
 * @author yujiaqi
 * @date 2020-06-04 14:19
 * @description
 */
public class OrderServiceImpl implements OrderService{

    @Override
    public String createOrder(Long productId) {
        return "商品ID=["+productId+"],成功创建订单！";
    }
}
