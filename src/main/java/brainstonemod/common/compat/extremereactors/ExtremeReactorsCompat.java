package brainstonemod.common.compat.extremereactors;

import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.IModIntegration;
import erogenousbeef.bigreactors.api.registry.Reactants;
import erogenousbeef.bigreactors.api.registry.ReactorConversions;
import erogenousbeef.bigreactors.api.registry.ReactorInterior;
import erogenousbeef.bigreactors.api.registry.TurbineCoil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ExtremeReactorsCompat implements IModIntegration {
	private static final String REACTANT_ESSENCE_OF_LIFE = "essenceoflife";
	private static final String REACTANT_BRAINSTONE = "brainstone";

	private static final String MATERIAL_BRAINSTONE = "brainstone";
	private static final String MATERIAL_STABLE_PULSATING_BRAINSTONE = "stablepulsatingbrainstone";

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Reactants.registerReactant(REACTANT_ESSENCE_OF_LIFE, 0, 0xe3237b);
		Reactants.registerReactant(REACTANT_BRAINSTONE, 1, 0x214d12);

		Reactants.registerSolid(new ItemStack(BrainStoneItems.essenceOfLife()), REACTANT_ESSENCE_OF_LIFE, 250);
		Reactants.registerSolid(new ItemStack(BrainStoneItems.brainStoneDust()), REACTANT_BRAINSTONE, 250);

		ReactorConversions.register(REACTANT_ESSENCE_OF_LIFE, REACTANT_ESSENCE_OF_LIFE, 2.0F, 0.5F);

		TurbineCoil.registerBlock(MATERIAL_BRAINSTONE, 2.25F, 1.0F, 2.1F);
		TurbineCoil.registerBlock(MATERIAL_STABLE_PULSATING_BRAINSTONE, 4.0F, 1.5F, 4.0F);

		ReactorInterior.registerBlock(MATERIAL_BRAINSTONE, 0.61F, 0.9F, 2.1F, 2.3F);
		ReactorInterior.registerBlock(MATERIAL_STABLE_PULSATING_BRAINSTONE, 0.95F, 0.98F, 6.5F, 5.0F);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}
}
