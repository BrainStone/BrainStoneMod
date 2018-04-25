package brainstonemod.common.helper;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.advancement.CriterionRegistry;
import brainstonemod.common.compat.BrainStoneModules;
import com.brandon3055.draconicevolution.api.OreDictHelper;
import com.brandon3055.draconicevolution.api.fusioncrafting.ICraftingInjector;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionCraftingInventory;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Optional.Interface(modid = BrainStoneModules.DRACONIC_EVOLUTION_MODID, iface = "com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe")
@RequiredArgsConstructor
public class BrainStoneLifeCapacitorUpgrade extends IForgeRegistryEntry.Impl<IRecipe>
		implements IRecipe, IFusionRecipe {
	private final Upgrade upgrade;

	@Override
	public ItemStack getCraftingResult(InventoryCrafting items) {
		ItemStack capacitor = ItemStack.EMPTY;
		ItemStack slot;

		for (int i = 0; i < items.getSizeInventory(); i++) {
			slot = items.getStackInSlot(i);

			if (!slot.isEmpty() && (slot.getItem() == BrainStoneItems.brainStoneLifeCapacitor())) {
				capacitor = slot;
				break;
			}
		}

		return getRecipeOutput(capacitor);
	}

	@Override
	public ItemStack getRecipeOutput(ItemStack catalyst) {
		if (catalyst.isEmpty())
			return getRecipeOutput();

		return BrainStoneItems.brainStoneLifeCapacitor().upgradeType(catalyst.copy(), upgrade);
	}

	@Override
	public ItemStack getRecipeOutput() {
		final ItemStack baseStack = new ItemStack(BrainStoneItems.brainStoneLifeCapacitor());

		return BrainStoneItems.brainStoneLifeCapacitor().upgradeType(baseStack, upgrade);
	}

	@Override
	public boolean matches(InventoryCrafting items, World world) {
		return matches((IInventory) items, world);
	}

	@Override
	public boolean matches(IFusionCraftingInventory inventory, World world, BlockPos pos) {
		List<ICraftingInjector> pedestals = new ArrayList<ICraftingInjector>();
		pedestals.addAll(inventory.getInjectors());

		// Check the catalyst for this recipe
		if (inventory.getStackInCore(0).isEmpty()
				|| (inventory.getStackInCore(0).getItem() != BrainStoneItems.brainStoneLifeCapacitor())) {
			return false;
		}

		// Check that all of the ingredients are available.
		for (Object ingredient : getRecipeIngredients()) {
			boolean foundIngredient = false;

			for (ICraftingInjector pedestal : pedestals) {
				if (!pedestal.getStackInPedestal().isEmpty()
						&& OreDictHelper.areStacksEqual(ingredient, pedestal.getStackInPedestal())) {
					ItemStack i = OreDictHelper.resolveObject(ingredient);
					if (i.hasTagCompound() && !ItemStack.areItemStackTagsEqual(i, pedestal.getStackInPedestal())) {
						continue;
					}

					foundIngredient = true;
					pedestals.remove(pedestal);
					break;
				}
			}

			if (!foundIngredient) {
				return false;
			}
		}

		// Check that there are no extra items that are not part of the recipe.
		for (ICraftingInjector pedestal : pedestals) {
			if (!pedestal.getStackInPedestal().isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public boolean matches(IInventory items, World world) {
		boolean foundCapacitor = false;
		boolean foundUpgrade = false;

		ItemStack slot;
		Item item;

		final Item capacitor = BrainStoneItems.brainStoneLifeCapacitor();
		final Item upgradeItem = upgrade.getUpgrade().getItem();

		for (int i = 0; i < items.getSizeInventory(); ++i) {
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
	public String canCraft(IFusionCraftingInventory inventory, World world, BlockPos pos) {
		if (!inventory.getStackInCore(1).isEmpty()) {
			return "outputObstructed";
		}

		if (getRecipeOutput(inventory.getStackInCore(0)).isEmpty()) {
			return "de.fusion.upgradeOther." + upgrade.name().toLowerCase();
		}

		List<ICraftingInjector> pedestals = new ArrayList<ICraftingInjector>();
		pedestals.addAll(inventory.getInjectors());

		for (ICraftingInjector pedestal : pedestals) {
			if (!pedestal.getStackInPedestal().isEmpty() && pedestal.getPedestalTier() < getRecipeTier()) {
				return "tierLow";
			}
		}

		return "true";
	}

	@Override
	public boolean isRecipeCatalyst(ItemStack catalyst) {
		return catalyst.getItem() == BrainStoneItems.brainStoneLifeCapacitor();
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) > 2;
	}

	public ItemStack[] getStacks() {
		return new ItemStack[] { new ItemStack(BrainStoneItems.brainStoneLifeCapacitor()), upgrade.getUpgrade() };
	}

	@Override
	public ItemStack getRecipeCatalyst() {
		return getStacks()[0];
	}

	@Override
	public List<?> getRecipeIngredients() {
		return Collections.singletonList(getStacks()[1]);
	}

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		if (!matches(event.craftMatrix, event.player.getEntityWorld()))
			return;

		EntityPlayerMP player;

		if (event.player instanceof EntityPlayerMP) {
			player = (EntityPlayerMP) event.player;
		} else {
			player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.getPlayerByUUID(event.player.getUniqueID());
		}

		int level = BrainStoneItems.brainStoneLifeCapacitor().getTypeLevel(event.crafting, upgrade);

		CriterionRegistry.UPGRADE_BRAIN_STONE_LIFE_CAPACITOR.trigger(player, upgrade, level);
	}

	@Override
	public void craft(IFusionCraftingInventory inventory, World world, BlockPos pos) {
		List<ICraftingInjector> pedestals = new ArrayList<>();
		pedestals.addAll(inventory.getInjectors());

		// Use Ingredients
		for (Object ingredient : getRecipeIngredients()) {
			for (ICraftingInjector pedestal : pedestals) {
				if (!pedestal.getStackInPedestal().isEmpty()
						&& OreDictHelper.areStacksEqual(ingredient, pedestal.getStackInPedestal())
						&& pedestal.getPedestalTier() >= getRecipeTier()) {

					ItemStack stack = pedestal.getStackInPedestal();
					if (stack.getItem().hasContainerItem(stack)) {
						stack = stack.getItem().getContainerItem(stack);
					} else {
						stack.shrink(1);
						if (stack.getCount() <= 0) {
							stack = ItemStack.EMPTY;
						}
					}

					pedestal.setStackInPedestal(stack);
					pedestals.remove(pedestal);
					break;
				}
			}
		}

		ItemStack catalyst = inventory.getStackInCore(0);

		inventory.setStackInCore(0, ItemStack.EMPTY);
		inventory.setStackInCore(1, getRecipeOutput(catalyst));
	}

	@Override
	public void onCraftingTick(IFusionCraftingInventory inventory, World world, BlockPos pos) {
		// Do nothing
	}

	@Override
	public int getRecipeTier() {
		return 2;
	}

	@Override
	public int getEnergyCost() {
		// Same as Awakened upgrades
		return 32000000;
	}

	public static enum Upgrade {
		CAPACITY(BrainStoneItems.essenceOfLife()), CHARGING(BrainStoneBlocks.pulsatingBrainStone());

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
}
