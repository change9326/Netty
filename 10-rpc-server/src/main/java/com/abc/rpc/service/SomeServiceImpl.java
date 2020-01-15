package com.abc.rpc.service;

public class SomeServiceImpl implements SomeService {
    @Override
    public String hello(String name) {
        return "Hello," + name;
    }
}
