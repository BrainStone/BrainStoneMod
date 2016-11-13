package brainstonemod.common.compat.betterachievements;

import betterachievements.api.util.IMCHelper;
import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class BetterAchievementsCompat implements IModIntegration {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do Nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		IMCHelper.sendIconForPage("Brain Stone Mod", BrainStone.brainStone());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do Nothing
	}

	@Override
	public void addAchievement() {
		// Do Nothing
	}
}
