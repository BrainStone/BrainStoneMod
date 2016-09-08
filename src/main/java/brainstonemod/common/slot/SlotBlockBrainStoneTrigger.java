package brainstonemod.common.slot;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneTrigger;

public class SlotBlockBrainStoneTrigger extends Slot {
	public static boolean staticIsItemValid(ItemStack itemstack) {
		if (!(itemstack.getItem() instanceof ItemBlock))
			return false;

		final Block block = Block.getBlockFromItem(itemstack.getItem());
		if (block == null)
			return false;

		if ((block == BrainStone.brainStoneTrigger())
				|| (block == Blocks.leaves))
			return false;
		else
			return block.isOpaqueCube();
	}

	public SlotBlockBrainStoneTrigger(
			TileEntityBlockBrainStoneTrigger tileentityblockbrainstonetrigger,
			int i, int j, int k) {
		super(tileentityblockbrainstonetrigger, i, j, k);
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return staticIsItemValid(itemstack);
	}
}
