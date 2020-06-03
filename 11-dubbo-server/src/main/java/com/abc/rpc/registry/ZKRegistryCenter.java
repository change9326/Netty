package com.abc.rpc.registry;

import com.abc.rpc.ZKConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZKRegistryCenter implements RegistryCenter {
    // 声明zk客户端
    private CuratorFramework curator;

    // 实例代码块
    {
        curator = CuratorFrameworkFactory.builder()
                .connectString(ZKConstant.ZK_CLUSTER)
                // 连接超时时间，默认15秒
                .connectionTimeoutMs(10000)
                // 会话超时时间，60秒
                .sessionTimeoutMs(4000)
                // 设置重试机制：每1秒重试一次，最多重试10次
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        // 启动zk客户端
        curator.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
        // 要创建类似于/dubbocustom/com.abc.rpc.service.SomeService节点
        String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;

        // 若节点不存在，则创建
        if(curator.checkExists().forPath(servicePath) == null) {
            // 创建服务名称的持久节点
            curator.create()
                    // 若父节点不存在，则创建父节点
                    .creatingParentsIfNeeded()
                    // 创建持久节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(servicePath, "0".getBytes());
        }

        // 要创建类似于/dubbocustom/com.abc.rpc.service.SomeService/192.168.59.121:8081节点
        String addressPath = servicePath + "/" + serviceAddress;

        // 创建ip+port的临时节点
        String nodeName = curator.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(addressPath, "0".getBytes());
        System.out.println("提供者主机节点创建成功：" + nodeName);

    }
}
