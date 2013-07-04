package mods.brainstonemod.slots;

import mods.brainstonemod.BrainStone;
import mods.brainstonemod.tileentities.TileEntityBlockBrainStoneTrigger;
import net.minecraft.block.Block;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotBlockBrainStoneTrigger extends Slot {
	@SuppressWarnings("unused")
	public static boolean staticIsItemValid(ItemStack itemstack) {
		if (!(itemstack.getItem() instanceof ItemBlock))
			return false;

		final int i = itemstack.itemID;

		if ((i > Block.blocksList.length) || (i <= 0))
			return false;

		final Block block = Block.blocksList[i];
		final int j = block.blockID;

		if (block == null)
			return false;

		if ((j == BrainStone.brainStoneTrigger().blockID)
				|| (j == Block.leaves.blockID))
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
