package brainstonemod.common.compat.forestry;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import net.minecraft.creativetab.CreativeTabs;
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
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BrainStone.addRecipe(new ItemStack(BrainStone.brainStoneDust(), 1), "XX", "XX", 'X',
				BrainStone.brainStoneDustTiny());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addAchievement() {
		// TODO Auto-generated method stub
	}
}
