package brainstonemod.common.block.template;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public abstract class BlockBrainStoneHiders extends BlockBrainStoneContainerBase {
	public BlockBrainStoneHiders() {
		super(Material.rock);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabRedstone));
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
		final TileEntity te = iblockaccess.getTileEntity(x, y, z);

		if(te != null && te instanceof IInventory) {
			ItemStack itemstack = ((IInventory) te).getStackInSlot(0);

			if (itemstack == null)
				return 0xffffff;

			Block block = Block.getBlockFromItem(itemstack.getItem());

			if (block == null)
				return 0xffffff;
			else
				return block.colorMultiplier(iblockaccess, x, y, z);
		}

		return 0xffffff;
	}
}