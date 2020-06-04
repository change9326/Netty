package com.learn.dubbo.discovery;

import com.learn.dubbo.ZkConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author yujiaqi
 * @date 2020-06-04 11:00
 * @description
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery{

    private CuratorFramework curator;

    private List<String> servers;

    public ServiceDiscoveryImpl() {
        this.curator = CuratorFrameworkFactory.builder()
                      .connectString(ZkConstant.ZK_CLUSTER)
                      .connectionTimeoutMs(10000)
                      .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10))
                .build();
        curator.start();
    }

    @Override
    public String discovery(String serviceName) throws Exception {
        String servicePath = ZkConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
        servers = curator.getChildren().forPath(servicePath);
        if(servers.size() ==0){
            return null;
        }
        // 添加watcher监听，监听子节点列表变化
        registerWatcher(servicePath);
        // 负载均衡
        return new RandomLoadBalance().choose(servers);
    }

    /**
     * 添加watcher监听，监听子节点列表变化
     * @param servicePath
     */
    private void registerWatcher(String servicePath) throws Exception {
        // 将指定路径下的节点数据内容及状态缓存到本地
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator, servicePath, true);

        // 一旦监听到子节点列表发生变化，马上更新当前的servers集合
        pathChildrenCache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent)
                -> servers=curator.getChildren().forPath(servicePath));

        // 启动监听
        pathChildrenCache.start();
    }
}
