package brainstonemod.common.api.tconstruct;

import brainstonemod.common.api.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

/**
 * @author The_Fireplace
 */
public class TiConCompat implements IModIntegration {
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
		TinkersContructMaterialBrainStone.initToolMaterials();
	}

	@Override
	public void serverStarting(FMLServerAboutToStartEvent event) {
		// Do nothing
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}
}
