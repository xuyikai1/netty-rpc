package serializer.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serializer.Serializer;
import serializer.impl.KryoSerializer;

/**
 * @Author: Xuyk
 * @Description: kryo编码器
 * @Date: Created in 14:53 2019/3/20
 */
public class KryoEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer = new KryoSerializer();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = serializer.serialize(o);
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }

}
