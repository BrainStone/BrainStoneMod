package brainstonemod.common.compat.mysticalagriculture;

import brainstonemod.common.compat.IModIntegration;
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
		
		brainStone.register();
		essenceOfLife.register();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		//
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
