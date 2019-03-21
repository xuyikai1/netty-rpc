package zookeeper;

import common.Constant;
import entity.ZookeeperNode;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import util.NetUtil;

import java.util.*;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 15:13 2019/3/18
 */
public class Curator {

    private static final String localIp = NetUtil.getLocalIp();

    /** Curator客户端 */
    private static CuratorFramework client;

    /** node路径 */
    private String nodePath;

    /** 服务路径 */
    private String servicePath;

    /** 注册服务名 */
    private String serviceName;

    /** 支持服务批量注册 对应参数Map */
    private Map<String, ZookeeperNode> servers = new HashMap<>();

    /** 集群ips */
    private String ips;

    /** 本地ip */
    private String ip;

    /** 本地端口号 */
    private int port;


    private static Curator curator = new Curator();

    public Curator() {
        //使用Constant里的默认zk的ip集群(ZK_IPS)连接上zk
        this.ips = Constant.ZK_IPS;
        connect(Constant.ZK_IPS);
        this.nodePath =  Constant.ZOOKEEPER_SERVER_PATH;
        this.servicePath = "/serverSpace/" + Constant.ZOOKEEPER_SERVER_PATH;
        connect(ips);
    }

    public Curator(String serviceName, String ip, int port, String ips) {
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
        this.ips = ips;
        this.nodePath = Constant.ZOOKEEPER_SERVER_PATH + "/" + serviceName + "/" + ip + ":" + port;
        this.servicePath = "/" + Constant.ZOOKEEPER_SERVER_PATH + "/" + serviceName;
        connect(ips);
    }

    public Curator(Map<String,ZookeeperNode> servers, String ip, int port, String ips) {
        this.servers = servers;
        this.ip = ip;
        this.port = port;
        this.ips = ips;
        connect(ips);
    }

    /**
     * 传入zk集群ip 连接zk 例：192.168.254.133:2181,192.168.254.134:2181
     * @param ips
     */
    public void connect(String ips){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        // 1.使用工厂类来建造客户端的实例对象2.放入zookeeper服务器ip3.设定会话时间以及重连策略4.设置命名空间以及开始建立连接
        client = CuratorFrameworkFactory.builder()
                .connectString(ips)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("serverSpace").build();
        client.start();
    }

    /**
     * 注册服务(创建临时节点)、监听服务变化 单个注册/批量注册
     */
    public void registerService(){
        if(servers.size() > 0){
            //TODO 批量注册服务待完善
        }
        //创建节点 路径:/serverSpace/服务名/ip+端口
        ZookeeperNode node = curator.createNode(nodePath);
        System.out.println(node);
    }

    /**
     * 获取zookeeper节点
     * @param nodePath
     * @return
     */
    public ZookeeperNode getNode(String nodePath){
        byte[] nodeData = null;
        Stat stat = new Stat();
        try{
            nodeData = client.getData().storingStatIn(stat).forPath(nodePath);
            System.out.println("查询节点成功...");
        }catch (Exception e){
            System.out.println("查询节点失败s...");
            e.printStackTrace();
        }
        return new ZookeeperNode(nodePath,nodeData,stat);
    }

    /**
     * 根据自定义结点路径递归创建节点
     */
    public ZookeeperNode createNode(String nodePath){
        ZookeeperNode node = new ZookeeperNode();
        try{
            // 创建节点1.节点数据 2.创建父节点，也就是会递归创建 3.节点类型 4.节点的acl权限
            String result = client.create().creatingParentsIfNeeded()
                    //创建临时节点、服务器关闭节点失效
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(nodePath);
            //获取到创建好的节点
            node = getNode(nodePath);

            System.out.println("result: " + result);
            System.out.println("节点创建成功...");
        }catch (Exception e){
            System.out.println("节点创建失败...");
            e.printStackTrace();
        }
        return node;
    }

    /**
     * 修改节点
     * @param newData
     * @param node
     * @return
     */
    public ZookeeperNode updateNode(String newData,ZookeeperNode node){
        boolean isExist = isExist(node.getNodePath());
        if(!isExist){
            System.out.println("不存在该节点");
            return null;
        }
        Stat stat = node.getStat();
        String nodePath = node.getNodePath();
        try{
            // 1.指定数据版本 2.需要修改的节点路径以及新数据
            byte[] bytes = newData.getBytes();
            stat = client.setData().withVersion(stat.getVersion())
                    .forPath(nodePath, bytes);
            node.setStat(stat);

            System.out.println("节点修改成功...");
        }catch (Exception e){
            System.out.println("节点修改失败...");
            e.printStackTrace();
        }
        return node;
    }

