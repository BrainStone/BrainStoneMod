package brainstonemod.common.compat.jei;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import javax.annotation.Nonnull;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BrainstoneJEIPlugin implements IModPlugin {
  private static IJeiHelpers helpers;

  @SuppressWarnings("deprecation")
  public static void reloadJEI() {
    helpers.reload();
  }

  @Override
  public void register(@Nonnull IModRegistry modRegistry) {
    helpers = modRegistry.getJeiHelpers();

    // Hide off variants
    helpers
        .getIngredientBlacklist()
        .addIngredientToBlacklist(new ItemStack(BrainStoneBlocks.pulsatingBrainStoneEffect()));
    helpers
        .getIngredientBlacklist()
        .addIngredientToBlacklist(new ItemStack(BrainStoneBlocks.brainStoneOut()));

    if (!BrainStoneModules.draconicEvolution()) {
      // Register handler
      modRegistry.handleRecipes(
          BrainStoneLifeCapacitorUpgrade.class,
          CapacitorUpgradeRecipeWrapper::new,
          VanillaRecipeCategoryUid.CRAFTING);
    }
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    subtypeRegistry.registerSubtypeInterpreter(
        BrainStoneItems.brainStoneLifeCapacitor(), itemStack -> "");
  }
}
