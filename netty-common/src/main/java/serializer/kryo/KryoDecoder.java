package serializer.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import serializer.Serializer;
import serializer.impl.KryoSerializer;

/**
 * @Author: Xuyk
 * @Description: kryo解码器
 * @Date: Created in 14:54 2019/3/20
 */
public class KryoDecoder extends LengthFieldBasedFrameDecoder {


    private Serializer serializer = new KryoSerializer();

    public KryoDecoder(int maxFrameLength) {
        super(maxFrameLength, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode != null) {
            int byteLength = decode.readableBytes();
            byte[] byteHolder = new byte[byteLength];
            decode.readBytes(byteHolder);
            Object deserialize = serializer.deserialize(byteHolder);
            return deserialize;
        }
        System.out.println("Decoder Result is null");
        return null;
    }
}
