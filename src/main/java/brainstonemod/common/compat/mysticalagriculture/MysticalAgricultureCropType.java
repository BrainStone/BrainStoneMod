package brainstonemod.common.compat.mysticalagriculture;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop;
import com.blakebr0.mysticalagriculture.crafting.ModRecipes;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.items.ItemBase;
import com.blakebr0.mysticalagriculture.items.ItemSeed;

import brainstonemod.BrainStone;
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

		getPlant().setCrop(getCrop());
		getPlant().setSeed(getSeed());

		getCrop().setCreativeTab(BrainStone.getCreativeTab(MysticalAgriculture.tabMysticalAgriculture));
		getSeed().setCreativeTab(BrainStone.getCreativeTab(MysticalAgriculture.tabMysticalAgriculture));

		BrainStone.blocks.put(getName() + CROP, getPlant());

		BrainStone.items.put(getName() + ESSENCE, getCrop());
		BrainStone.items.put(getName() + SEEDS, getSeed());
	}

	public void addSeedRecipe() {
		if (!isEnabled())
			return;

		BrainStone.addRecipe(new ItemStack(getSeed()), "MEM", "ESE", "MEM", 'E', ModRecipes.getEssence(getTier()), 'S',
				ModRecipes.getCraftingSeed(getTier()), 'M', material);
	}

	public void addReprocessorRecipe() {
		if (!isEnabled())
			return;

		ReprocessorManager.addRecipe(new ItemStack(getCrop(), 2, 0), new ItemStack(getSeed(), 1, 0));
	}
}
