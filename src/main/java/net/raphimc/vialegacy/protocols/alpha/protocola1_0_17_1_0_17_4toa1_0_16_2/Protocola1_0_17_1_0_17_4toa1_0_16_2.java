package net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_17_1_0_17_4toa1_0_16_2.storage.TimeLockStorage;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.ClientboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocols.alpha.protocola1_1_0_1_1_2_1toa1_0_17_1_0_17_4.ServerboundPacketsa1_0_17;
import net.raphimc.vialegacy.protocols.release.protocol1_8to1_7_6_10.types.Types1_7_6;
import net.raphimc.vialegacy.util.PreNettySplitter;

public class Protocola1_0_17_1_0_17_4toa1_0_16_2 extends AbstractProtocol<ClientboundPacketsa1_0_16, ClientboundPacketsa1_0_17, ServerboundPacketsa1_0_17, ServerboundPacketsa1_0_17> {

    public Protocola1_0_17_1_0_17_4toa1_0_16_2() {
        super(ClientboundPacketsa1_0_16.class, ClientboundPacketsa1_0_17.class, ServerboundPacketsa1_0_17.class, ServerboundPacketsa1_0_17.class);
    }

    @Override
    protected void registerPackets() {
        this.registerServerbound(ServerboundPacketsa1_0_17.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.SHORT); // item id
                map(Types1_7_6.POSITION_UBYTE); // position
                map(Type.UNSIGNED_BYTE); // direction
                handler(wrapper -> {
                    if (wrapper.get(Type.SHORT, 0) < 0) {
                        wrapper.cancel();
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocola1_0_17_1_0_17_4toa1_0_16_2.class, ClientboundPacketsa1_0_16::getPacket));

        userConnection.put(new TimeLockStorage(userConnection, 0));
    }

}
