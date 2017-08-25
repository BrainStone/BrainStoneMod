package brainstonemod.common.helper;

import brainstonemod.BrainStone;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

@RequiredArgsConstructor
public class BrainStoneLifeCapacitorUpgrade implements IRecipe {
	public enum Upgrade {
		CAPACITY(BrainStone.essenceOfLife()), CHARGING(BrainStone.pulsatingBrainStone());

		@Getter
		private final ItemStack upgrade;

		private Upgrade(Object upgrade) {
			if (upgrade instanceof Item) {
				this.upgrade = new ItemStack((Item) upgrade);
			} else if (upgrade instanceof Block) {
				this.upgrade = new ItemStack((Block) upgrade);
			} else if (upgrade instanceof ItemStack) {
				this.upgrade = (ItemStack) upgrade;
			} else {
				this.upgrade = ItemStack.EMPTY;
			}
		}
	}

	static {
		RecipeSorter.register(BrainStone.RESOURCE_PREFIX + "BrainStoneLiveCapacitorUpgrade",
				BrainStoneLifeCapacitorUpgrade.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
	}

	private final Upgrade upgrade;

	@Override
	public ItemStack getCraftingResult(InventoryCrafting items) {
		ItemStack capacitor = ItemStack.EMPTY;
		ItemStack slot;

		for (int i = 0; i < items.getSizeInventory(); i++) {
			slot = items.getStackInSlot(i);

			if (!slot.isEmpty() && (slot.getItem() == BrainStone.brainStoneLifeCapacitor())) {
				capacitor = slot;
				break;
			}
		}

		ItemStack res = capacitor.copy();

		if (upgrade == Upgrade.CAPACITY) {
			res = BrainStone.brainStoneLifeCapacitor().upgradeCapacity(res);
		} else if (upgrade == Upgrade.CHARGING) {
			res = BrainStone.brainStoneLifeCapacitor().upgradeCharging(res);
		}

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

			if (slot.isEmpty()) {
				continue;
			}

			item = slot.getItem();

			if (item == capacitor) {
				if (foundCapacitor)
					return false;

				foundCapacitor = true;
			} else if (item == upgradeItem) {
				if (foundUpgrade)
					return false;

				foundUpgrade = true;
			} else
				return false;
		}

		return foundCapacitor && foundUpgrade;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		final ItemStack baseStack = new ItemStack(BrainStone.brainStoneLifeCapacitor());

		if (upgrade == Upgrade.CAPACITY)
			return BrainStone.brainStoneLifeCapacitor().upgradeCapacity(baseStack);
		else if (upgrade == Upgrade.CHARGING)
			return BrainStone.brainStoneLifeCapacitor().upgradeCharging(baseStack);
		else
			return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

	public ItemStack[] getStacks() {
		return new ItemStack[] { new ItemStack(BrainStone.brainStoneLifeCapacitor()), upgrade.getUpgrade() };
	}
}
