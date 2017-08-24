package brainstonemod.common.compat.jei;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

/**
 * @author The_Fireplace
 */
public class CapacitorUpgradeRecipeWrapper implements ICustomCraftingRecipeWrapper {
	private BrainStoneLifeCapacitorUpgrade recipe;

	public CapacitorUpgradeRecipeWrapper(BrainStoneLifeCapacitorUpgrade recipe) {
		this.recipe = recipe;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		// Do nothing
	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.getStacks()));
		ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IIngredients ingredients) {
		// Do nothing
	}
}
