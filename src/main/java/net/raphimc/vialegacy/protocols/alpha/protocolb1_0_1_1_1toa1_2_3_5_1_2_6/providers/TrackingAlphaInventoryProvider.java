package net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.storage.AlphaInventoryTracker;

import static net.raphimc.vialegacy.protocols.alpha.protocolb1_0_1_1_1toa1_2_3_5_1_2_6.Protocolb1_0_1_1_1toa1_2_3_5_1_2_6.copyItems;

public class TrackingAlphaInventoryProvider extends AlphaInventoryProvider {

    @Override
    public boolean usesInventoryTracker() {
        return true;
    }

    @Override
    public Item[] getMainInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getMainInventory());
    }

    @Override
    public Item[] getCraftingInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getCraftingInventory());
    }

    @Override
    public Item[] getArmorInventoryItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getArmorInventory());
    }

    @Override
    public Item[] getContainerItems(UserConnection user) {
        return copyItems(user.get(AlphaInventoryTracker.class).getOpenContainerItems());
    }

    @Override
    public void addToInventory(UserConnection user, Item item) {
        user.get(AlphaInventoryTracker.class).addItem(item);
    }

}
