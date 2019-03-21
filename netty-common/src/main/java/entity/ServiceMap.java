package entity;

import java.util.List;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:19 2019/3/21
 */
public class ServiceMap {

    private String serviceName;

    private List<String> addresses;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
