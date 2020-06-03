package com.abc.rpc.discovery;

public interface ServiceDiscovery {
    /**
     * 根据服务名称查找提供者主机地址
     * @param serviceName
     * @return  ip:port
     */
    String discovery(String serviceName) throws Exception;
}
