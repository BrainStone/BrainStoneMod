package brainstonemod.common.api.jer;

import brainstonemod.BrainStone;
import brainstonemod.common.api.IModIntegration;
import jeresources.api.IJERAPI;
import jeresources.api.JERPlugin;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class JERCompat implements IModIntegration {
	@JERPlugin
	public static IJERAPI jerAPI;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// jerAPI.getMobRegistry().register(new EntityWither(new
		// FakeClientWorld()),
		// new LootDrop(new ItemStack(BrainStone.essenceOfLife()),
		// (float) BrainStoneConfigWrapper.getEssenceOfLifeBaseChance()));

		ItemStack brainStoneOre = new ItemStack(BrainStone.brainStoneOre());
		ItemStack brainStoneDust = new ItemStack(BrainStone.brainStoneDust());

		jerAPI.getWorldGenRegistry().register(brainStoneOre, new DistributionSquare(1, 20, 0, 32),
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
