package com.learn.dubbo.discovery;

/**
 * @author yujiaqi
 * @date 2020-06-04 10:59
 * @description:服务发现
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称查找提供者主机地址
     * @param serviceName
     * @return  ip:port
     */
    String discovery(String serviceName) throws Exception;
}
