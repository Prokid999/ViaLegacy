package net.raphimc.vialegacy.protocols.beta.protocolb1_6_0_6tob1_5_0_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.util.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.util.PreNettyTypes.readItemStackb1_2;
import static net.raphimc.vialegacy.util.PreNettyTypes.readString;

public enum ServerboundPacketsb1_5 implements ServerboundPacketType, PreNettyPacketType {

    KEEP_ALIVE(0, (user, buf) -> {
    }),
    LOGIN(1, (user, buf) -> {
        buf.readInt();
        readString(buf);
        buf.readLong();
        buf.readByte();
    }),
    HANDSHAKE(2, (user, buf) -> {
        readString(buf);
    }),
    CHAT_MESSAGE(3, (user, buf) -> {
        readString(buf);
    }),
    INTERACT_ENTITY(7, (user, buf) -> {
        buf.readInt();
        buf.readInt();
        buf.readByte();
    }),
    RESPAWN(9, (user, buf) -> {
    }),
    PLAYER_MOVEMENT(10, (user, buf) -> {
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION(11, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readUnsignedByte();
    }),
    PLAYER_ROTATION(12, (user, buf) -> {
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    PLAYER_POSITION_AND_ROTATION(13, (user, buf) -> {
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readDouble();
        buf.readFloat();
        buf.readFloat();
        buf.readUnsignedByte();
    }),
    PLAYER_DIGGING(14, (user, buf) -> {
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
    }),
    PLAYER_BLOCK_PLACEMENT(15, (user, buf) -> {
        buf.readInt();
        buf.readUnsignedByte();
        buf.readInt();
        buf.readUnsignedByte();
        readItemStackb1_2(buf);
    }),
    HELD_ITEM_CHANGE(16, (user, buf) -> {
        buf.readShort();
    }),
    ANIMATION(18, (user, buf) -> {
        buf.readInt();
        buf.readByte();
    }),
    ENTITY_ACTION(19, (user, buf) -> {
        buf.readInt();
        buf.readByte();
    }),
    POSITION(27, (user, buf) -> {
        buf.readFloat();
        buf.readFloat();
        buf.readFloat();
        buf.readFloat();
        buf.readBoolean();
        buf.readBoolean();
    }),
    CLOSE_WINDOW(101, (user, buf) -> {
        buf.readByte();
    }),
    CLICK_WINDOW(102, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readByte();
        buf.readShort();
        buf.readBoolean();
        readItemStackb1_2(buf);
    }),
    WINDOW_CONFIRMATION(106, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readByte();
    }),
    UPDATE_SIGN(130, (user, buf) -> {
        buf.readInt();
        buf.readShort();
        buf.readInt();
        readString(buf);
        readString(buf);
        readString(buf);
        readString(buf);
    }),
    DISCONNECT(255, (user, buf) -> {
        readString(buf);
    });

    private static final ServerboundPacketsb1_5[] REGISTRY = new ServerboundPacketsb1_5[256];

    static {
        for (ServerboundPacketsb1_5 packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ServerboundPacketsb1_5 getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ServerboundPacketsb1_5(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
        this.id = id;
        this.packetReader = packetReader;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public BiConsumer<UserConnection, ByteBuf> getPacketReader() {
        return this.packetReader;
    }

}
