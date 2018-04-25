package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.IModIntegration;
import com.google.common.collect.ImmutableMap;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import forestry.core.ModuleCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ForestryCompat implements IModIntegration {
	private static void registerCarpenterRecipes() {
		RecipeManagers.carpenterManager.addRecipe(150, ItemStack.EMPTY,
				new ItemStack(BrainStoneItems.essenceOfLifeFragment(), 1), "DD", "DD", 'D',
				BrainStoneItems.essenceOfLifeDust());
	}

	private static void registerCentrifugeRecipes() {
		RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(BrainStoneItems.brainStoneComb()),
				ImmutableMap.of(ModuleCore.items.beeswax.getItemStack(), 1.0f,
						new ItemStack(BrainStoneItems.brainStoneDustTiny()), 0.9f,
						new ItemStack(BrainStoneItems.brainStoneDustTiny()), 0.1f,
						new ItemStack(BrainStoneItems.brainStoneDustTiny()), 0.1f,
						new ItemStack(BrainStoneItems.brainStoneDustTiny()), 0.1f));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BrainStoneItems.addItem("brain_stone_dust_tiny",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		BrainStoneItems.addItem("essence_of_life_dust",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		BrainStoneItems.addItem("essence_of_life_fragment",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		BrainStoneItems.addItem("brain_stone_comb", (new Item()).setCreativeTab(Tabs.tabApiculture));
		BrainStoneItems.addItem("brain_stone_scoop",
				(new ItemBrainStoneScoop(BrainStoneItems.toolBRAINSTONE)).setCreativeTab(Tabs.tabApiculture));
		BrainStoneItems.addItem("stable_pulsating_brain_stone_scoop",
				(new ItemBrainStoneScoop(BrainStoneItems.toolSTABLEPULSATINGBS)).setCreativeTab(Tabs.tabApiculture));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BeeGenes.intiGenes();
		BeeGenes.intiFlowers();
		BeeBranches.initBranches();
		BeeSpecies.initBees();
		registerCentrifugeRecipes();
		registerCarpenterRecipes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
