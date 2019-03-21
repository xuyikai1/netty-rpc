package com.nettyserver.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import zookeeper.Curator;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:49 2019/3/20
 */
public class CuratorTest {

    @Test
    public void getNode() {
    }

    @Test
    public void createNode() {
    }

    @Test
    public void updateNode() {
    }

    @Test
    public void deleteNode() {
    }

    @Test
    public void getChildNodes() {
        Curator curator = new Curator();
        List<String> list = curator.getChildNodes("/super");
        System.out.println(list);
    }

    @Test
    public void isExist() {
    }

    @Test
    public void main() {
    }

    @Test
    public void Watcher() throws Exception {
        /*Curator curator = new Curator();
        curator.createNode();
        // NodeCache: 缓存节点，并且可以监听数据节点的变更，会触发事件
        final NodeCache nodeCache = new NodeCache(curator.client, "/super/testNode");

        // 参数 buildInitial : 初始化的时候获取node的值并且缓存
        nodeCache.start(true);

        // 获取缓存里的节点初始化数据
        if (nodeCache.getCurrentData() != null) {
            System.out.println("节点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
        } else {
            System.out.println("节点初始化数据为空...");
        }

        // 为缓存的节点添加watcher，或者说添加监听器
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            // 节点数据change事件的通知方法
            public void nodeChanged() throws Exception {
                // 防止节点被删除时发生错误
                if (nodeCache.getCurrentData() == null) {
                    System.out.println("获取节点数据异常，无法获取当前缓存的节点数据，可能该节点已被删除");
                    return;
                }
                // 获取节点最新的数据
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println(nodeCache.getCurrentData().getPath() + " 节点的数据发生变化，最新的数据为：" + data);
            }
        });*/

    }
}