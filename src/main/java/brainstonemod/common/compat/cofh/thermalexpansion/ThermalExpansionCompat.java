package brainstonemod.common.compat.cofh.thermalexpansion;

import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ThermalExpansionCompat implements IModIntegration {
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
		// TODO: Switch back to init when fix is out.
		// Also remove the remove then
		ThermalExpansionRecipes.registerThermalExpansionRecipies();
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}
}
