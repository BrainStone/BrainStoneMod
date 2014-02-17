package brainstonemod.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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
	 * @param i
	 *            The internal BrainStone id
	 * @param flag
	 *            Is the block powered by Redstone? If true the block does not
	 *            emit light, if yes it does
	 */
	public BlockBrainStone(boolean flag) {
		super(Material.rock);
		setHardness(3.0F);
		setResistance(1.0F);

		if (flag) {
			this.setLightValue(0.0F);
			this.setUnlocalizedName("brainStoneOut");
		} else {
			this.setLightValue(1.0F);
			this.setUnlocalizedName("brainStone");
			setCreativeTab(CreativeTabs.tabBlock);
		}

		powered = flag;
		blockParticleGravity = -0.2F;
	}

	@Override
	public Block getBlockDropped(int i, Random random, int j) {
		return BrainStone.dirtyBrainStone();
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
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!world.isRemote) {
			if (powered && !world.isBlockIndirectlyGettingPowered(i, j, k)) {
				world.setBlock(i, j, k, BrainStone.brainStone(), 0, 2);
			} else if (!powered
					&& world.isBlockIndirectlyGettingPowered(i, j, k)) {
				world.setBlock(i, j, k, BrainStone.brainStoneOut(), 0, 2);
			}
		}
	}
}
