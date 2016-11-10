package brainstonemod.common.block.template;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockBrainStoneBase extends Block {

	public BlockBrainStoneBase(Material par2Material) {
		super(par2Material);
	}

	protected Block getBlockDropped(IBlockState block, Random rand, int fortune) {
		return this;
	}

	@Override
	public Item getItemDropped(IBlockState block, Random rand, int fortune) {
		return Item.getItemFromBlock(getBlockDropped(block, rand, fortune));
	}
}