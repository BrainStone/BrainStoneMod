package brainstonemod.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import cpw.mods.fml.common.ICraftingHandler;

public class BrainStoneCraftingHandler implements ICraftingHandler {
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item,
			IInventory craftMatrix) {
		final int i = item.itemID;

		if ((i == BrainStone.brainLightSensor().blockID)
				|| (i == BrainStone.brainStoneTrigger().blockID)) {
			player.addStat(BrainStone.intelligentBlocks(), 1);
		} else if ((i == BrainStone.brainStoneSword().itemID)
				|| (i == BrainStone.brainStoneShovel().itemID)
				|| (i == BrainStone.brainStonePickaxe().itemID)
				|| (i == BrainStone.brainStoneAxe().itemID)
				|| (i == BrainStone.brainStoneHoe().itemID)) {
			player.addStat(BrainStone.intelligentTools(), 1);
		} else if (i == BrainStone.brainLogicBlock().blockID) {
			player.addStat(BrainStone.logicBlock(), 1);
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
		if (item.itemID == BrainStone.brainStone().blockID) {
			player.addStat(BrainStone.itLives(), 1);
		}
	}
}