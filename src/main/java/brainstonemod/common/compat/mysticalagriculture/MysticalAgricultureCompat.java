package brainstonemod.common.compat.mysticalagriculture;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MysticalAgricultureCompat implements IModIntegration {
	public MysticalCropCropHelper brainStone;
	public MysticalCropCropHelper essenceOfLife;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		brainStone = new MysticalCropCropHelper("brain_stone", 3, true);
		essenceOfLife = new MysticalCropCropHelper("essence_of_life", 5, true); 
		
		brainStone.generate();
		essenceOfLife.generate();
		
		BrainStone.items.put("essence_of_life_fragment",
				(new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		BrainStone.addRecipe(new ItemStack(BrainStone.essenceOfLife(), 1), "FF", "FF", 'F',
				BrainStone.essenceOfLifeFragment());
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
