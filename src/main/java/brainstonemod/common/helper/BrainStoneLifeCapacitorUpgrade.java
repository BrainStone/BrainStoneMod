package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

public class BrainStoneLifeCapacitorUpgrade implements IRecipe {
	public enum Upgrade {
		CAPACITY(BrainStone.essenceOfLife()), CHARGING(BrainStone.pulsatingBrainStone());

		private Object upgradeItem;

		Upgrade(Object upgradeItem) {
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
				BrainStoneLifeCapacitorUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
	}

	private final Upgrade upgrade;

	public BrainStoneLifeCapacitorUpgrade(Upgrade upgrade) {
		this.upgrade = upgrade;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting items) {
		ItemStack capacitor = null;
		ItemStack slot;

		for (int i = 0; i < items.getSizeInventory(); i++) {
			slot = items.getStackInSlot(i);

			if ((slot != null) && (slot.getItem() == BrainStone.brainStoneLifeCapacitor())) {
				capacitor = slot;
				break;
			}
		}

		ItemStack res = capacitor.copy();

		if (upgrade == Upgrade.CAPACITY)
			res = BrainStone.brainStoneLifeCapacitor().upgradeCapacity(res);
		else if (upgrade == Upgrade.CHARGING)
			res = BrainStone.brainStoneLifeCapacitor().upgradeCharging(res);

		return res;
	}

	@Override
	public boolean matches(InventoryCrafting items, World world) {
		boolean foundCapacitor = false;
		boolean foundUpgrade = false;

		ItemStack slot;
		Item item;

		final Item capacitor = BrainStone.brainStoneLifeCapacitor();
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
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack higherCapacity = new ItemStack(BrainStone.brainStoneLifeCapacitor());
		higherCapacity = BrainStone.brainStoneLifeCapacitor().upgradeCapacity(higherCapacity);
		ItemStack higherCharging = new ItemStack(BrainStone.brainStoneLifeCapacitor());
		higherCharging = BrainStone.brainStoneLifeCapacitor().upgradeCharging(higherCharging);
		if (upgrade == Upgrade.CAPACITY)
			return higherCapacity;
		else {
			return higherCharging;
		}
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return new ItemStack[0];// TODO: Check this
	}

	public ItemStack[] getStacks() {
		return new ItemStack[] { new ItemStack(BrainStone.brainStoneLifeCapacitor()), upgrade.getUpgrade() };
	}
}
