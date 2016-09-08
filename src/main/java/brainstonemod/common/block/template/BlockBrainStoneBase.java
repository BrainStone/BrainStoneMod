package brainstonemod.common.block.template;

import java.util.Random;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class BlockBrainStoneBase extends Block {

	public BlockBrainStoneBase(Material par2Material) {
		super(par2Material);
	}

	protected Block getBlockDropped(int meta, Random rand, int fortune) {
		return this;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return Item.getItemFromBlock(getBlockDropped(meta, rand, fortune));
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister
				.registerIcon(BrainStone.RESOURCE_PREFIX + getUnlocalizedName().replaceFirst("tile.", ""));
	}
}