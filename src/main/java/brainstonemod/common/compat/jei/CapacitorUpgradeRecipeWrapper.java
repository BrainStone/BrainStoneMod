package brainstonemod.common.compat.jei;

import java.util.Arrays;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

@RequiredArgsConstructor
public class CapacitorUpgradeRecipeWrapper extends BlankRecipeWrapper {
	private final BrainStoneLifeCapacitorUpgrade recipe;

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.getStacks()));
		ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
	}
}
