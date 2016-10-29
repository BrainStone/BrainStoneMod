package brainstonemod.common.api.tconstruct;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import brainstonemod.BrainStone;
import lombok.experimental.UtilityClass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

@UtilityClass
public class TinkersContructMaterialBrainStone {
	@Optional.Method(modid = "tconstruct")
	public static void initToolMaterials() {
		// Materials
		Material brainStone = newMaterial("brainstone", 0x33FF57);
		Material stablePulsatingBrainStone = newMaterial("stablepulsatingbrainstone", 0x004d00);

		// Basic traits and properties
		brainStone.setCraftable(true);
		brainStone.addItem(BrainStone.brainStone(), Material.VALUE_Ingot);
		brainStone.setRepresentativeItem(BrainStone.brainStone());
		brainStone.addTrait(magnetic, HEAD);
		brainStone.addTrait(alien, HEAD);
		brainStone.addTrait(alien);

		stablePulsatingBrainStone.setCraftable(true);
		stablePulsatingBrainStone.addItem(BrainStone.stablePulsatingBrainStone(), Material.VALUE_Ingot);
		stablePulsatingBrainStone.setRepresentativeItem(BrainStone.stablePulsatingBrainStone());
		stablePulsatingBrainStone.addTrait(magnetic2, HEAD);
		stablePulsatingBrainStone.addTrait(momentum, HEAD);
		stablePulsatingBrainStone.addTrait(alien, HEAD);
		stablePulsatingBrainStone.addTrait(writable, EXTRA);
		stablePulsatingBrainStone.addTrait(alien, EXTRA);
		stablePulsatingBrainStone.addTrait(unnatural, EXTRA);
		stablePulsatingBrainStone.addTrait(alien, HANDLE);
		stablePulsatingBrainStone.addTrait(unnatural, HANDLE);

		// Stats
		addMaterialStats(brainStone, BrainStone.toolBRAINSTONE, 1.5f);
		TinkerRegistry.addMaterialStats(brainStone, new BowMaterialStats(1.3f, 5.25f));

		addMaterialStats(stablePulsatingBrainStone, BrainStone.toolSTABLEPULSATINGBS, 3.0f);
		TinkerRegistry.addMaterialStats(stablePulsatingBrainStone, new BowMaterialStats(2.0f, 7.9f));

		// Adding the materials
		TinkerRegistry.addMaterial(brainStone);
		TinkerRegistry.addMaterial(stablePulsatingBrainStone);
	}

	private static Material newMaterial(String name, int color) {
		Material mat = new Material(name, color);
		TinkerMaterials.materials.add(mat);
		return mat;
	}

	private static void addMaterialStats(Material material, ToolMaterial toolMaterial, float handleModifier) {
		TinkerRegistry.addMaterialStats(material,
				new HeadMaterialStats(toolMaterial.getMaxUses(), toolMaterial.getEfficiencyOnProperMaterial(),
						toolMaterial.getDamageVsEntity() * 2.0f, toolMaterial.getHarvestLevel()),
				new HandleMaterialStats(handleModifier, toolMaterial.getMaxUses() / 4),
				new ExtraMaterialStats(toolMaterial.getMaxUses() / 4));
	}
}
