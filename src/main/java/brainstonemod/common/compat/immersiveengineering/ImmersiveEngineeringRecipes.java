package brainstonemod.common.compat.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@UtilityClass
public class ImmersiveEngineeringRecipes {
  @Optional.Method(modid = BrainStoneModules.IMMERSIVE_ENGINEERING_MODID)
  public static void registerImmersiveEngineeringRecipies() {
    final ItemStack brainStone = new ItemStack(BrainStoneBlocks.brainStone());
    final ItemStack dirtyBrainStone = new ItemStack(BrainStoneBlocks.dirtyBrainStone());
    final ItemStack brainStoneOre = new ItemStack(BrainStoneBlocks.brainStoneOre());
    final ItemStack brainStoneDust = new ItemStack(BrainStoneItems.brainStoneDust());
    final ItemStack brainStoneDust4 = new ItemStack(BrainStoneItems.brainStoneDust(), 4);
    final ItemStack slag = ImmersiveEngineeringItems.getSlag();

    // Crusher

    // Remove default recipe
    CrusherRecipe.removeRecipesForOutput(brainStoneDust);

    // BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
    addCrusherRecipe(brainStoneDust, brainStoneOre, 4000, brainStoneDust, 0.5f);
    // BrainStone => 4xBrainStoneDust
    addCrusherRecipe(brainStoneDust4, brainStone, 5000);
    // DirtyBrainStone => 4xBrainStoneDust
    addCrusherRecipe(brainStoneDust4, dirtyBrainStone, 4000);

    // Arc Furnace

    // 4xBrainStoneDust => BrainStone
    addArcFurnaceRecipe(brainStone, brainStoneDust4, slag, 250);
    // DirtyBrainStone => BrainStone
    addArcFurnaceRecipe(brainStone, dirtyBrainStone, slag, 200);
  }

  private static void addCrusherRecipe(
      ItemStack output, Object input, int energy, Object... secondaryOutputs) {
    CrusherRecipe recipe = new CrusherRecipe(output, input, energy);
    recipe.addToSecondaryOutput(secondaryOutputs);

    CrusherRecipe.recipeList.add(recipe);
  }

  private static void addArcFurnaceRecipe(
      ItemStack output, Object input, ItemStack slag, int time, Object... additives) {
    ArcFurnaceRecipe.addRecipe(output, input, slag, time, 512, additives);
  }
}
