package net.raphimc.vialegacy.netty;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PreNettyEncoder extends MessageToByteEncoder<ByteBuf> {

    protected final UserConnection user;

    public PreNettyEncoder(final UserConnection user) {
        this.user = user;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        Type.VAR_INT.readPrimitive(in); // length
        out.writeByte(Type.VAR_INT.readPrimitive(in) & 255); // id
        out.writeBytes(in); // content
    }

}
