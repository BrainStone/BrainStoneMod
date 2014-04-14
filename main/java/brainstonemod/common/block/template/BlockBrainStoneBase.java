package brainstonemod.common.block.template;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class BlockBrainStoneBase extends Block {

	public BlockBrainStoneBase(Material par2Material) {
		super(par2Material);
	}

	protected Block getBlockDropped(int par1, Random rand, int par3) {
		return this;
	}

	@Override
	public Item getItemDropped(int par1, Random rand, int par3) {
		return Item.getItemFromBlock(getBlockDropped(par1, rand, par3));
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ getUnlocalizedName().replaceFirst("tile.", ""));
	}
}