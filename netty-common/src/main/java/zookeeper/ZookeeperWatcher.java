package zookeeper;


import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:56 2019/3/20
 */
public class ZookeeperWatcher implements CuratorWatcher {

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        System.out.println("触发watcher，节点路径为：" + watchedEvent.getPath());
    }

}

