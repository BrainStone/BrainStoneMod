package brainstonemod.common.compat.cofh.thermalexpansion;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import cofh.api.util.ThermalExpansionHelper;
import lombok.experimental.UtilityClass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@UtilityClass
public class ThermalExpansionRecipes {
	@Optional.Method(modid = BrainStoneModules.THERMAL_EXPANSION_MODID)
	public static void registerThermalExpansionRecipies() {
		final ItemStack brainStone = new ItemStack(BrainStone.brainStone());
		final ItemStack dirtyBrainStone = new ItemStack(BrainStone.dirtyBrainStone());
		final ItemStack brainStoneOre = new ItemStack(BrainStone.brainStoneOre());
		final ItemStack brainStoneDust = new ItemStack(BrainStone.brainStoneDust());
		final ItemStack brainStoneDust4 = new ItemStack(BrainStone.brainStoneDust(), 4);
		final ItemStack slag = ThermalExpansionItems.getSlag();
		final ItemStack sand = new ItemStack(Blocks.SAND);

		// Pulverizer

		// Remove default recipe
		ThermalExpansionHelper.removePulverizerRecipe(brainStoneOre);

		// BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
		ThermalExpansionHelper.addPulverizerRecipe(4000, brainStoneOre, brainStoneDust, brainStoneDust, 50);
		// BrainStone => 4xBrainStoneDust
		ThermalExpansionHelper.addPulverizerRecipe(5000, brainStone, brainStoneDust4);
		// DirtyBrainStone => 4xBrainStoneDust
		ThermalExpansionHelper.addPulverizerRecipe(4000, dirtyBrainStone, brainStoneDust4);

		// Induction Smelter

		// 4xBrainStoneDust => BrainStone
		ThermalExpansionHelper.addSmelterRecipe(2500, brainStoneDust4, sand, brainStone, slag);
		// DirtyBrainStone => BrainStone
		ThermalExpansionHelper.addSmelterRecipe(2000, dirtyBrainStone, sand, brainStone, slag);
	}
}
