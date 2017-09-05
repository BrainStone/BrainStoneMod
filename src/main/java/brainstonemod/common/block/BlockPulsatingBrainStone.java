package brainstonemod.common.block;

import java.util.List;
import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.overlord.IOverlordCompat;
import brainstonemod.common.compat.overlord.OverlordCompat;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.item.ItemArmorBrainStone;
import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockPulsatingBrainStone extends Block {
	private final boolean effect;
	private static Block hasEffectBlock, hasNoEffectBlock;
	private static long lastGravityChange = 0;

	public BlockPulsatingBrainStone(boolean effect) {
		super(Material.ROCK);

		this.effect = effect;

		setHardness(3.0F);
		setResistance(1.0F);
		setLightLevel(1.0F);
		setHarvestLevel("pickaxe", 2);

		if (effect) {
			hasEffectBlock = this;
		} else {
			setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS));
			hasNoEffectBlock = this;
		}

		blockParticleGravity = (float) MathHelper.nextDouble(new Random(), -3.0, 3.0);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(hasNoEffectBlock);
	}

	@Override
	public int damageDropped(IBlockState state) {
		if (effect)
			return 0;
		else
			return getMetaFromState(state) & 0x01;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(BrainStoneBlocks.pulsatingBrainStone());
	}

	public static Potion getRandomPotion(Random random) {
		Potion potion;

		do {
			potion = Potion.getPotionById(random.nextInt(Potion.REGISTRY.getKeys().size()));
		} while (potion == null);

		return potion;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		worldIn.scheduleUpdate(pos, this, (int) worldIn.getTotalWorldTime() % tickRate(worldIn));
	}

	@Override
	public int tickRate(World par1World) {
		return 2;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		final int metaData = (int) ((world.getTotalWorldTime() / tickRate(world)) % 16);

		if (metaData >= 15) {
			if (effect) {
				if (random.nextInt(2) == 0) {
					world.setBlockState(pos, hasNoEffectBlock.getDefaultState(), 2);
				}
			} else {
				if (random.nextInt(4) == 0) {
					world.setBlockState(pos, hasEffectBlock.getDefaultState(), 2);
				}
			}
		} else if ((metaData == 8) && (effect)) {
			applyEffect(world, pos, random);
		}

		if (world.getTotalWorldTime() > lastGravityChange) {
			blockParticleGravity = (float) MathHelper.nextDouble(random, -3.0, 3.0);

			lastGravityChange = world.getTotalWorldTime();
		}

		world.scheduleUpdate(pos, this, tickRate(world));
	}

	public static void applyEffect(World world, BlockPos pos, Random random) {
		BSP.debug("", "Effect Time!");

		double radius;
		int taskRand;
		EntityLivingBase entity;
		Object tmpEntity;
		final List<?> list = world.getEntitiesWithinAABBExcludingEntity(null,
				new AxisAlignedBB(pos.add(-10, -10, -10), pos.add(11, 11, 11)));

		for (Object aList : list) {
			tmpEntity = aList;

			BSP.debug(tmpEntity.getClass().getName());

			if (tmpEntity instanceof EntityLivingBase) {
				entity = (EntityLivingBase) tmpEntity;

				BSP.debug(entity, entity.getArmorInventoryList());

				if (isProtected(entity.getArmorInventoryList())) {
					BSP.debug("Mob/Player wears armor! No effect!");

					continue;
				}

				if (BrainStoneModules.overlord()) {
					IOverlordCompat compat = new OverlordCompat();
					if (compat.exemptEntity(entity)) {
						BSP.debug("Army Member has Pulsating BrainStone Augment! No effect!");
						continue;
					}
				}

				radius = MathHelper.nextDouble(random, 2.0, 10.0);

				if (entity.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= radius) {
					taskRand = random.nextInt(10);

					if ((taskRand >= 0) && (taskRand < 6)) {
						BSP.debug("Potion Effect");

						entity.addPotionEffect(new PotionEffect(getRandomPotion(random), random.nextInt(5980) + 20,
								random.nextInt(4)));
					} else if ((taskRand >= 6) && (taskRand < 10)) {
						kickEntity(entity, random, 3.0);
					}
				}
			}
		}
	}

	public static void kickEntity(Entity entity, Random random, double power) {
		BSP.debug("Kick");

		final double x1 = MathHelper.nextDouble(random, -(power / 2.0), (power / 2.0));
		final double y1 = MathHelper.nextDouble(random, 0.0, power);
		final double z1 = MathHelper.nextDouble(random, -(power / 2.0), (power / 2.0));

		if (entity instanceof EntityPlayer) {
			BrainStonePacketHelper.sendPlayerUpdateMovementPacket((EntityPlayer) entity, x1, y1, z1);
		} else {
			entity.addVelocity(x1, y1, z1);
		}
	}

	public static boolean isProtected(Iterable<ItemStack> armorStacks) {
		NonNullList<ItemStack> armor = NonNullList.create();
		armorStacks.forEach(armor::add);

		int offset = 0;

		if (armor.size() < 4)
			return false;
		else if (armor.size() == 5) {
			offset = 1;
		} else if (armor.size() > 5)
			return false;

		if (armor.stream().skip(offset).anyMatch(ItemStack::isEmpty))
			return false;

		// Old code
		// final boolean enderIOEnabled = BrainStoneModules.enderIO();
		//
		// return armor.stream().skip(offset).allMatch(stack -> (stack.getItem()
		// instanceof ItemArmorBrainStone) || (enderIOEnabled &&
		// EnderIOArmorHelper.isProtected(stack)));

		return armor.stream().skip(offset).allMatch(stack -> stack.getItem() instanceof ItemArmorBrainStone);
	}
}