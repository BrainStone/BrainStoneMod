package brainstonemod.common.compat.jei;

import javax.annotation.Nonnull;

import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import net.minecraft.item.ItemStack;

/**
 * @author The_Fireplace
 */
public class CapacitorUpgradeRecipeHandler implements IRecipeHandler<BrainStoneLifeCapacitorUpgrade> {
	@Nonnull
	@Override
	public Class<BrainStoneLifeCapacitorUpgrade> getRecipeClass() {
		return BrainStoneLifeCapacitorUpgrade.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull BrainStoneLifeCapacitorUpgrade recipe) {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull BrainStoneLifeCapacitorUpgrade recipe) {
		return new CapacitorUpgradeRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull BrainStoneLifeCapacitorUpgrade recipe) {
		if (recipe.getRecipeOutput() == null) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
			BSP.error("Recipe has no output. {}", recipeInfo);
			return false;
		}
		int inputCount = 0;
		for (Object input : recipe.getStacks()) {
			if (input instanceof ItemStack) {
				inputCount++;
			} else {
				String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
				BSP.error("Recipe has an input that is not an ItemStack. {}", recipeInfo);
				return false;
			}
		}
		if (inputCount > 9) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
			BSP.error("Recipe has too many inputs. {}", recipeInfo);
			return false;
		}
		return inputCount > 0;
	}
}
