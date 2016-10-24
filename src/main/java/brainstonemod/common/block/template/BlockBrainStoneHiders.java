package brainstonemod.common.block.template;

import brainstonemod.BrainStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockBrainStoneHiders extends BlockBrainStoneContainerBase {
	public BlockBrainStoneHiders() {
		super(Material.ROCK);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.REDSTONE));
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	//TODO: Find out if there is an equivalent to this now. MapColor, perhaps?
	/*@Override
	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos pos) {
		final TileEntity te = iblockaccess.getTileEntity(pos);

		if(te != null && te instanceof IInventory) {
			ItemStack itemstack = ((IInventory) te).getStackInSlot(0);

			if (itemstack == null)
				return 0xffffff;

			Block block = Block.getBlockFromItem(itemstack.getItem());

			if (block == null)
				return 0xffffff;
			else
				return block.colorMultiplier(iblockaccess, pos);
		}

		return 0xffffff;
	}*/
}