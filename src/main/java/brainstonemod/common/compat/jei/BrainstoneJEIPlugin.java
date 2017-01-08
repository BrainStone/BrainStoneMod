package brainstonemod.common.compat.jei;

import javax.annotation.Nonnull;

import brainstonemod.BrainStone;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BrainstoneJEIPlugin implements IModPlugin {
	private static IJeiHelpers helpers;

	@Override
	public void register(@Nonnull IModRegistry registry) {
		helpers = registry.getJeiHelpers();
		helpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BrainStone.pulsatingBrainStoneEffect()));
		helpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BrainStone.brainStoneOut()));
		registry.addRecipeHandlers(new CapacitorUpgradeRecipeHandler());
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
		// Do nothing
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		// Do nothing
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
		// Do nothing
	}

	@SuppressWarnings("deprecation")
	public static void reloadJEI() {
		helpers.reload();
	}
}
