package brainstonemod.common.api.enderio;

import brainstonemod.common.api.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

/**
 * @author The_Fireplace
 */
public class EIOCompat implements IModIntegration {
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
		EnderIORecipies.registerEnderIORecipies();
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
