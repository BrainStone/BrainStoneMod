package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ForestryCompat implements IModIntegration {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BrainStone.items.put("brain_stone_dust_tiny",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
		BrainStone.items.put("brain_stone_scoop", (new ItemBrainStoneScoop(BrainStone.toolBRAINSTONE)));
		BrainStone.items.put("stable_pulsating_brain_stone_scoop",
				(new ItemBrainStoneScoop(BrainStone.toolSTABLEPULSATINGBS)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneDust(), 1), "XX", "XX", 'X',
				BrainStone.brainStoneDustTiny());
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneScoop(), 1), "SWS", "SBS", " S ", 'S', Items.STICK, 'W',
				Blocks.WOOL, 'B', BrainStone.brainStone());
		BrainStone.addRecipe(new ItemStack(BrainStone.stablePulsatingBrainStoneScoop(), 1), "SWS", "SBS", " R ", 'S',
				Items.STICK, 'W', Blocks.WOOL, 'B', BrainStone.stablePulsatingBrainStone(), 'R', Blocks.REDSTONE_BLOCK);
		
		BeeBranches.initBranches();
		BeeSpecies.initBees();
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
