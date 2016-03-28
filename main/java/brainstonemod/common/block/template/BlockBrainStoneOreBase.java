package brainstonemod.common.block.template;

import brainstonemod.BrainStone;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockBrainStoneOreBase extends BlockOre {
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister
				.registerIcon(BrainStone.RESOURCE_PREFIX + getUnlocalizedName().replaceFirst("tile.", ""));
	}
}