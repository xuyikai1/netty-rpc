package entity;

import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

/**
 * @Author: Xuyk
 * @Description: zookeeper节点信息类：可作为缓存保存在服务端
 * @Date: Created in 10:09 2019/3/20
 */
public class ZookeeperNode {

    private String nodePath;
    private byte[] bytes;
    private Stat stat;
    private String content;

    public ZookeeperNode(){}

    public ZookeeperNode(String nodePath, byte[] bytes, Stat stat) {
        this.nodePath = nodePath;
        this.bytes = bytes;
        this.stat = stat;
        this.content = new String(bytes);
    }

    public ZookeeperNode(String nodePath, byte[] bytes, Stat stat, String content) {
        this.nodePath = nodePath;
        this.bytes = bytes;
        this.stat = stat;
        this.content = content;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ZookeeperNode{" +
                "nodePath='" + nodePath + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", stat=" + stat +
                ", content='" + content + '\'' +
                '}';
    }
}
