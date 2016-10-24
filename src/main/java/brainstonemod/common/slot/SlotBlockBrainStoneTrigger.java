package brainstonemod.common.slot;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import brainstonemod.common.tileentity.TileEntityBrainStoneTrigger;

public class SlotBlockBrainStoneTrigger extends Slot {
	public SlotBlockBrainStoneTrigger(
			TileEntityBrainStoneTrigger tileentityblockbrainstonetrigger,
			int i, int j, int k) {
		super(tileentityblockbrainstonetrigger, i, j, k);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
        if (!(itemstack.getItem() instanceof ItemBlock))
            return false;

        final Block block = Block.getBlockFromItem(itemstack.getItem());
        return block != null && !(block == BrainStone.brainStoneTrigger() || block == Blocks.LEAVES) && block.isOpaqueCube(block.getDefaultState());

    }
}
