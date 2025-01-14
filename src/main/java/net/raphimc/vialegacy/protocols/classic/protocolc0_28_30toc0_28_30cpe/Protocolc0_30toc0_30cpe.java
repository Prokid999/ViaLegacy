package net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.*;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import net.raphimc.vialegacy.ViaLegacy;
import net.raphimc.vialegacy.api.*;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.ClientboundPacketsa1_0_15;
import net.raphimc.vialegacy.protocols.alpha.protocola1_0_16_2toa1_0_15.Protocola1_0_16_2toa1_0_15;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.ClientboundPacketsc0_28;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.ServerboundPacketsc0_28;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.data.ClassicBlocks;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.model.ClassicLevel;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.storage.*;
import net.raphimc.vialegacy.protocols.classic.protocola1_0_15toc0_28_30.types.Typesc0_30;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.data.ClassicProtocolExtension;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.data.ExtendedClassicBlocks;
import net.raphimc.vialegacy.protocols.classic.protocolc0_28_30toc0_28_30cpe.storage.*;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.types.Types1_1;
import net.raphimc.vialegacy.protocols.release.protocol1_6_2to1_6_1.Protocol1_6_2to1_6_1;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.ClientboundPackets1_6_4;
import net.raphimc.vialegacy.protocols.release.protocol1_7_2_5to1_6_4.types.Types1_6_4;
import net.raphimc.vialegacy.util.PreNettySplitter;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Protocolc0_30toc0_30cpe extends AbstractProtocol<ClientboundPacketsc0_30cpe, ClientboundPacketsc0_28, ServerboundPacketsc0_30cpe, ServerboundPacketsc0_28> {

    public Protocolc0_30toc0_30cpe() {
        super(ClientboundPacketsc0_30cpe.class, ClientboundPacketsc0_28.class, ServerboundPacketsc0_30cpe.class, ServerboundPacketsc0_28.class);
    }

    @Override
    protected void registerPackets() {
        this.registerClientbound(ClientboundPacketsc0_30cpe.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocol1_6_2to1_6_1.class)) {
                        final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                        final PacketWrapper brand = PacketWrapper.create(ClientboundPackets1_6_4.PLUGIN_MESSAGE, wrapper.user());
                        brand.write(Types1_6_4.STRING, "MC|Brand");
                        final byte[] brandBytes = protocolMetadataStorage.getServerSoftwareName().getBytes(StandardCharsets.UTF_8);
                        brand.write(Type.SHORT, (short) brandBytes.length); // data length
                        brand.write(Type.REMAINING_BYTES, brandBytes); // data

                        wrapper.send(Protocolc0_30toc0_30cpe.class);
                        brand.send(Protocol1_6_2to1_6_1.class);
                        wrapper.cancel();
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXTENSION_PROTOCOL_INFO, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                    protocolMetadataStorage.setServerSoftwareName(wrapper.read(Typesc0_30.STRING)); // app name
                    protocolMetadataStorage.setExtensionCount(wrapper.read(Type.SHORT)); // extension count

                    final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
                    classicProgressStorage.progress = 0;
                    classicProgressStorage.upperBound = protocolMetadataStorage.getExtensionCount();
                    classicProgressStorage.status = "Receiving extension list...";
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXTENSION_PROTOCOL_ENTRY, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final ExtensionProtocolMetadataStorage protocolMetadataStorage = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                    final String extensionName = wrapper.read(Typesc0_30.STRING); // name
                    final int extensionVersion = wrapper.read(Type.INT); // version

                    final ClassicProtocolExtension extension = ClassicProtocolExtension.byName(extensionName);
                    if (extension != null) {
                        protocolMetadataStorage.addServerExtension(extension, extensionVersion);
                    } else {
                        ViaLegacy.getPlatform().getLogger().warning("Received unknown classic protocol extension: (" + extensionName + " v" + extensionVersion + ")");
                    }

                    protocolMetadataStorage.incrementReceivedExtensions();

                    final ClassicProgressStorage classicProgressStorage = wrapper.user().get(ClassicProgressStorage.class);
                    classicProgressStorage.progress = protocolMetadataStorage.getReceivedExtensions();

                    if (protocolMetadataStorage.getReceivedExtensions() >= protocolMetadataStorage.getExtensionCount()) {
                        classicProgressStorage.status = "Sending extension list...";
                        final List<ClassicProtocolExtension> supportedExtensions = new ArrayList<>();
                        for (ClassicProtocolExtension protocolExtension : ClassicProtocolExtension.values()) {
                            if (protocolExtension.isSupported()) {
                                supportedExtensions.add(protocolExtension);
                            }
                        }

                        if (supportedExtensions.contains(ClassicProtocolExtension.HACK_CONTROL)) {
                            wrapper.user().put(new ExtHackControlStorage(wrapper.user()));
                        }
                        if (supportedExtensions.contains(ClassicProtocolExtension.BLOCK_PERMISSIONS)) {
                            wrapper.user().put(new ExtBlockPermissionsStorage(wrapper.user()));
                        }

                        final PacketWrapper extensionProtocolInfo = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXTENSION_PROTOCOL_INFO, wrapper.user());
                        extensionProtocolInfo.write(Typesc0_30.STRING, "ClassiCube 1.3.2"); // app name
                        extensionProtocolInfo.write(Type.SHORT, (short) supportedExtensions.size()); // extension count
                        extensionProtocolInfo.sendToServer(Protocolc0_30toc0_30cpe.class);

                        for (ClassicProtocolExtension protocolExtension : supportedExtensions) {
                            final PacketWrapper extensionProtocolEntry = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXTENSION_PROTOCOL_ENTRY, wrapper.user());
                            extensionProtocolEntry.write(Typesc0_30.STRING, protocolExtension.getName()); // name
                            extensionProtocolEntry.write(Type.INT, protocolExtension.getHighestSupportedVersion()); // version
                            extensionProtocolEntry.sendToServer(Protocolc0_30toc0_30cpe.class);
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final byte level = wrapper.read(Type.BYTE); // support level
                    if (level != 1) {
                        ViaLegacy.getPlatform().getLogger().info("Classic server supports CustomBlocks level " + level);
                    }
                    final PacketWrapper response = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXT_CUSTOM_BLOCKS_SUPPORT_LEVEL, wrapper.user());
                    response.write(Type.BYTE, (byte) 1); // support level
                    response.sendToServer(Protocolc0_30toc0_30cpe.class);
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_HACK_CONTROL, ClientboundPacketsc0_28.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    final ExtHackControlStorage hackControlStorage = wrapper.user().get(ExtHackControlStorage.class);
                    final boolean flying = wrapper.read(Type.BOOLEAN); // flying
                    final boolean noClip = wrapper.read(Type.BOOLEAN); // no clip
                    final boolean speed = wrapper.read(Type.BOOLEAN); // speed
                    final boolean respawn = wrapper.read(Type.BOOLEAN); // respawn key
                    final boolean thirdPerson = wrapper.read(Type.BOOLEAN); // third person view
                    final short jumpHeight = wrapper.read(Type.SHORT); // jump height

                    if (!hackControlStorage.update(flying, noClip, speed, respawn, thirdPerson, jumpHeight)) {
                        wrapper.cancel();
                        return;
                    }

                    String statusMessage = "&6Hack control: ";
                    statusMessage += hackControlStorage.flying ? "&aFlying" : "&cFlying";
                    statusMessage += " ";
                    statusMessage += hackControlStorage.noClip ? "&aNoClip" : "&cNoClip";
                    statusMessage += " ";
                    statusMessage += hackControlStorage.speed ? "&aSpeed" : "&cSpeed";
                    statusMessage += " ";
                    statusMessage += hackControlStorage.respawn ? "&aRespawn" : "&cRespawn";
                    statusMessage += " ";
                    statusMessage += hackControlStorage.thirdPerson ? "&aThird-Person" : "&cThird-Person";
                    statusMessage += " &aJump-Height: " + hackControlStorage.jumpHeight;

                    wrapper.write(Type.BYTE, (byte) 0); // sender id
                    wrapper.write(Typesc0_30.STRING, statusMessage); // message
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_SET_BLOCK_PERMISSION, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final ExtBlockPermissionsStorage blockPermissionsStorage = wrapper.user().get(ExtBlockPermissionsStorage.class);
                    final byte blockId = wrapper.read(Type.BYTE); // block id
                    final boolean canPlace = wrapper.read(Type.BOOLEAN); // can place
                    final boolean canDelete = wrapper.read(Type.BOOLEAN); // can delete

                    if (canPlace) {
                        blockPermissionsStorage.addPlaceable(blockId);
                    } else {
                        blockPermissionsStorage.removePlaceable(blockId);
                    }
                    if (canDelete) {
                        blockPermissionsStorage.addBreakable(blockId);
                    } else {
                        blockPermissionsStorage.removeBreakable(blockId);
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_BULK_BLOCK_UPDATE, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    wrapper.cancel();
                    final ClassicLevelStorage levelStorage = wrapper.user().get(ClassicLevelStorage.class);
                    if (levelStorage == null || !levelStorage.hasReceivedLevel()) {
                        return;
                    }
                    final ClassicBlockRemapper remapper = wrapper.user().get(ClassicBlockRemapper.class);
                    final ClassicLevel level = levelStorage.getClassicLevel();

                    final int count = wrapper.read(Type.UNSIGNED_BYTE) + 1; // count
                    final byte[] indices = wrapper.read(new CustomByteType(1024)); // indices
                    final byte[] blocks = wrapper.read(new CustomByteType(256)); // blocks

                    if (wrapper.user().getProtocolInfo().getPipeline().contains(Protocola1_0_16_2toa1_0_15.class)) {
                        final Map<ChunkCoord, List<BlockChangeRecord>> records = new HashMap<>();
                        for (int i = 0; i < count; i++) {
                            final int index = (indices[i * 4] & 255) << 24 | (indices[i * 4 + 1] & 255) << 16 | (indices[i * 4 + 2] & 255) << 8 | (indices[i * 4 + 3] & 255);
                            final Position pos = new Position(index % level.getSizeX(), (index / level.getSizeX()) / level.getSizeZ(), (index / level.getSizeX()) % level.getSizeZ());
                            final byte blockId = blocks[i];
                            level.setBlock(pos, blockId);
                            if (!levelStorage.isChunkLoaded(pos)) continue;
                            final IdAndData mappedBlock = remapper.getMapper().get(blockId);
                            records.computeIfAbsent(new ChunkCoord(pos.x() >> 4, pos.z() >> 4), k -> new ArrayList<>()).add(new BlockChangeRecord1_8(pos.x() & 15, pos.y(), pos.z() & 15, mappedBlock.toCompressedData()));
                        }

                        for (Map.Entry<ChunkCoord, List<BlockChangeRecord>> entry : records.entrySet()) {
                            final PacketWrapper multiBlockChange = PacketWrapper.create(ClientboundPacketsa1_0_15.MULTI_BLOCK_CHANGE, wrapper.user());
                            multiBlockChange.write(Type.INT, entry.getKey().chunkX); // chunkX
                            multiBlockChange.write(Type.INT, entry.getKey().chunkZ); // chunkZ
                            multiBlockChange.write(Types1_1.BLOCK_CHANGE_RECORD_ARRAY, entry.getValue().toArray(new BlockChangeRecord[0])); // blockChangeRecords
                            multiBlockChange.send(Protocola1_0_16_2toa1_0_15.class);
                        }
                    }
                });
            }
        });
        this.registerClientbound(ClientboundPacketsc0_30cpe.EXT_TWO_WAY_PING, ClientboundPacketsc0_28.KEEP_ALIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    final byte direction = wrapper.read(Type.BYTE); // direction
                    final short data = wrapper.read(Type.SHORT); // data

                    if (direction == 1) {
                        final PacketWrapper pingResponse = PacketWrapper.create(ServerboundPacketsc0_30cpe.EXT_TWO_WAY_PING, wrapper.user());
                        pingResponse.write(Type.BYTE, direction); // direction
                        pingResponse.write(Type.SHORT, data); // data
                        pingResponse.sendToServer(Protocolc0_30toc0_30cpe.class);
                    }
                });
            }
        });

        this.registerServerbound(State.LOGIN, ServerboundPacketsc0_30cpe.LOGIN.getId(), ServerboundPacketsc0_28.LOGIN.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // protocol id
                map(Typesc0_30.STRING); // username
                map(Typesc0_30.STRING); // mp pass
                map(Type.BYTE); // op level
                handler(wrapper -> {
                    wrapper.set(Type.BYTE, 1, (byte) 0x42); // extension protocol magic number
                });
            }
        });
        this.registerServerbound(ServerboundPacketsc0_28.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.BYTE); // sender id
                map(Typesc0_30.STRING); // message
                handler(wrapper -> {
                    final ExtensionProtocolMetadataStorage protocolMetadata = wrapper.user().get(ExtensionProtocolMetadataStorage.class);
                    if (!protocolMetadata.hasServerExtension(ClassicProtocolExtension.LONGER_MESSAGES, 1)) return;
                    wrapper.cancel();

                    String message = wrapper.get(Typesc0_30.STRING, 0);
                    while (!message.isEmpty()) {
                        final int pos = Math.min(message.length(), 64);
                        final String msg = message.substring(0, pos);
                        message = message.substring(pos);
                        final PacketWrapper chatMessage = PacketWrapper.create(ServerboundPacketsc0_30cpe.CHAT_MESSAGE, wrapper.user());
                        chatMessage.write(Type.BYTE, (byte) (!message.isEmpty() ? 1 : 0)); // 1 = more parts | 0 = last part
                        chatMessage.write(Typesc0_30.STRING, msg); // message
                        chatMessage.sendToServer(Protocolc0_30toc0_30cpe.class);
                    }
                });
            }
        });
        this.registerServerbound(ServerboundPacketsc0_28.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Typesc0_30.POSITION); // position
                map(Type.BOOLEAN); // place block
                map(Type.BYTE); // block id
                handler(wrapper -> {
                    if (!wrapper.user().has(ExtBlockPermissionsStorage.class)) return;

                    final ExtBlockPermissionsStorage blockPermissions = wrapper.user().get(ExtBlockPermissionsStorage.class);
                    final ClassicLevel level = wrapper.user().get(ClassicLevelStorage.class).getClassicLevel();

                    final Position position = wrapper.get(Typesc0_30.POSITION, 0);
                    final boolean placeBlock = wrapper.get(Type.BOOLEAN, 0);
                    final int blockId = wrapper.get(Type.BYTE, 0);

                    int block = level.getBlock(position);
                    final boolean disallow = (placeBlock && !blockPermissions.isPlacingAllowed(blockId)) || (!placeBlock && !blockPermissions.isBreakingAllowed(block));

                    if (disallow) {
                        wrapper.cancel();
                        final PacketWrapper chatMessage = PacketWrapper.create(ClientboundPacketsc0_30cpe.CHAT_MESSAGE, wrapper.user());
                        chatMessage.write(Type.BYTE, (byte) 0); // sender id
                        chatMessage.write(Typesc0_30.STRING, "&cYou are not allowed to place/break this block"); // message
                        chatMessage.send(Protocolc0_30toc0_30cpe.class);
                    } else {
                        block = placeBlock ? blockId : ClassicBlocks.AIR;
                        level.setBlock(position, block);
                    }

                    final PacketWrapper blockChange = PacketWrapper.create(ClientboundPacketsc0_30cpe.BLOCK_CHANGE, wrapper.user());
                    blockChange.write(Typesc0_30.POSITION, position); // position
                    blockChange.write(Type.BYTE, (byte) block); // block id
                    blockChange.send(Protocolc0_30toc0_30cpe.class);
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        userConnection.put(new PreNettySplitter(userConnection, Protocolc0_30toc0_30cpe.class, ClientboundPacketsc0_30cpe::getPacket));

        userConnection.put(new ExtensionProtocolMetadataStorage(userConnection));

        final ClassicBlockRemapper previousRemapper = userConnection.get(ClassicBlockRemapper.class);
        userConnection.put(new ClassicBlockRemapper(userConnection, i -> {
            if (ClassicBlocks.MAPPING.containsKey(i)) return previousRemapper.getMapper().get(i);
            final ExtensionProtocolMetadataStorage extensionProtocol = userConnection.get(ExtensionProtocolMetadataStorage.class);
            if (extensionProtocol.hasServerExtension(ClassicProtocolExtension.CUSTOM_BLOCKS, 1)) {
                return ExtendedClassicBlocks.MAPPING.get(i);
            }
            return new IdAndData(BlockList1_6.stone.blockID, 0);
        }, o -> {
            if (ClassicBlocks.REVERSE_MAPPING.containsKey(o)) return previousRemapper.getReverseMapper().getInt(o);
            final ExtensionProtocolMetadataStorage extensionProtocol = userConnection.get(ExtensionProtocolMetadataStorage.class);
            if (extensionProtocol.hasServerExtension(ClassicProtocolExtension.CUSTOM_BLOCKS, 1)) {
                return ExtendedClassicBlocks.REVERSE_MAPPING.getInt(o);
            }
            return ClassicBlocks.STONE;
        }));
    }

}
