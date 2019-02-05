package brainstonemod.common.compat.industrialforegoing;

import brainstonemod.BrainStone;
import brainstonemod.common.compat.IModIntegration;
import com.buuz135.industrial.api.recipe.LaserDrillEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IndustrialForegoingCompat implements IModIntegration {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		LaserDrillEntry.addOreFile(
				new ResourceLocation(BrainStone.RESOURCE_PACKAGE, "compat/industrial_foregoing_default_ores.json"));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
