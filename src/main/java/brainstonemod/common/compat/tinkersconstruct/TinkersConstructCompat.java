package brainstonemod.common.compat.tinkersconstruct;

import brainstonemod.common.compat.IModIntegration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TinkersConstructCompat implements IModIntegration {
	private TinkersContructMaterials materials;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		materials = new TinkersContructMaterials();

		materials.initToolMaterials();
		MinecraftForge.EVENT_BUS.register(materials);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
