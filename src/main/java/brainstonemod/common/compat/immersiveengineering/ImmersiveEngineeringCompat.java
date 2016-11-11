package brainstonemod.common.compat.immersiveengineering;

import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ImmersiveEngineeringCompat implements IModIntegration {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		ImmersiveEngineeringRecipes.registerImmersiveEngineeringRecipies();
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}
}
