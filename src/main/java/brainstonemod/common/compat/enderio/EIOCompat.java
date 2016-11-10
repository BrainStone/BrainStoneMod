package brainstonemod.common.compat.enderio;

import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
	public void addAchievement() {
		// Do nothing
	}
}
