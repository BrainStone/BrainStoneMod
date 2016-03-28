package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BrainStoneLiveCapacitorUpgrade implements IRecipe {
	public static enum Upgrade {
		CAPACITY(BrainStone.essenceOfLive()), CHARGING(BrainStone.pulsatingBrainStone());

		private Object upgradeItem;

		private Upgrade(Object upgradeItem) {
			this.upgradeItem = upgradeItem;
		}

		public ItemStack getUpgrade() {
			if (upgradeItem instanceof Item)
				return new ItemStack((Item) upgradeItem);
			else if (upgradeItem instanceof Block)
				return new ItemStack((Block) upgradeItem);
			else if (upgradeItem instanceof ItemStack)
				return (ItemStack) upgradeItem;
			else
				return null;
		}
	}

	static {
		RecipeSorter.register(BrainStone.RESOURCE_PREFIX + "BrainStoneLiveCapacitorUpgrade",
				BrainStoneLiveCapacitorUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
	}

	private final Upgrade upgrade;

	public BrainStoneLiveCapacitorUpgrade(Upgrade upgrade) {
		this.upgrade = upgrade;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting items) {
		ItemStack capacitor = null;
		ItemStack slot;

		for (int i = 0; i < items.getSizeInventory(); i++) {
			slot = items.getStackInSlot(i);

			if ((slot != null) && (slot.getItem() == BrainStone.brainStoneLiveCapacitor())) {
				capacitor = slot;
				break;
			}
		}

		ItemStack res = capacitor.copy();
		
		if (upgrade == Upgrade.CAPACITY)
			res = BrainStone.brainStoneLiveCapacitor().upgradeCapacity(res);
		else if (upgrade == Upgrade.CHARGING)
			res = BrainStone.brainStoneLiveCapacitor().upgradeCharging(res);

		return res;
	}

	@Override
	public boolean matches(InventoryCrafting items, World world) {
		boolean foundCapacitor = false;
		boolean foundUpgrade = false;

		ItemStack slot;
		Item item;

		final Item capacitor = BrainStone.brainStoneLiveCapacitor();
		final Item upgradeItem = upgrade.getUpgrade().getItem();

		for (int i = 0; i < items.getSizeInventory(); i++) {
			slot = items.getStackInSlot(i);

			if (slot == null)
				continue;

			item = slot.getItem();

			if (item == capacitor) {
				if (foundCapacitor)
					return false;

				foundCapacitor = true;
			} else if (item == upgradeItem) {
				if (foundUpgrade)
					return false;

				foundUpgrade = true;
			} else {
				return false;
			}
		}

		return foundCapacitor && foundUpgrade;
	}

	@Override
	public int getRecipeSize() {
		return 0;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(BrainStone.brainStoneLiveCapacitor());
	}
}
