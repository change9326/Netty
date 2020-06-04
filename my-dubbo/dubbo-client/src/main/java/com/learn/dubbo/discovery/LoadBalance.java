package com.learn.dubbo.discovery;

import java.util.List;

/**
 * @author yujiaqi
 * @date 2020-06-04 10:54
 * @description:定义负载均衡接口
 */
public interface LoadBalance {
    /**
     * 从servers中通过负载均衡策略选择一个主机地址
     * @param servers
     * @return
     */
    String choose(List<String> servers);
}
