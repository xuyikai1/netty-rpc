package common;

/**
 * @Author: Xuyk
 * @Description: 常量类
 * @Date: Created in 17:13 2019/3/14
 */
public class Constant {

    public static final String THREAD_POOL_NAME = "client-threadPool";
    /** 设置服务器的两倍cpu数量 **/
    public static final Integer ORDER_MAX_CORE_POOL_SIZE = 8;
    public static final Integer THREAD_POOL_SIZE = 8;

    /** 设置zookeeper的服务注册统一初始路径 **/
    public static final String ZOOKEEPER_SERVER_PATH = "/services";

    public static final String SERVER_SERVICE_PATH = "/192.168.254.1:8765/services";

    /** 集群模式则是多个ip */
    public static final String ZK_IPS = "192.168.254.133:2181,192.168.254.134:2181,192.168.254.135:2181";

    /** 存储服务端本地ip 用于客户端连接*/
    public static final String SERVER_IP = "192.168.254.1";

    /** 存储服务端本地ip和端口 用于服务端连接*/
    public static final String IP_PORT = "/192.168.254.1:8765";

    /** 设置服务端端口号*/
    public static final int PORT = 8765;

    /** 读超时 **/
    public static final Integer READ_IDEL_TIME_OUT = 1;
    /** 写超时 **/
    public static final Integer WRITE_IDEL_TIME_OUT = 1;
    /** 所有超时 **/
    public static final Integer ALL_IDEL_TIME_OUT = 1;

    public static final Integer requestTimeoutMillis = 100;

}
