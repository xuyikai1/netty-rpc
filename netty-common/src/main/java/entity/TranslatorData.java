package entity;

import java.io.Serializable;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:09 2019/3/14
 */
public class TranslatorData implements Serializable {

    private static final long serialVersionUID = -8101402485611305363L;

    private String id;
    private String name;
    private String message;
    private Object data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
