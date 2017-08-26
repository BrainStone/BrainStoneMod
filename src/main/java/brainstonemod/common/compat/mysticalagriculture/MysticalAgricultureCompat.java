package brainstonemod.common.compat.mysticalagriculture;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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
	public MysticalAgricultureCropType brainStone;
	public MysticalAgricultureCropType essenceOfLife;

	protected List<MysticalAgricultureCropType> crops = new LinkedList<>();

	protected MysticalAgricultureCropType newCrop(String name, int tier, Object material, boolean enabled) {
		MysticalAgricultureCropType crop = MysticalAgricultureCropType.of(name, tier, material, enabled);
		crops.add(crop);

		return crop;
	}

	protected final void forEachCrop(Consumer<? super MysticalAgricultureCropType> operation) {
		crops.stream().forEach(operation);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		brainStone = newCrop("brain_stone", 3, BrainStone.brainStoneDust(), true);
		essenceOfLife = newCrop("essence_of_life", 5, BrainStone.essenceOfLife(), true);

		forEachCrop(MysticalAgricultureCropType::generate);

		if (!BrainStoneModules.forestry()) {
			BrainStone.items.put("essence_of_life_fragment",
					(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		}
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

		forEachCrop(MysticalAgricultureCropType::addSeedRecipe);
		forEachCrop(MysticalAgricultureCropType::addReprocessorRecipe);
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
