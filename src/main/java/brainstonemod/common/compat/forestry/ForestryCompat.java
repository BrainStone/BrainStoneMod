package brainstonemod.common.compat.forestry;

import com.google.common.collect.ImmutableMap;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import forestry.core.PluginCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ForestryCompat implements IModIntegration {
	private static void registerRecipes() {
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneDust(), 1), "XX", "XX", 'X',
				BrainStone.brainStoneDustTiny());
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneScoop(), 1), "SWS", "SBS", " S ", 'S', Items.STICK, 'W',
				Blocks.WOOL, 'B', BrainStone.brainStone());
		BrainStone.addRecipe(new ItemStack(BrainStone.stablePulsatingBrainStoneScoop(), 1), "SWS", "SBS", " R ", 'S',
				Items.STICK, 'W', Blocks.WOOL, 'B', BrainStone.stablePulsatingBrainStone(), 'R', Blocks.REDSTONE_BLOCK);
	}

	private static void registerCarpenterRecipes() {
		// Nothing yet!
	}

	private static void registerCentrifugeRecipes() {
		RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(BrainStone.brainStoneComb()),
				ImmutableMap.of(PluginCore.items.beeswax.getItemStack(), 1.0f,
						new ItemStack(BrainStone.brainStoneDustTiny()), 0.9f, null, 1.0f));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BrainStone.items.put("brain_stone_dust_tiny",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		BrainStone.items.put("brain_stone_comb",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(Tabs.tabApiculture)));
		BrainStone.items.put("brain_stone_scoop", (new ItemBrainStoneScoop(BrainStone.toolBRAINSTONE)));
		BrainStone.items.put("stable_pulsating_brain_stone_scoop",
				(new ItemBrainStoneScoop(BrainStone.toolSTABLEPULSATINGBS)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BeeGenes.intiGenes();
		BeeGenes.intiFlowers();
		BeeBranches.initBranches();
		BeeSpecies.initBees();
		registerRecipes();
		registerCentrifugeRecipes();
		registerCarpenterRecipes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}
}
