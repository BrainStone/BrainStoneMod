package brainstonemod.common.compat.jei;

import javax.annotation.Nonnull;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BrainstoneJEIPlugin extends BlankModPlugin {
	private static IJeiHelpers helpers;

	@SuppressWarnings("deprecation")
	public static void reloadJEI() {
		helpers.reload();
	}

	@Override
	public void register(@Nonnull IModRegistry modRegistry) {
		helpers = modRegistry.getJeiHelpers();

		// Hide off variants
		helpers.getIngredientBlacklist()
				.addIngredientToBlacklist(new ItemStack(BrainStone.pulsatingBrainStoneEffect()));
		helpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BrainStone.brainStoneOut()));

		// Register handler
		modRegistry.handleRecipes(BrainStoneLifeCapacitorUpgrade.class, CapacitorUpgradeRecipeWrapper::new,
				VanillaRecipeCategoryUid.CRAFTING);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(BrainStone.brainStoneLifeCapacitor(), itemStack -> "");
	}
}
