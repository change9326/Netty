package com.learn.dubbo.service;

/**
 * @author yujiaqi
 * @date 2020-06-03 11:19
 * @description:业务接口实现类
 */

public class SomeServiceImpl implements SomeService{
    @Override
    public String hello(String name) {
        return "Hello," + name;
    }
}
