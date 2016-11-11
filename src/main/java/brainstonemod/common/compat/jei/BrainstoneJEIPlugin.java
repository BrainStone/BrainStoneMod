package brainstonemod.common.compat.jei;

import javax.annotation.Nonnull;

import brainstonemod.BrainStone;
import mezz.jei.JustEnoughItems;
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
	@Override
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		helpers.getItemBlacklist().addItemToBlacklist(new ItemStack(BrainStone.pulsatingBrainStoneEffect()));
		helpers.getItemBlacklist().addItemToBlacklist(new ItemStack(BrainStone.brainStoneOut()));
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
	
	public static void reloadJEI() {
		JustEnoughItems.getProxy().restartJEI();
	}
}
