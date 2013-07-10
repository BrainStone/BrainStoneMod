package brainstonemod.templates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import brainstonemod.BrainStone;
import brainstonemod.tileentities.TileEntityBlockBrainStoneHiders;

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
	public BlockBrainStoneHiders(int i) {
		super(BrainStone.getId(i), Material.rock);

		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		final TileEntityBlockBrainStoneHiders tileentityblockbrainstonehiders = (TileEntityBlockBrainStoneHiders) iblockaccess
				.getBlockTileEntity(i, j, k);

		if (tileentityblockbrainstonehiders == null)
			return 0xffffff;

		final ItemStack itemstack = tileentityblockbrainstonehiders
				.getStackInSlot(0);

		if (itemstack == null)
			return 0xffffff;

		final int l = itemstack.itemID;

		if ((l >= Block.blocksList.length) || (l < 0))
			return 0xffffff;

		final Block block = Block.blocksList[l];

		if (block == null)
			return 0xffffff;
		else
			return Block.blocksList[l].colorMultiplier(iblockaccess, i, j, k);
	}
}