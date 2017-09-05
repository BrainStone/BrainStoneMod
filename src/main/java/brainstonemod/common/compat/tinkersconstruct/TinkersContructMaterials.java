package brainstonemod.common.compat.tinkersconstruct;

import static slimeknights.tconstruct.library.materials.MaterialTypes.EXTRA;
import static slimeknights.tconstruct.library.materials.MaterialTypes.HANDLE;
import static slimeknights.tconstruct.library.materials.MaterialTypes.HEAD;
import static slimeknights.tconstruct.tools.TinkerTraits.alien;
import static slimeknights.tconstruct.tools.TinkerTraits.magnetic;
import static slimeknights.tconstruct.tools.TinkerTraits.magnetic2;
import static slimeknights.tconstruct.tools.TinkerTraits.momentum;
import static slimeknights.tconstruct.tools.TinkerTraits.unnatural;
import static slimeknights.tconstruct.tools.TinkerTraits.writable;

import java.util.Arrays;
import java.util.List;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import lombok.Getter;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerMaterials;

public class TinkersContructMaterials {
	@Getter
	private Material brainStone;
	@Getter
	private Material stablePulsatingBrainStone;
	private List<MaterialIntegration> materiaIntegrations;

	public void initToolMaterials() {
		// Materials
		brainStone = newMaterial("brainstone", 0x33FF57);
		stablePulsatingBrainStone = newMaterial("stablepulsatingbrainstone", 0x004d00);

		// Basic traits and properties
		brainStone.setCraftable(true);
		brainStone.addItem(BrainStoneBlocks.brainStone(), Material.VALUE_Ingot);
		brainStone.setRepresentativeItem(BrainStoneBlocks.brainStone());
		brainStone.addTrait(magnetic, HEAD);
		brainStone.addTrait(alien, HEAD);
		brainStone.addTrait(alien);

		stablePulsatingBrainStone.setCraftable(true);
		stablePulsatingBrainStone.addItem(BrainStoneBlocks.stablePulsatingBrainStone(), Material.VALUE_Ingot);
		stablePulsatingBrainStone.setRepresentativeItem(BrainStoneBlocks.stablePulsatingBrainStone());
		stablePulsatingBrainStone.addTrait(magnetic2, HEAD);
		stablePulsatingBrainStone.addTrait(momentum, HEAD);
		stablePulsatingBrainStone.addTrait(alien, HEAD);
		stablePulsatingBrainStone.addTrait(writable, EXTRA);
		stablePulsatingBrainStone.addTrait(alien, EXTRA);
		stablePulsatingBrainStone.addTrait(unnatural, EXTRA);
		stablePulsatingBrainStone.addTrait(alien, HANDLE);
		stablePulsatingBrainStone.addTrait(unnatural, HANDLE);

		// Stats
		addMaterialStats(brainStone, BrainStoneItems.toolBRAINSTONE, 1.5f);
		TinkerRegistry.addMaterialStats(brainStone, new BowMaterialStats(1.3f, 5.25f, 1.0f));

		addMaterialStats(stablePulsatingBrainStone, BrainStoneItems.toolSTABLEPULSATINGBS, 3.0f);
		TinkerRegistry.addMaterialStats(stablePulsatingBrainStone, new BowMaterialStats(2.0f, 7.9f, 2.0f));

		// Adding the materials
		materiaIntegrations = Arrays.asList(new MaterialIntegration(brainStone),
				new MaterialIntegration(stablePulsatingBrainStone));

		for (MaterialIntegration materialIntegration : materiaIntegrations) {
			materialIntegration.toolforge();
			materialIntegration.integrateRecipes();
		}
	}

	private static Material newMaterial(String name, int color) {
		Material mat = new Material(name, color);
		TinkerMaterials.materials.add(mat);
		return mat;
	}

	private static void addMaterialStats(Material material, ToolMaterial toolMaterial, float handleModifier) {
		TinkerRegistry.addMaterialStats(material,
				new HeadMaterialStats(toolMaterial.getMaxUses(), toolMaterial.getEfficiencyOnProperMaterial(),
						toolMaterial.getDamageVsEntity() + 2.0f, toolMaterial.getHarvestLevel()),
				new HandleMaterialStats(handleModifier, toolMaterial.getMaxUses() / 4),
				new ExtraMaterialStats(toolMaterial.getMaxUses() / 4));
	}

	@SubscribeEvent
	public void registerRecipes(Register<IRecipe> event) {
		IForgeRegistry<IRecipe> registry = event.getRegistry();

		for (MaterialIntegration materialIntegration : materiaIntegrations) {
			materialIntegration.registerToolForgeRecipe(registry);
		}
	}
}
