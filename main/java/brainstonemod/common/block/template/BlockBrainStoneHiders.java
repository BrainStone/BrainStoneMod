package brainstonemod.common.block.template;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import brainstonemod.common.tileentity.TileEntityBlockBrainStoneHiders;

public abstract class BlockBrainStoneHiders extends
		BlockBrainStoneContainerBase {
	// protected boolean renderGrass;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainStoneHiders() {
		super(Material.rock);

		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
		final TileEntityBlockBrainStoneHiders tileentityblockbrainstonehiders = (TileEntityBlockBrainStoneHiders) iblockaccess
				.getTileEntity(x, y, z);

		if (tileentityblockbrainstonehiders == null)
			return 0xffffff;

		final ItemStack itemstack = tileentityblockbrainstonehiders
				.getStackInSlot(0);

		if (itemstack == null)
			return 0xffffff;

		final Block block = Block.getBlockFromItem(itemstack.getItem());

		if (block == null)
			return 0xffffff;
		else
			return block.colorMultiplier(iblockaccess, x, y, z);
	}
}