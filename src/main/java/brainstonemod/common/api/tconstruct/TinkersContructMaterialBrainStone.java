package brainstonemod.common.api.tconstruct;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;

public class TinkersContructMaterialBrainStone {
	@Optional.Method(modid = "TConstruct")
	public static void initToolMaterials() {
		BSP.info("Tinker's Construct detected! Registering BrainStone materials as tool materials!");

		addMaterial(1546, "tinker", "brainstone", "BrainStone", Item.getItemFromBlock(BrainStone.brainStone()), 4, 5350,
				600, 5, 1.5F, 1, 0f, TextFormatting.GREEN.toString(), 0x33FF57, 30, 5.25f, 1.4F, 0.0F);
		addMaterial(1547, "tinker", "stablepulsatingbrainstone", "StablePulsatingBrainStone",
				Item.getItemFromBlock(BrainStone.stablePulsatingBrainStone()), 5, 21472, 1800, 20, 3.0F, 2, 0f,
				TextFormatting.DARK_GREEN.toString(), 0x004d00, 45, 8.0f, 4.2F, 0.0F);
	}

	private static void addMaterial(int materialID, String domain, String renderName, String materialName,
			Item material, int harvestLevel, int durability, int miningSpeed, int attack, float handleModifier,
			int reinforced, float stonebound, String style, int primaryColor, int drawSpeed, float speedMax, float mass,
			float breakChance) {//TODO: Rewrite
		/*TConstructClientRegistry.addMaterialRenderMapping(materialID, domain, renderName, true);
		TConstructRegistry.addToolMaterial(materialID, materialName, harvestLevel, durability, miningSpeed, attack,
				handleModifier, reinforced, stonebound, style, primaryColor);
		PatternBuilder.instance.registerFullMaterial(new ItemStack(material), 2, materialName,
				new ItemStack(TinkerTools.toolShard, 1, materialID), new ItemStack(TinkerTools.toolRod, 1, materialID),
				materialID);
		for (int meta = 0; meta < TinkerTools.patternOutputs.length; meta++) {
			if (TinkerTools.patternOutputs[meta] != null)
				TConstructRegistry.addPartMapping(TinkerTools.woodPattern, meta + 1, materialID,
						new ItemStack(TinkerTools.patternOutputs[meta], 1, materialID));
		}

		if (TConstruct.pulseManager.isPulseLoaded("Tinkers' Weaponry")) {
			for (int m = 0; m < TinkerWeaponry.patternOutputs.length; m++)
				TConstructRegistry.addPartMapping(TinkerWeaponry.woodPattern, m, materialID,
						new ItemStack(TinkerWeaponry.patternOutputs[m], 1, materialID));

			TConstructRegistry.addPartMapping(TinkerTools.woodPattern, 25, materialID,
					new ItemStack(TinkerWeaponry.arrowhead, 1, materialID));

			TConstructRegistry.addBowMaterial(materialID, drawSpeed, speedMax);
			TConstructRegistry.addArrowMaterial(materialID, mass, breakChance);
		}

		TConstructRegistry.addDefaultToolPartMaterial(materialID);
		TConstructRegistry.addDefaultShardMaterial(materialID);*/
	}
}
