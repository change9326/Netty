package com.abc.rpc.discovery;

import java.util.List;

public interface LoadBalance {
    /**
     *  从servers中通过负载均衡策略选择一个主机地址
     * @param servers
     * @return
     */
    String choose(List<String> servers);
}
