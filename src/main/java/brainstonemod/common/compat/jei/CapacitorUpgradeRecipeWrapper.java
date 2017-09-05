package brainstonemod.common.compat.jei;

import java.util.Arrays;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

@RequiredArgsConstructor
public class CapacitorUpgradeRecipeWrapper implements IRecipeWrapper {
	private final BrainStoneLifeCapacitorUpgrade recipe;

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.getStacks()));
		ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
	}
}
