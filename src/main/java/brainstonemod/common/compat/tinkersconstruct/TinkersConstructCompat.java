package brainstonemod.common.compat.tinkersconstruct;

import brainstonemod.common.compat.IModIntegration;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TinkersConstructCompat implements IModIntegration {
	@Getter
	private TinkersContructMaterials materials;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		materials = new TinkersContructMaterials();

		materials.createToolMaterials();
		materials.addToolMaterialTraits();
		materials.addToolMaterialStats();
		materials.preInitToolMaterials();

		MinecraftForge.EVENT_BUS.register(materials);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		materials.addToolMaterialItems();
		materials.initToolMaterials();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
