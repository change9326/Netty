package com.learn.dubbo.registry;

import com.learn.dubbo.ZkConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author yujiaqi
 * @date 2020-06-03 20:05
 * @description
 */
public class ZkRegistryCenter implements RegistryCenter{
    /**
     * 声明zk 客户端
     */
   private  CuratorFramework curator;
    // 实例代码块
    {
       curator=CuratorFrameworkFactory.builder()
               .connectString(ZkConstant.ZK_CLUSTER)
               // 设置连接超时时间,默认为15s
               .connectionTimeoutMs(10000)
               // 会话超时时间，60s
               .sessionTimeoutMs(4000)
               // 设置重试机制：每1秒重试一次，最多重试10次
               .retryPolicy(new ExponentialBackoffRetry(1000,10))
               .build();
       curator.start();
   }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
        // 要创建类似于/dubbocustom/com.learn.dubbo.service.SomeService 的节点
        String servicePath = ZkConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
        // 节点不存在则创建
        if(curator.checkExists().forPath(servicePath)==null){
            // 创建服务名称的持久节点
            curator.create()
                    // 若父节点不存在，则创建父节点
                    .creatingParentsIfNeeded()
                    // 创建持久节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(servicePath,"0".getBytes());
        }
        // 要创建类似于/dubbocustom/com.learn.dubbo.service.SomeService/localhost:8888
        String addressPath = servicePath + "/" + serviceAddress;

        // 创建ip+port 的临时节点
        String nodeName = curator.create().withMode(CreateMode.EPHEMERAL)
                .forPath(addressPath, "0".getBytes());
        System.out.println("提供者主机节点创建成功：" + nodeName);
    }
}
