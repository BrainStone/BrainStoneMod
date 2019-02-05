package brainstonemod.common.block;

import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockBrainStone extends Block {
	/**
	 * Saves the state given in the constructor named flag.<br>
	 * Will be used in the code. Cannot be changed.
	 */
	private final boolean powered;

	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 *
	 * @param flag
	 *            Is the block powered by Redstone? If true the block does not
	 *            emit light, if yes it does
	 */
	public BlockBrainStone(boolean flag) {
		super(Material.ROCK);
		setHardness(3.0F);
		setResistance(1.0F);
		setHarvestLevel("pickaxe", 2);

		if (!flag) {
			setLightLevel(1.0F);
			setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS));
		}

		powered = flag;
		blockParticleGravity = -0.2F;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BrainStoneBlocks.dirtyBrainStone());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(BrainStoneBlocks.brainStone());
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			if (powered && (world.isBlockIndirectlyGettingPowered(pos) == 0)) {
				world.setBlockState(pos, BrainStoneBlocks.brainStone().getDefaultState(), 2);
			} else if (!powered && (world.isBlockIndirectlyGettingPowered(pos) > 0)) {
				world.setBlockState(pos, BrainStoneBlocks.brainStoneOut().getDefaultState(), 2);
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		onBlockAdded(worldIn, pos, worldIn.getBlockState(pos));
	}
}
