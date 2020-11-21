package brainstonemod.common.compat.draconicevolution;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import com.brandon3055.draconicevolution.api.OreDictHelper;
import com.brandon3055.draconicevolution.api.fusioncrafting.ICraftingInjector;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionCraftingInventory;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrainStoneLifeCapacitorFusionUpgrade extends BrainStoneLifeCapacitorUpgrade
    implements IFusionRecipe {
  public BrainStoneLifeCapacitorFusionUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade upgrade) {
    super(upgrade);
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
          if (i.hasTagCompound()
              && !ItemStack.areItemStackTagsEqual(i, pedestal.getStackInPedestal())) {
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
      if (!pedestal.getStackInPedestal().isEmpty()
          && pedestal.getPedestalTier() < getRecipeTier()) {
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
  public ItemStack getRecipeCatalyst() {
    return getStacks()[0];
  }

  @Override
  public List<?> getRecipeIngredients() {
    return Collections.singletonList(getStacks()[1]);
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
    CAPACITY(BrainStoneItems.essenceOfLife()),
    CHARGING(BrainStoneBlocks.pulsatingBrainStone());

    @Getter private final ItemStack upgrade;

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
