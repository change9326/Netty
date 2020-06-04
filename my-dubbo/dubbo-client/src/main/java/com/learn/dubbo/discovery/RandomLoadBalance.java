package com.learn.dubbo.discovery;
import java.util.List;
import java.util.Random;

/**
 * @author yujiaqi
 * @date 2020-06-04 10:56
 * @description:随机负载均衡策略
 */
public class RandomLoadBalance implements LoadBalance{
    @Override
    public String choose(List<String> servers) {
        System.out.println("All Servers,"+ servers.toString());
        String chooseServer = servers.get(new Random().nextInt(servers.size()));
        System.out.println("Choose Servers,"+chooseServer);
        return chooseServer;
    }
}
