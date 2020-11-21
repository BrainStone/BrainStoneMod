package brainstonemod.common.compat.jei;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

@RequiredArgsConstructor
public class CapacitorUpgradeRecipeWrapper implements IRecipeWrapper {
  private final BrainStoneLifeCapacitorUpgrade recipe;

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.getStacks()));
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }
}
