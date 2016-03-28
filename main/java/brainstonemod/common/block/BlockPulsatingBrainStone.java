package brainstonemod.common.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.common.api.enderio.BrainStoneUpgrade;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.BrainStonePacketHelper;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockPulsatingBrainStone extends BlockBrainStoneBase {
	private final boolean effect;
	private static Block hasEffectBlock, hasNoEffectBlock;
	private static long lastGravityChange = 0;

	public BlockPulsatingBrainStone(boolean effect) {
		super(Material.rock);

		this.effect = effect;

		setHardness(3.0F);
		setResistance(1.0F);
		setLightLevel(1.0F);
		setHarvestLevel("pickaxe", 2);

		if (effect) {
			hasEffectBlock = this;
		} else {
			setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabBlock));
			hasNoEffectBlock = this;
		}

		blockParticleGravity = (float) MathHelper.getRandomDoubleInRange(new Random(), -3.0, 3.0);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	protected Block getBlockDropped(int meta, Random random, int fortune) {
		return hasNoEffectBlock;
	}

	@Override
	public int damageDropped(int meta) {
		if (effect)
			return 0;
		else
			return meta & 0x01;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(BrainStone.pulsatingBrainStone());
	}

	private int getRandomPotion(Random random) {
		Potion potion;

		do {
			potion = Potion.potionTypes[random.nextInt(Potion.potionTypes.length)];
		} while (potion == null);

		return potion.id;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, this, (int) world.getTotalWorldTime() % tickRate(world));
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		final int metaData = (int) ((world.getTotalWorldTime() / tickRate(world)) % 16);

		if (metaData >= 15) {
			if (effect) {
				if (random.nextInt(2) == 0) {
					world.setBlock(x, y, z, hasNoEffectBlock, 0, 2);
				}
			} else {
				if (random.nextInt(4) == 0) {
					world.setBlock(x, y, z, hasEffectBlock, 0, 2);
				}
			}
		} else if ((metaData == 8) && (effect)) {
			BSP.debug("", "Effect Time!");

			double radius;
			int taskRand;
			EntityLivingBase entity;
			Object tmpEntity;
			final List<?> list = world.getEntitiesWithinAABBExcludingEntity(null,
					AxisAlignedBB.getBoundingBox(x - 10, y - 10, z - 10, x + 11, y + 11, z + 11));

			final int size = list.size();

			for (int i = 0; i < size; i++) {
				tmpEntity = list.get(i);

				BSP.debug(tmpEntity.getClass().getName());

				if (tmpEntity instanceof EntityLivingBase) {
					entity = (EntityLivingBase) tmpEntity;

					final ItemStack[] equipment = entity.getLastActiveItems();

					BSP.debug(entity, Arrays.toString(equipment));

					if (isProtected(equipment)) {
						BSP.debug("Mob/Player wears armor! No effect!");

						continue;
					}

					radius = MathHelper.getRandomDoubleInRange(random, 2.0, 10.0);

					if (entity.getDistance((x) + 0.5, (y) + 0.5, (z) + 0.5) <= radius) {
						taskRand = random.nextInt(10);

						if ((taskRand >= 0) && (taskRand < 6)) {
							BSP.debug("Potion Effect");

							entity.addPotionEffect(new PotionEffect(getRandomPotion(random), random.nextInt(5980) + 20,
									random.nextInt(4)));
						} else if ((taskRand >= 6) && (taskRand < 10)) {
							BSP.debug("Kick");

							final double x1 = MathHelper.getRandomDoubleInRange(random, -1.5, 1.5);
							final double y1 = MathHelper.getRandomDoubleInRange(random, 0.0, 3.0);
							final double z1 = MathHelper.getRandomDoubleInRange(random, -1.5, 1.5);

							if (tmpEntity instanceof EntityPlayer) {
								BrainStonePacketHelper.sendPlayerUpdateMovementPacket((EntityPlayer) entity, x1, y1,
										z1);
							} else {
								entity.addVelocity(x1, y1, z1);
							}
						}
					}
				}
			}
		}

		if (world.getTotalWorldTime() > lastGravityChange) {
			blockParticleGravity = (float) MathHelper.getRandomDoubleInRange(random, -3.0, 3.0);

			lastGravityChange = world.getTotalWorldTime();
		}

		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	private static boolean isProtected(ItemStack[] armor) {
		int offset = 0;

		if (armor.length < 4)
			return false;
		else if (armor.length == 5)
			offset = 1;
		else if (armor.length > 5)
			return false;

		boolean allArmorSlotsFilled = (armor[3 + offset] != null) && (armor[2 + offset] != null)
				&& (armor[1 + offset] != null) && (armor[0 + offset] != null);

		if (!allArmorSlotsFilled)
			return false;

		boolean enderIOEnabled = Loader.isModLoaded("EnderIO");

		if (!((armor[3 + offset].getItem() == BrainStone.brainStoneHelmet())
				|| (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[3 + offset])))))
			return false;
		else if (!((armor[2 + offset].getItem() == BrainStone.brainStonePlate())
				|| (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[2 + offset])))))
			return false;
		else if (!((armor[1 + offset].getItem() == BrainStone.brainStoneLeggings())
				|| (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[1 + offset])))))
			return false;
		else if (!((armor[0 + offset].getItem() == BrainStone.brainStoneBoots())
				|| (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[0 + offset])))))
			return false;
		else
			return true;
	}
}