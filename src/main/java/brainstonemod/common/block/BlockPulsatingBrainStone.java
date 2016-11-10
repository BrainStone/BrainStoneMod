package brainstonemod.common.block;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import brainstonemod.BrainStone;
import brainstonemod.common.block.template.BlockBrainStoneBase;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.enderio.BrainStoneUpgrade;
import brainstonemod.common.compat.overlord.IOverlordCompat;
import brainstonemod.common.compat.overlord.OverlordCompat;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.item.ItemArmorBrainStone;
import brainstonemod.network.BrainStonePacketHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class BlockPulsatingBrainStone extends BlockBrainStoneBase {
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

		blockParticleGravity = (float) MathHelper.getRandomDoubleInRange(new Random(), -3.0, 3.0);
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BrainStone.pulsatingBrainStone());
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
		worldIn.scheduleBlockUpdate(pos, this, (int) worldIn.getTotalWorldTime() % tickRate(worldIn), 0);//TODO: Check that priority
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
			BSP.debug("", "Effect Time!");

			double radius;
			int taskRand;
			EntityLivingBase entity;
			Object tmpEntity;
			final List<?> list = world.getEntitiesWithinAABBExcludingEntity(null,
					new AxisAlignedBB(pos.add(-10,-10,-10), pos.add(11,11,11)));

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

					if(BrainStoneModules.overlord()){
						IOverlordCompat compat = new OverlordCompat();
						if(compat.exemptEntity(entity)) {
							BSP.debug("Army Member has Pulsating BrainStone Augment! No effect!");
							continue;
						}
					}

					radius = MathHelper.getRandomDoubleInRange(random, 2.0, 10.0);

					if (entity.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= radius) {
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
								BrainStonePacketHelper.sendPlayerUpdateMovementPacket((EntityPlayer) entity, x1, y1, z1);
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

		world.scheduleBlockUpdate(pos, this, tickRate(world), 0);//TODO: Check that priority
	}

	public static boolean isProtected(Iterable<ItemStack> armors) {
		ItemStack[] armor = new ItemStack[]{};

		for(ItemStack armorStack:armors){
			armor = ArrayUtils.add(armor, armorStack);
		}

        int offset = 0;

        if (armor.length < 4)
            return false;
        else if (armor.length == 5)
            offset = 1;
        else if (armor.length > 5)
            return false;

        boolean allArmorSlotsFilled = (armor[3 + offset] != null) && (armor[2 + offset] != null)
                && (armor[1 + offset] != null) && (armor[offset] != null);

        if (!allArmorSlotsFilled)
            return false;

        boolean enderIOEnabled = Loader.isModLoaded("EnderIO");

        return ((armor[3 + offset].getItem() instanceof ItemArmorBrainStone)
                || (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[3 + offset])))) && ((armor[2 + offset].getItem() instanceof ItemArmorBrainStone) || (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[2 + offset])))) && ((armor[1 + offset].getItem() instanceof ItemArmorBrainStone) || (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[1 + offset])))) && ((armor[offset].getItem() instanceof ItemArmorBrainStone) || (enderIOEnabled && (BrainStoneUpgrade.UPGRADE.hasUpgrade(armor[0 + offset]))));
    }
}