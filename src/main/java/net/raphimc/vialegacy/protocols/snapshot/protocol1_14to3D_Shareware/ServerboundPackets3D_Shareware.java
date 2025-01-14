package net.raphimc.vialegacy.protocols.snapshot.protocol1_14to3D_Shareware;

import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;

public enum ServerboundPackets3D_Shareware implements ServerboundPacketType {

    TELEPORT_CONFIRM, // 0x00
    QUERY_BLOCK_NBT, // 0x01
    SET_DIFFICULTY, // 0x02
    CHAT_MESSAGE, // 0x03
    CLIENT_STATUS, // 0x04
    CLIENT_SETTINGS, // 0x05
    TAB_COMPLETE, // 0x06
    WINDOW_CONFIRMATION, // 0x07
    CLICK_WINDOW_BUTTON, // 0x08
    CLICK_WINDOW, // 0x09
    CLOSE_WINDOW, // 0x0A
    PLUGIN_MESSAGE, // 0x0B
    EDIT_BOOK, // 0x0C
    ENTITY_NBT_REQUEST, // 0x0D
    INTERACT_ENTITY, // 0x0E
    LOCK_DIFFICULTY, // 0x0F
    KEEP_ALIVE, // 0x10
    PLAYER_MOVEMENT, // 0x11
    PLAYER_POSITION, // 0x12
    PLAYER_POSITION_AND_ROTATION, // 0x13
    PLAYER_ROTATION, // 0x14
    VEHICLE_MOVE, // 0x15
    STEER_BOAT, // 0x16
    PICK_ITEM, // 0x17
    CRAFT_RECIPE_REQUEST, // 0x18
    PLAYER_ABILITIES, // 0x19
    PLAYER_DIGGING, // 0x1A
    ENTITY_ACTION, // 0x1B
    STEER_VEHICLE, // 0x1C
    RECIPE_BOOK_DATA, // 0x1D
    RENAME_ITEM, // 0x1E
    RESOURCE_PACK_STATUS, // 0x1F
    ADVANCEMENT_TAB, // 0x20
    SELECT_TRADE, // 0x21
    SET_BEACON_EFFECT, // 0x22
    HELD_ITEM_CHANGE, // 0x23
    UPDATE_COMMAND_BLOCK, // 0x24
    UPDATE_COMMAND_BLOCK_MINECART, // 0x25
    CREATIVE_INVENTORY_ACTION, // 0x26
    UPDATE_JIGSAW_BLOCK, // 0x27
    UPDATE_STRUCTURE_BLOCK, // 0x28
    UPDATE_SIGN, // 0x29
    ANIMATION, // 0x2A
    SPECTATE, // 0x2B
    PLAYER_BLOCK_PLACEMENT, // 0x2C
    USE_ITEM; // 0x2D

    @Override
    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getName() {
        return this.name();
    }

}
