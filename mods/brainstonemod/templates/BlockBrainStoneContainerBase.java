package mods.brainstonemod.templates;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockBrainStoneContainerBase extends BlockContainer {

	protected BlockBrainStoneContainerBase(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Checks if the block is a solid face on the given side, used by placement
	 * logic.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y position
	 * @param z
	 *            Z position
	 * @param side
	 *            The side to check
	 * @return True if the block is solid on the specified side.
	 */
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z,
			ForgeDirection side) {
		return true;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("BrainStoneMod:"
				+ this.getUnlocalizedName().replaceFirst("tile.", ""));
	}
}