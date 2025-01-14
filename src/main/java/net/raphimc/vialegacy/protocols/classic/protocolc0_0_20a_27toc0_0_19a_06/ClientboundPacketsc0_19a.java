package net.raphimc.vialegacy.protocols.classic.protocolc0_0_20a_27toc0_0_19a_06;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import net.raphimc.vialegacy.util.PreNettyPacketType;

import java.util.function.BiConsumer;

import static net.raphimc.vialegacy.util.PreNettyTypes.readByteArray1024;
import static net.raphimc.vialegacy.util.PreNettyTypes.readString64;

public enum ClientboundPacketsc0_19a implements ClientboundPacketType, PreNettyPacketType {

    JOIN_GAME(0, (user, buf) -> {
        buf.readByte();
        readString64(buf);
        readString64(buf);
    }),
    KEEP_ALIVE(1, (user, buf) -> {
    }),
    LEVEL_INIT(2, (user, buf) -> {
    }),
    LEVEL_DATA(3, (user, buf) -> {
        buf.readShort();
        readByteArray1024(buf);
        buf.readByte();
    }),
    LEVEL_FINALIZE(4, (user, buf) -> {
        buf.readShort();
        buf.readShort();
        buf.readShort();
    }),
    BLOCK_CHANGE(6, (user, buf) -> {
        buf.readShort();
        buf.readShort();
        buf.readShort();
        buf.readByte();
    }),
    SPAWN_PLAYER(7, (user, buf) -> {
        buf.readByte();
        readString64(buf);
        buf.readShort();
        buf.readShort();
        buf.readShort();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_TELEPORT(8, (user, buf) -> {
        buf.readByte();
        buf.readShort();
        buf.readShort();
        buf.readShort();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_POSITION_AND_ROTATION(9, (user, buf) -> {
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_POSITION(10, (user, buf) -> {
        buf.readByte();
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    ENTITY_ROTATION(11, (user, buf) -> {
        buf.readByte();
        buf.readByte();
        buf.readByte();
    }),
    DESTROY_ENTITIES(12, (user, buf) -> {
        buf.readByte();
    }),
    CHAT_MESSAGE(13, (user, buf) -> {
        buf.readByte();
        readString64(buf);
    }),
    DISCONNECT(14, (user, buf) -> {
        readString64(buf);
    });

    private static final ClientboundPacketsc0_19a[] REGISTRY = new ClientboundPacketsc0_19a[256];

    static {
        for (ClientboundPacketsc0_19a packet : values()) {
            REGISTRY[packet.id] = packet;
        }
    }

    public static ClientboundPacketsc0_19a getPacket(final int id) {
        return REGISTRY[id];
    }

    private final int id;
    private final BiConsumer<UserConnection, ByteBuf> packetReader;

    ClientboundPacketsc0_19a(final int id, final BiConsumer<UserConnection, ByteBuf> packetReader) {
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
