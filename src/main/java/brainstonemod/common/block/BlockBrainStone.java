package brainstonemod.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneBase;

public class BlockBrainStone extends BlockBrainStoneBase {
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
		super(Material.rock);
		setHardness(3.0F);
		setResistance(1.0F);
		setHarvestLevel("pickaxe", 2);

		if (!flag) {
			setLightLevel(1.0F);
			setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabBlock));
		}

		powered = flag;
		blockParticleGravity = -0.2F;
	}

	@Override
	public Block getBlockDropped(int i, Random random, int j) {
		return BrainStone.dirtyBrainStone();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z) {
		return new ItemStack(BrainStone.brainStone());
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		if (!world.isRemote) {
			if (powered && !world.isBlockIndirectlyGettingPowered(i, j, k)) {
				world.setBlock(i, j, k, BrainStone.brainStone(), 0, 2);
			} else if (!powered
					&& world.isBlockIndirectlyGettingPowered(i, j, k)) {
				world.setBlock(i, j, k, BrainStone.brainStoneOut(), 0, 2);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		if (!world.isRemote) {
			if (powered && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
				world.setBlock(x, y, z, BrainStone.brainStone(), 0, 2);
			} else if (!powered
					&& world.isBlockIndirectlyGettingPowered(x, y, z)) {
				world.setBlock(x, y, z, BrainStone.brainStoneOut(), 0, 2);
			}
		}
	}
}