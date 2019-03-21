package entity;

/**
 * @Author: Xuyk
 * @Description: netty通信返回类
 * @Date: Created in 14:46 2019/3/20
 */
public class Response {

    private long requestId;
    private Object response;
    private Throwable throwable;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
