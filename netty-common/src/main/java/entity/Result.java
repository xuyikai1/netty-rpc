package entity;


import cn.hutool.json.JSONObject;

/**
 * @author: Xuyk @Time: 2018/12/25 15:36 @Description: 返回对象dto
 */
public class Result<T> {

    private boolean success;
    private String msg;
    private T data;

    public Result(){}

    public Result(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
