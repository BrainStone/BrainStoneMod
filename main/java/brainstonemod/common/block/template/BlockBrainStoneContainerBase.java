package brainstonemod.common.block.template;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

public abstract class BlockBrainStoneContainerBase extends BlockContainer {

	protected BlockBrainStoneContainerBase(Material par2Material) {
		super(par2Material);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z,
			int side) {
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ getUnlocalizedName().replaceFirst("tile.", ""));
	}
}