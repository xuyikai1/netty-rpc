package entity;

import cn.hutool.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author: Xuyk
 * @Description: netty通信请求类
 * @Date: Created in 14:45 2019/3/20
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 8121053492427588997L;

    private long requestId;
    private Class<?> clazz;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] params;
    private String serviceName;
    private long requestTime;

    public Request(){}

    public Request(long requestId, Class<?> clazz, String method, Class<?>[] parameterTypes, Object[] params, String serviceName, long requestTime) {
        this.requestId = requestId;
        this.clazz = clazz;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.params = params;
        this.serviceName = serviceName;
        this.requestTime = requestTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String toJsonString(){
        return new JSONObject(this).toString();
    }

}
