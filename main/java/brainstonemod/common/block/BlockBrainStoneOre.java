package brainstonemod.common.block;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneOreBase;

public class BlockBrainStoneOre extends BlockBrainStoneOreBase {
	/**
	 * Constructor of the block. Registers all properties and sets the id and
	 * the material
	 * 
	 * @param i
	 *            The internal BrainStone id
	 */
	public BlockBrainStoneOre() {
		super();

		setHardness(2.0F);
		this.setUnlocalizedName("brainStoneOre");
		setResistance(0.25F);
		this.setLightValue(0.3F);
		setCreativeTab(CreativeTabs.tabBlock);
		blockParticleGravity = 0.2F;
	}

	@Override
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3,
			int par4, int par5, float par6, int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5,
				par6, par7);

		if (getItemDropped(par5, par1World.rand, par7) != Item
				.getItemFromBlock(this)) {
			int var8 = 0;

			if (this == BrainStone.brainStoneOre()) {
				var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 10,
						20);
			}

			dropXpOnBlockBreak(par1World, par2, par3, par4, var8);
		}
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
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
