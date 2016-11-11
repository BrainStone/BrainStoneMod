package brainstonemod.common.compat.jeresources;

import brainstonemod.BrainStone;
import brainstonemod.client.config.BrainStoneClientConfigWrapper;
import brainstonemod.common.compat.IModIntegration;
import jeresources.api.IJERAPI;
import jeresources.api.JERPlugin;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import mezz.jei.util.FakeClientWorld;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.ItemStack;
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
		final World world = FakeClientWorld.getInstance();
		final ItemStack essenceOfLife = new ItemStack(BrainStone.essenceOfLife());
		final ItemStack brainStoneOre = new ItemStack(BrainStone.brainStoneOre());
		final ItemStack brainStoneDust = new ItemStack(BrainStone.brainStoneDust());

		jerAPI.getMobRegistry().register(new EntityWither(world),
				new LootDrop(essenceOfLife, (float) BrainStoneClientConfigWrapper.getEssenceOfLifeBaseChance()));

		jerAPI.getWorldGenRegistry().register(brainStoneOre,
				new DistributionSquare(BrainStoneClientConfigWrapper.getBrainStoneOreVeinCount(),
						BrainStoneClientConfigWrapper.getBrainStoneOreVeinSize(), 0, 32),
				new LootDrop(brainStoneDust, 0, 1, 1.0f, 0), new LootDrop(brainStoneDust, 0, 2, 1.0f, 1),
				new LootDrop(brainStoneDust, 0, 3, 1.0f, 2), new LootDrop(brainStoneDust, 0, 4, 1.0f, 3),
				new LootDrop(brainStoneOre, 1, 1, Conditional.silkTouch));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}
}
