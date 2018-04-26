package brainstonemod.common.compat.jeresources;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.IModIntegration;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.helper.BSP;
import jeresources.api.IJERAPI;
import jeresources.api.JERPlugin;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class JEResourcesCompat implements IModIntegration {
	@JERPlugin
	public static IJERAPI jerAPI;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		final World world = jerAPI.getWorld();
		final ItemStack essenceOfLife = new ItemStack(BrainStoneItems.essenceOfLife());
		final ItemStack brainStoneOre = new ItemStack(BrainStoneBlocks.brainStoneOre());
		final ItemStack brainStoneDust = new ItemStack(BrainStoneItems.brainStoneDust());

		final LootDrop[] drops = new LootDrop[] { new LootDrop(brainStoneDust, 0, 1, 1f / 2f, 0),
				new LootDrop(brainStoneDust, 0, 2, 2f / 3f, 1), new LootDrop(brainStoneDust, 0, 3, 3f / 4f, 2),
				new LootDrop(brainStoneDust, 0, 4, 4f / 5f, 3) };

		jerAPI.getMobRegistry().register(new EntityWither(world),
				new LootDrop(essenceOfLife, (float) BrainStoneConfigWrapper.getEssenceOfLifeBaseChance()));

		for (int dim : BrainStoneConfigWrapper.getBrainStoneOreDims()) {
			try {
				jerAPI.getWorldGenRegistry().register(brainStoneOre,
						new DistributionSquare(BrainStoneConfigWrapper.getBrainStoneOreVeinCount(),
								BrainStoneConfigWrapper.getBrainStoneOreVeinSize(), 0, 32),
						new Restriction(new DimensionRestriction(dim)), true, drops);
			} catch (IllegalArgumentException e) {
				BSP.warn("JER doesn't know about DIM" + dim);
			}
		}

		jerAPI.getDungeonRegistry().registerCategory("chests/house/top", "BrainStone House Top");
		jerAPI.getDungeonRegistry().registerChest("chests/house/top",
				new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "chests/house/top"));
		jerAPI.getDungeonRegistry().registerCategory("chests/house/bottom", "BrainStone House Bottom");
		jerAPI.getDungeonRegistry().registerChest("chests/house/bottom",
				new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "chests/house/bottom"));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
