package brainstonemod.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.IPickupNotifier;

public class BrainStonePickupNotifier implements IPickupNotifier {
	@Override
	public void notifyPickup(EntityItem item, EntityPlayer player) {
		if (item.getEntityItem().itemID == BrainStone.brainStoneDust().itemID) {
			player.addStat(BrainStone.WTHIT(), 1);
		}
	}
}