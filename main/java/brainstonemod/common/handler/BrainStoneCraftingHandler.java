package brainstonemod.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;

public class BrainStoneCraftingHandler implements ICraftingHandler {
	@Override
	public void onCrafting(EntityPlayer player, ItemStack itemStack,
			IInventory craftMatrix) {
		final Item item = itemStack.getItem();
		final Block block = Block.getBlockFromItem(item);

		if ((block == BrainStone.brainLightSensor())
				|| (block == BrainStone.brainStoneTrigger())) {
			player.addStat(BrainStone.intelligentBlocks(), 1);
		} else if ((item == BrainStone.brainStoneSword())
				|| (item == BrainStone.brainStoneShovel())
				|| (item == BrainStone.brainStonePickaxe())
				|| (item == BrainStone.brainStoneAxe())
				|| (item == BrainStone.brainStoneHoe())) {
			player.addStat(BrainStone.intelligentTools(), 1);
		} else if (block == BrainStone.brainLogicBlock()) {
			player.addStat(BrainStone.logicBlock(), 1);
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack itemStack) {
		if (Block.getBlockFromItem(itemStack.getItem())== BrainStone.brainStone()) {
			player.addStat(BrainStone.itLives(), 1);
		}
	}
}