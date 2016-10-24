package brainstonemod.common.block;

import brainstonemod.BrainStone;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBrainStoneOre extends BlockOre {
	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 *
     */
	public BlockBrainStoneOre() {
		super();

		setHardness(2.0F);
		setResistance(0.25F);
		setLightLevel(0.3F);
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS));
		setHarvestLevel("pickaxe", 2);
		blockParticleGravity = 0.2F;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
		super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);

		if (getItemDropped(state, world.rand, fortune) != Item
				.getItemFromBlock(this)) {
			int var8 = 0;

			if (this == BrainStone.brainStoneOre()) {
				var8 = MathHelper.getRandomIntegerInRange(world.rand, 10,
						20);
			}

			dropXpOnBlockBreak(world, pos, var8);
		}
	}

	@Override
	public Item getItemDropped(IBlockState i, Random random, int j) {
		return BrainStone.brainStoneDust();
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(2);
	}

	@Override
	public int quantityDroppedWithBonus(int i, Random random) {
		return random.nextInt(2 + i);
	}
}
