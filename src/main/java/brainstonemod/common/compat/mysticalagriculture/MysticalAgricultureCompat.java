package brainstonemod.common.compat.mysticalagriculture;

import com.blakebr0.mysticalagriculture.crafting.ModRecipes;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.IModIntegration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MysticalAgricultureCompat implements IModIntegration {
	public MysticalCropCropHelper brainStone;
	public MysticalCropCropHelper essenceOfLife;

	private static void addSeedRecipe(MysticalCropCropHelper crop, Object material) {
		if (crop.isEnabled()) {
			BrainStone.addRecipe(new ItemStack(crop.getSeed()), "MEM", "ESE", "MEM", 'E',
					ModRecipes.getEssence(crop.getTier()), 'S', ModRecipes.getCraftingSeed(crop.getTier()), 'M',
					material);
		}
	}

	private void addReprocessorRecipe(MysticalCropCropHelper crop) {
		if (crop.isEnabled()) {
			ReprocessorManager.addRecipe(new ItemStack(crop.getCrop(), 2, 0), new ItemStack(crop.getSeed(), 1, 0));
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		brainStone = new MysticalCropCropHelper("brain_stone", 3, true);
		essenceOfLife = new MysticalCropCropHelper("essence_of_life", 5, true);

		brainStone.generate();
		essenceOfLife.generate();

		BrainStone.items.put("essence_of_life_fragment",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneDust(), 1), "EE", "EE", 'E',
				BrainStone.brainStoneEssence());
		BrainStone.addRecipe(new ItemStack(BrainStone.essenceOfLifeFragment(), 1), "EEE", "EEE", "EEE", 'E',
				BrainStone.essenceOfLifeEssence());

		if (!BrainStoneModules.forestry()) {
			BrainStone.addRecipe(new ItemStack(BrainStone.essenceOfLife(), 1), "FF", "FF", 'F',
					BrainStone.essenceOfLifeFragment());
		}

		addSeedRecipe(brainStone, BrainStone.brainStoneDust());
		addSeedRecipe(essenceOfLife, BrainStone.essenceOfLife());

		addReprocessorRecipe(brainStone);
		addReprocessorRecipe(essenceOfLife);
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
