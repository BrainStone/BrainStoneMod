package mods.brainstonemod.templates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockBrainStoneBase extends Block {

	public BlockBrainStoneBase(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("brainstone:"
				+ this.getUnlocalizedName().replaceFirst("tile.", ""));
	}
}