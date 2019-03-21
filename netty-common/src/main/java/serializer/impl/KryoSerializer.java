package serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: Xuyk
 * @Description: Kryo序列化
 * @Date: Created in 14:49 2019/3/20
 */
public class KryoSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo=new Kryo();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output,obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }
    @Override
    public <T> T deserialize(byte[] bytes) {
        Kryo kryo=new Kryo();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }
}
