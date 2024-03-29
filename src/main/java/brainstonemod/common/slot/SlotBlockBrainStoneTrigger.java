package brainstonemod.common.slot;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotBlockBrainStoneTrigger extends Slot {
	public SlotBlockBrainStoneTrigger(TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger, int i, int j,
			int k) {
		super(tileentityblockbrainstonetrigger, i, j, k);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		if (!(itemstack.getItem() instanceof ItemBlock))
			return false;

		final Block block = Block.getBlockFromItem(itemstack.getItem());
		return (block != null) && !((block == BrainStoneBlocks.brainStoneTrigger()) || (block == Blocks.LEAVES))
				&& block.isOpaqueCube(block.getDefaultState());

	}
}
