package brainstonemod.common.block;

import java.util.Random;

import brainstonemod.BrainStone;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();

		if (getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
			int i = 0;

			if (this == BrainStone.brainStoneOre()) {
				i = MathHelper.getRandomIntegerInRange(rand, 10, 20);
			}

			return i;
		}
		return 0;
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
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return random.nextInt(2 + fortune);
	}
}
