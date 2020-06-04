package com.learn.dubbo.service;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:16
 * @description
 */
public interface OrderService {
    /**
     * 创建订单
     */
    String createOrder(Long productId);
}