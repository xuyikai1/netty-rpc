package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 15:01 2019/3/20
 */
public class NetUtil {

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLocalIp());
    }

}
