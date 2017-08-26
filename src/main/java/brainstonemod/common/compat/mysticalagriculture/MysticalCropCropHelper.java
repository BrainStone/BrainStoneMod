package brainstonemod.common.compat.mysticalagriculture;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop;
import com.blakebr0.mysticalagriculture.items.ItemBase;
import com.blakebr0.mysticalagriculture.items.ItemSeed;

import brainstonemod.BrainStone;
import lombok.Getter;

@Getter
public class MysticalCropCropHelper {
	private static final String CROP = "_crop";
	private static final String ESSENCE = "_essence";
	private static final String SEEDS = "_seeds";

	private final String name;
	private final int tier;
	private final boolean enabled;
	private final BlockMysticalCrop plant;
	private final ItemBase crop;
	private final ItemSeed seed;

	public MysticalCropCropHelper(String name, int tier, boolean enabled) {
		this.name = name;
		this.tier = tier;
		this.enabled = enabled;
		plant = new BlockMysticalCrop(getName() + CROP);
		crop = new ItemBase(getName() + ESSENCE);
		seed = new ItemSeed(getName() + SEEDS, getPlant(), tier);
	}

	public void register() {
		if (!isEnabled())
			return;

		getPlant().setCrop(getCrop());
		getPlant().setSeed(getSeed());

		BrainStone.blocks.put(getName() + CROP, getPlant());

		BrainStone.items.put(getName() + ESSENCE, getCrop());
		BrainStone.items.put(getName() + SEEDS, getSeed());
	}
}
