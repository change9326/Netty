package com.abc.rpc.discovery;

import com.abc.rpc.ZKConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class ServiceDiscoveryImpl implements ServiceDiscovery {
    private CuratorFramework curator;
    private List<String> servers;

    public ServiceDiscoveryImpl() {
        this.curator = CuratorFrameworkFactory.builder()
                .connectString(ZKConstant.ZK_CLUSTER)
                .connectionTimeoutMs(10000)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        curator.start();
    }

    @Override
    public String discovery(String serviceName) throws Exception {
        String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
        servers = curator.getChildren().forPath(servicePath);
        if(servers.size() == 0) {
            return null;
        }
        // 添加watcher监听，监听子节点列表变化
        registerWatcher(servicePath);
        return new RandomLoadBalance().choose(servers);
    }

    private void registerWatcher(String servicePath) throws Exception {
        // 其会将指定路径下的节点数据内容及状态缓存到本地
        PathChildrenCache cache = new PathChildrenCache(curator, servicePath, true);

        // 一旦监听到子节点列表发生变化，马上更新当前的servers集合
        cache.getListenable().addListener((client, event) -> {
            servers = curator.getChildren().forPath(servicePath);
        });

        // 启动监听
        cache.start();
    }
}
