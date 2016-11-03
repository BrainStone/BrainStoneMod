package brainstonemod.common.api.jei;

import brainstonemod.BrainStone;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

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

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {

	}
}
