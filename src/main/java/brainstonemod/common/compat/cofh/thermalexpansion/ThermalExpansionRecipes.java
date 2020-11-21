package brainstonemod.common.compat.cofh.thermalexpansion;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.cofh.thermalfoundation.ThermalFoundationItems;
import cofh.api.util.ThermalExpansionHelper;
import lombok.experimental.UtilityClass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@UtilityClass
public class ThermalExpansionRecipes {
  @Optional.Method(modid = BrainStoneModules.THERMAL_EXPANSION_MODID)
  public static void registerThermalExpansionRecipies() {
    final ItemStack brainStone = new ItemStack(BrainStoneBlocks.brainStone());
    final ItemStack dirtyBrainStone = new ItemStack(BrainStoneBlocks.dirtyBrainStone());
    final ItemStack brainStoneOre = new ItemStack(BrainStoneBlocks.brainStoneOre());
    final ItemStack brainStoneDust = new ItemStack(BrainStoneItems.brainStoneDust());
    final ItemStack brainStoneDust4 = new ItemStack(BrainStoneItems.brainStoneDust(), 4);
    final ItemStack slag = ThermalFoundationItems.getSlag();
    final ItemStack sand = new ItemStack(Blocks.SAND);

    // Pulverizer

    // BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
    ThermalExpansionHelper.addPulverizerRecipe(
        4000, brainStoneOre, brainStoneDust, brainStoneDust, 50);
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
