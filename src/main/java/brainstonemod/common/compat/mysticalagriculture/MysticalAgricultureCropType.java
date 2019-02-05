package brainstonemod.common.compat.mysticalagriculture;

import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop;
import com.blakebr0.mysticalagriculture.crafting.ModRecipes;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.items.ItemSeed;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MysticalAgricultureCropType {
	protected static final String CROP = "_crop";
	protected static final String ESSENCE = "_essence";
	protected static final String SEEDS = "_seeds";

	private final String name;
	private final int tier;
	private final Object material;
	private final boolean enabled;
	private final BlockMysticalCrop plant;
	private final ItemBase crop;
	private final ItemSeed seed;

	public static MysticalAgricultureCropType of(String name, int tier, Object material, boolean enabled) {
		BlockMysticalCrop plant = new BlockMysticalCrop(name + CROP);
		ItemBase crop = new ItemBase(name + ESSENCE);
		ItemSeed seed = new ItemSeed(name + SEEDS, plant, tier);

		return new MysticalAgricultureCropType(name, tier, material, enabled, plant, crop, seed);
	}

	public void generate() {
		if (!isEnabled())
			return;

		getCrop().setCreativeTab(MysticalAgriculture.CREATIVE_TAB);
		getPlant().setCrop(getCrop());
		getPlant().setSeed(getSeed());

		BrainStoneBlocks.addBlock(getName() + CROP, getPlant());
		BrainStoneItems.addItem(getName() + ESSENCE, getCrop());
		BrainStoneItems.addItem(getName() + SEEDS, getSeed());
	}

	public void addSeedRecipe() {
		if (!isEnabled())
			return;

		ModRecipes.addShapedRecipe(new ItemStack(getSeed()), "MEM", "ESE", "MEM", 'E', ModRecipes.getEssence(getTier()),
				'S', ModRecipes.getCraftingSeed(getTier()), 'M', getMaterial());
	}

	public void addReprocessorRecipe() {
		if (!isEnabled())
			return;

		ReprocessorManager.addRecipe(new ItemStack(getCrop(), 2, 0), new ItemStack(getSeed(), 1, 0));
	}
}
