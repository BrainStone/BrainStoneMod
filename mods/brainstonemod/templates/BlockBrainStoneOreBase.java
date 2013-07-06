package mods.brainstonemod.templates;

import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockBrainStoneOreBase extends BlockOre {

	public BlockBrainStoneOreBase(int par1) {
		super(par1);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("tile.", ""));
	}
}