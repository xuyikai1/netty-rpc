package serializer;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:47 2019/3/20
 */
public interface Serializer {

    byte[] serialize(Object obj);
    <T> T deserialize(byte[] bytes);
}
