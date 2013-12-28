package brainstonemod.common.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.helper.BSP;
import brainstonemod.network.BrainStonePacketHandler;
import cpw.mods.fml.common.network.Player;

public class BlockPulsatingBrainStone extends BlockBrainStoneBase {
	private final boolean effect;
	private static int hasEffectId, hasNoEffectId;

	public BlockPulsatingBrainStone(int i, boolean effect) {
		super(BrainStone.getId(i), Material.rock);

		this.effect = effect;

		this.setHardness(3.0F);
		this.setResistance(1.0F);
		this.setLightValue(1.0F);

		if (effect) {
			this.setUnlocalizedName("pulsatingBrainStoneEffect");
			hasEffectId = blockID;
		} else {
			this.setUnlocalizedName("pulsatingBrainStone");
			this.setCreativeTab(CreativeTabs.tabBlock);
			hasNoEffectId = blockID;
		}

		blockParticleGravity = (float) MathHelper.getRandomDoubleInRange(
				new Random(), 3.0, -3.0);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z) {
		return new ItemStack(BrainStone.pulsatingBrainStone(), 1);
	}

	private int getRandomPotion(Random random) {
		Potion potion;

		do {
			potion = Potion.potionTypes[random.nextInt(32)];
		} while (potion == null);

		return potion.id;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return hasNoEffectId;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID,
				(int) world.getTotalWorldTime() % this.tickRate(world));
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		final int metaData = (int) ((world.getWorldInfo().getWorldTime() / this
				.tickRate(world)) % 16);

		if (metaData >= 15) {
			if (effect) {
				if (random.nextInt(2) == 0) {
					world.setBlock(x, y, z, hasNoEffectId, 0, 2);
				}
			} else {
				if (random.nextInt(4) == 0) {
					world.setBlock(x, y, z, hasEffectId, 0, 2);
				}
			}
		} else if ((metaData == 8) && (effect)) {
			BSP.finer("Effect Time!");

			double radius;
			int taskRand;
			EntityLivingBase entity;
			Object tmpEntity;
			final List<?> list = world.getEntitiesWithinAABBExcludingEntity(null,
					AxisAlignedBB.getBoundingBox(x - 10, y - 10, z - 10,
							x + 11, y + 11, z + 11));

			final int size = list.size();

			for (int i = 0; i < size; i++) {
				tmpEntity = list.get(i);

				BSP.finer(tmpEntity.getClass().getName());

				if (tmpEntity instanceof EntityPlayer) {
					final ItemStack[] playerArmor = ((EntityPlayer) tmpEntity).inventory.armorInventory;

					if ((playerArmor[3] != null)
							&& (playerArmor[2] != null)
							&& (playerArmor[1] != null)
							&& (playerArmor[0] != null)
							&& (playerArmor[3].itemID == BrainStone
									.brainStoneHelmet().itemID)
							&& (playerArmor[2].itemID == BrainStone
									.brainStonePlate().itemID)
							&& (playerArmor[1].itemID == BrainStone
									.brainStoneLeggings().itemID)
							&& (playerArmor[0].itemID == BrainStone
									.brainStoneBoots().itemID)) {
						BSP.finer("Player wears armor! No effect!");

						continue;
					}
				}

				if (tmpEntity instanceof EntityLivingBase) {
					entity = (EntityLivingBase) tmpEntity;

					final ItemStack[] equipment = entity.getLastActiveItems();

					BSP.finer(entity, Arrays.toString(equipment), "");

					if (((equipment != null) && (equipment.length >= 5))
							&& (((equipment[1] != null) && (equipment[1].itemID == BrainStone
									.brainStoneBoots().itemID))
									&& ((equipment[2] != null) && (equipment[2].itemID == BrainStone
											.brainStoneLeggings().itemID))
									&& ((equipment[3] != null) && (equipment[3].itemID == BrainStone
											.brainStonePlate().itemID)) && ((equipment[4] != null) && (equipment[4].itemID == BrainStone
									.brainStoneHelmet().itemID)))) {
						BSP.finer("Mob wears armor! No effect!");

						continue;
					}

					radius = MathHelper.getRandomDoubleInRange(random, 2.0,
							10.0);

					if (entity.getDistance((x) + 0.5, (y) + 0.5, (z) + 0.5) <= radius) {
						taskRand = random.nextInt(10);

						if ((taskRand >= 0) && (taskRand < 6)) {
							BSP.finer("Potion Effect");

							entity.addPotionEffect(new PotionEffect(this
									.getRandomPotion(random), random
									.nextInt(5980) + 20, random.nextInt(4)));
						} else if ((taskRand >= 6) && (taskRand < 10)) {
							BSP.finer("Kick");

							final double x1 = MathHelper
									.getRandomDoubleInRange(random, -1.5, 1.5);
							final double y1 = MathHelper
									.getRandomDoubleInRange(random, 0.0, 3.0);
							final double z1 = MathHelper
									.getRandomDoubleInRange(random, -1.5, 1.5);

							if (tmpEntity instanceof EntityPlayer) {
								BrainStonePacketHandler
										.sendPlayerUpdateMovementPacket(
												(Player) entity, x1, y1, z1);
							} else {
								entity.addVelocity(x1, y1, z1);
							}
						}
					}
				}
			}
		}

		world.scheduleBlockUpdate(x, y, z, blockID, this.tickRate(world));
		BrainStonePacketHandler.sendReRenderBlockAtPacket(x, y, z, world);
	}
}