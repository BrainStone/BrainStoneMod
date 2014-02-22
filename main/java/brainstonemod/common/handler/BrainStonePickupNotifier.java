package brainstonemod.common.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.BrainStone;

public class BrainStonePickupNotifier implements IPickupNotifier {
	@Override
	public void notifyPickup(EntityItem item, EntityPlayer player) {
		if (item.getEntityItem().getItem() == BrainStone.brainStoneDust()) {
			player.addStat(BrainStone.WTHIT(), 1);
		}
	}
}