    /**
     * 删除节点
     * @param nodePath
     */
    public void deleteNode(String nodePath) {
        try{
            Stat statExist = client.checkExists().forPath(nodePath);
            if(statExist == null){
                System.out.println("不存在该节点");
                return ;
            }
            //1.如果删除失败，那么在后端还是会继续删除，直到成功 2.子节点也一并删除，也就是会递归删除
            client.delete()
                    .guaranteed()
                    .deletingChildrenIfNeeded()
                    .withVersion(statExist.getVersion())
                    .forPath(nodePath);
            System.out.println("节点删除成功...");
        }catch (Exception e){
            System.out.println("节点删除失败...");
            e.printStackTrace();
        }
    }

    /**
     * 获取指定节点路径的子节点列表
     * @param nodePath
     * @return
     */
    public List<String> getChildNodes(String nodePath){
        List<String> childNodes = new ArrayList<>();
        try{
            childNodes = client.getChildren().forPath(nodePath);
            System.out.println(nodePath + " 节点下的子节点列表：");
            for (String childNode : childNodes) {
                System.out.println(childNode);
            }
            System.out.println("获取子节点列表成功...");
        }catch (Exception e){
            System.out.println("获取子节点列表失败...");
            e.printStackTrace();
        }
        return childNodes;
    }


    /**
     * curator之nodeCache一次注册N次监听
     * @param nodePath
     */
    public void WatcheNode(String nodePath){

        System.out.println("进入watcher绑定...");
        try{

            // 为子节点添加watcher
            // PathChildrenCache: 监听数据节点的增删改，可以设置触发的事件
            final PathChildrenCache childrenCache = new PathChildrenCache(client, nodePath, true);

            /**
             * StartMode: 初始化方式
             * POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件
             * NORMAL：异步初始化
             * BUILD_INITIAL_CACHE：同步初始化
             */
            childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

            // 列出子节点数据列表，需要使用BUILD_INITIAL_CACHE同步初始化模式才能获得，异步是获取不到的
            List<ChildData> childDataList = childrenCache.getCurrentData();
            System.out.println("当前节点的子节点详细数据列表：");
            for (ChildData childData : childDataList) {
                System.out.println("\t* 子节点路径：" + childData.getPath() +
                        "，该节点的数据为：" + new String(childData.getData()) +
                    "该节点的路径为：" + childData.getPath());
            }

            // 添加事件监听器
            childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                    // 通过判断event type的方式来实现不同事件的触发
                    // 子节点初始化时触发
                    if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                        System.out.println("\n--------------\n");
                        System.out.println("子节点初始化成功");
                    }
                    // 添加子节点时触发
                    else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                        System.out.println("\n--------------\n");
                        System.out.print("子节点：" + event.getData().getPath() + " 添加成功，");
                        System.out.println("该子节点的数据为：" + new String(event.getData().getData()));
                    }
                    // 删除子节点时触发
                    else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                        System.out.println("\n--------------\n");
                        System.out.println("子节点：" + event.getData().getPath() + " 删除成功");
                    }
                    // 修改子节点数据时触发
                    else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                        System.out.println("\n--------------\n");
                        System.out.print("子节点：" + event.getData().getPath() + " 数据更新成功，");
                        System.out.println("子节点：" + event.getData().getPath() + " 新的数据为：" + new String(event.getData().getData()));
                    }
                }
            });

        }catch (Exception e){
            System.out.println("注册n次监听失败...");
            e.printStackTrace();
        }


    }

    /** 关闭zk客户端连接 */
    public void close() {
        if (client != null) {
            client.close();
        }
    }


    public boolean isExist(String nodePath){
        ZookeeperNode node = getNode(nodePath);
        return node == null ? false : true;
    }

    public static void main(String[] args) throws Exception {

        String ips = "192.168.254.133:2181,192.168.254.134:2181,192.168.254.135:2181";

        Curator curator = new Curator();

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ips)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("serverSpace").build();
        client.start();

        // 获取当前客户端的状态
        boolean isZkCuratorStarted = client.isStarted();
        System.out.println("当前客户端的状态：" + (isZkCuratorStarted ? "连接中..." : "已关闭..."));

        // 节点路径
        String nodePath = "/super/testNode";

        // 创建节点1.节点数据 2.创建父节点，也就是会递归创建 3.节点类型 4.节点的acl权限
        ZookeeperNode createNode = curator.createNode(nodePath);
        System.out.println(createNode.toString());

        // 更新节点数据
        ZookeeperNode updateNode = curator.updateNode("new Data",createNode);

        System.out.println("更新节点数据成功，新的数据版本为：" + updateNode.getStat().getVersion());

        ZookeeperNode node = curator.getNode(nodePath);
        System.out.println("查询版本号："+node.getStat().getVersion());
        System.out.println("查询数据："+node.getContent());

        curator.deleteNode(nodePath);

        Thread.sleep(1000);

        // 关闭客户端
        curator.close();

        // 获取当前客户端的状态
        isZkCuratorStarted = client.isStarted();
        System.out.println("当前客户端的状态：" + (isZkCuratorStarted ? "连接中..." : "已关闭..."));
    }

    public String getServicePath() {
        return servicePath;
    }
}
